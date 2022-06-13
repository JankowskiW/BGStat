package pl.wj.bgstat.boardgame;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.wj.bgstat.boardgame.model.BoardGame;
import pl.wj.bgstat.boardgame.model.BoardGameMapper;
import pl.wj.bgstat.boardgame.model.dto.*;
import pl.wj.bgstat.exception.*;
import pl.wj.bgstat.systemobjecttype.SystemObjectTypeRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static pl.wj.bgstat.exception.ExceptionHelper.*;

@Service
@RequiredArgsConstructor
public class BoardGameService {

    private static final long BOARD_GAME_DEFAULT_OBJECT_TYPE_ID = 1L;
    private static final int MIN_THUMBNAIL_HEIGHT = 600;
    private static final int MAX_THUMBNAIL_HEIGHT = 1200;
    private static final int MIN_THUMBNAIL_WIDTH = 600;
    private static final int MAX_THUMBNAIL_WIDTH = 1200;
    private static final int MAX_THUMBNAIL_SIZE = 10240;
    private static final String THUMBNAILS_PATH = "\\\\localhost\\resources\\thumbnails";


    private final BoardGameRepository boardGameRepository;
    private final SystemObjectTypeRepository systemObjectTypeRepository;

    public Page<BoardGameHeaderDto> getBoardGameHeaders(Pageable pageable) {
        return boardGameRepository.findBoardGameHeaders(pageable);
    }
    
    public BoardGameResponseDto getSingleBoardGame(long id) {
        BoardGame boardGame = boardGameRepository.findWithDescriptionById(id)
                .orElseThrow(() -> new ResourceNotFoundException(BOARD_GAME_RESOURCE_NAME, ID_FIELD, id));
        return BoardGameMapper.mapToBoardGameResponseDto(boardGame);
    }

    public BoardGameResponseDto addBoardGame(BoardGameRequestDto boardGameRequestDto, MultipartFile thumbnailFile) {
        boardGameRequestDto.setObjectTypeId(validateSystemObjectTypeId(boardGameRequestDto.getObjectTypeId()));
        throwExceptionWhenExistsByName(boardGameRequestDto.getName());
        throwExceptionWhenNotExistsById(boardGameRequestDto.getObjectTypeId(), systemObjectTypeRepository);
        BoardGame boardGame = BoardGameMapper.mapToBoardGame(boardGameRequestDto);
        String thumbnailPath = createThumbnail(thumbnailFile);
        boardGame.setThumbnailPath(thumbnailPath);
        boardGameRepository.save(boardGame);
        return BoardGameMapper.mapToBoardGameResponseDto(boardGame);
    }

    public BoardGameResponseDto editBoardGame(long id, BoardGameRequestDto boardGameRequestDto) {
        boardGameRequestDto.setObjectTypeId(validateSystemObjectTypeId(boardGameRequestDto.getObjectTypeId()));
        throwExceptionWhenNotExistsById(id, boardGameRepository);
        throwExceptionWhenExistsByNameAndNotId(id, boardGameRequestDto.getName());
        throwExceptionWhenNotExistsById(boardGameRequestDto.getObjectTypeId(), systemObjectTypeRepository);

        BoardGame boardGame = BoardGameMapper.mapToBoardGame(id, boardGameRequestDto);
        boardGameRepository.save(boardGame);
        return BoardGameMapper.mapToBoardGameResponseDto(boardGame);
    }

    public BoardGameResponseDto editBoardGamePartially(long id, BoardGamePartialRequestDto boardGamePartialRequestDto) {
        throwExceptionWhenExistsByNameAndNotId(id, boardGamePartialRequestDto.getName());
        BoardGame boardGame = boardGameRepository.findWithDescriptionById(id)
                .orElseThrow(() -> new ResourceNotFoundException(BOARD_GAME_RESOURCE_NAME, ID_FIELD, id));

        boardGame = BoardGameMapper.mapToBoardGame(boardGame, boardGamePartialRequestDto);

        boardGameRepository.save(boardGame);

        return BoardGameMapper.mapToBoardGameResponseDto(boardGame);
    }

    public BoardGameThumbnailResponseDto addOrReplaceThumbnail(long boardGameId, MultipartFile thumbnailFile) {
        throwExceptionWhenNotExistsById(boardGameId, boardGameRepository);
        BoardGameThumbnailResponseDto boardGameThumbnailResponseDto = boardGameRepository.findThumbnailPath(boardGameId);
        if (boardGameThumbnailResponseDto.getThumbnailPath() != null) {
            try {
                File file = new File(boardGameThumbnailResponseDto.getThumbnailPath());
                file.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        boardGameThumbnailResponseDto.setThumbnailPath(createThumbnail(thumbnailFile));
        boardGameRepository.updateThumbnailPathById(boardGameThumbnailResponseDto.getId(), boardGameThumbnailResponseDto.getThumbnailPath());
        return boardGameThumbnailResponseDto;
    }

    public void deleteBoardGame(long id) {
        // TODO: 11.06.2022 Delete thumbnail and rulebooks when client delete board game
        throwExceptionWhenNotExistsById(id, boardGameRepository);
        boardGameRepository.deleteById(id);
    }


    private String createThumbnail(MultipartFile thumbnailFile) {
        // TODO: 11.06.2022 Check if ImageIO.write in tests create real file and if it is, then stub that method
        String thumbnailPath = null;
        if (thumbnailFile != null && !thumbnailFile.isEmpty() && thumbnailFile.getContentType() != null) {
            MediaType mediaType = MediaType.valueOf(thumbnailFile.getContentType());

            if (SUPPORTED_THUMBNAIL_MEDIA_TYPES.stream().noneMatch(p -> p.equals(mediaType))) {
                throw new UnsupportedFileMediaTypeException(mediaType, ExceptionHelper.SUPPORTED_THUMBNAIL_MEDIA_TYPES);
            }
            try {
                InputStream is = thumbnailFile.getInputStream();
                BufferedImage biThumbnail = ImageIO.read(is);
                if (!(biThumbnail.getHeight() >= MIN_THUMBNAIL_HEIGHT && biThumbnail.getHeight() <= MAX_THUMBNAIL_HEIGHT &&
                        biThumbnail.getWidth() >= MIN_THUMBNAIL_WIDTH && biThumbnail.getWidth() <= MAX_THUMBNAIL_WIDTH &&
                        thumbnailFile.getSize() <= MAX_THUMBNAIL_SIZE))
                    throw new RequestFileException(
                            "Thumbnail", MIN_THUMBNAIL_HEIGHT, MAX_THUMBNAIL_HEIGHT,
                            MIN_THUMBNAIL_WIDTH, MAX_THUMBNAIL_WIDTH, MAX_THUMBNAIL_SIZE);

                thumbnailPath = String.format("%s\\%s.%s", THUMBNAILS_PATH, UUID.randomUUID(), mediaType.getSubtype());
                ImageIO.write(biThumbnail, mediaType.getSubtype(), new File(thumbnailPath));
            } catch (IOException e) {
                throw new RequestFileException(thumbnailFile.getName());
            }
        }
        return thumbnailPath;
    }

    private void throwExceptionWhenExistsByName(String name) {
        if (boardGameRepository.existsByName(name))
            throw new ResourceExistsException(BOARD_GAME_RESOURCE_NAME, NAME_FIELD);
    }

    private void throwExceptionWhenExistsByNameAndNotId(long id, String name) {
        if (boardGameRepository.existsByNameAndIdNot(name, id))
            throw new ResourceExistsException(BOARD_GAME_RESOURCE_NAME, NAME_FIELD);
    }

    private long validateSystemObjectTypeId(long id) {
        return id == 0 ? BOARD_GAME_DEFAULT_OBJECT_TYPE_ID : id;
    }

}
