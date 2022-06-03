package pl.wj.bgstat.boardgame;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.multipart.MultipartFile;
import pl.wj.bgstat.boardgame.model.BoardGame;
import pl.wj.bgstat.boardgame.model.BoardGameMapper;
import pl.wj.bgstat.boardgame.model.dto.BoardGameHeaderDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGamePartialRequestDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGameRequestDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGameResponseDto;
import pl.wj.bgstat.exception.*;
import pl.wj.bgstat.systemobjecttype.SystemObjectTypeRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

    public BoardGameResponseDto addBoardGame(BoardGameRequestDto boardGameRequestDto, MultipartFile thumbnail)
            throws HttpMediaTypeNotSupportedException, IOException {
        boardGameRequestDto.setObjectTypeId(validateSystemObjectTypeId(boardGameRequestDto.getObjectTypeId()));
        throwExceptionWhenExistsByName(boardGameRequestDto.getName());
        throwExceptionWhenNotExistsById(boardGameRequestDto.getObjectTypeId(), systemObjectTypeRepository);
        BoardGame boardGame = BoardGameMapper.mapToBoardGame(boardGameRequestDto);
        if (!thumbnail.isEmpty() && thumbnail.getContentType() != null) {
            MediaType mediaType = MediaType.valueOf(thumbnail.getContentType());

            if (SUPPORTED_THUMBNAIL_MEDIA_TYPES.stream().noneMatch(p -> p.equals(mediaType))) {
                // TODO: 02.06.2022 Change to custom exception
                throw new UnsupportedFileMediaTypeException(mediaType, ExceptionHelper.SUPPORTED_THUMBNAIL_MEDIA_TYPES);
            }

            BufferedImage biThumbnail = null;
            // TODO: 02.06.2022 Surround with try catch block and throw custom IOException
            biThumbnail = ImageIO.read(thumbnail.getInputStream());
            if (!(biThumbnail.getHeight() >= MIN_THUMBNAIL_HEIGHT && biThumbnail.getHeight() <= MAX_THUMBNAIL_HEIGHT &&
                    biThumbnail.getWidth() >= MIN_THUMBNAIL_WIDTH && biThumbnail.getWidth() <= MAX_THUMBNAIL_WIDTH &&
                    thumbnail.getSize() <= MAX_THUMBNAIL_SIZE))
                throw new RequestFileException(createRequestFileExceptionMessage(
                        "Thumbnail", MIN_THUMBNAIL_HEIGHT, MAX_THUMBNAIL_HEIGHT,
                        MIN_THUMBNAIL_WIDTH, MAX_THUMBNAIL_WIDTH, MAX_THUMBNAIL_SIZE));

            String thumbnailPath = String.format("%s\\%s.%s", THUMBNAILS_PATH, UUID.nameUUIDFromBytes(thumbnail.getBytes()), mediaType.getSubtype());
            ImageIO.write(biThumbnail, mediaType.getSubtype(), new File(thumbnailPath));

            boardGame.setThumbnailPath(thumbnailPath);

        }
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

    public void deleteBoardGame(long id) {
        throwExceptionWhenNotExistsById(id, boardGameRepository);
        boardGameRepository.deleteById(id);
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
