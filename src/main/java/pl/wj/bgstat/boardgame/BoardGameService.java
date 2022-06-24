package pl.wj.bgstat.boardgame;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.wj.bgstat.attribute.AttributeRepository;
import pl.wj.bgstat.boardgame.model.BoardGame;
import pl.wj.bgstat.boardgame.model.BoardGameMapper;
import pl.wj.bgstat.boardgame.model.dto.*;
import pl.wj.bgstat.exception.*;
import pl.wj.bgstat.gameplay.GameplayRepository;
import pl.wj.bgstat.rulebook.RulebookService;
import pl.wj.bgstat.systemobjecttype.SystemObjectTypeRepository;
import pl.wj.bgstat.userboardgame.UserBoardGameRepository;

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
    private final AttributeRepository attributeRepository;
    private final UserBoardGameRepository userBoardGameRepository;
    private final GameplayRepository gameplayRepository;

    private final RulebookService rulebookService;

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
            File file = new File(boardGameThumbnailResponseDto.getThumbnailPath());
            file.delete();
        }
        boardGameThumbnailResponseDto.setThumbnailPath(createThumbnail(thumbnailFile));
        boardGameRepository.updateThumbnailPathById(boardGameThumbnailResponseDto.getId(), boardGameThumbnailResponseDto.getThumbnailPath());
        return boardGameThumbnailResponseDto;
    }

    @Transactional
    public void deleteBoardGame(long id) {
        BoardGame boardGame = boardGameRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(BOARD_GAME_RESOURCE_NAME, ID_FIELD, id));
        String thumbnailPath = boardGame.getThumbnailPath();

        attributeRepository.deleteByObjectIdAndObjectTypeId(id, boardGame.getObjectTypeId());
        userBoardGameRepository.deleteByBoardGameId(id);
        try {
            rulebookService.deleteAllRulebooksByBoardGameId(id);
        } catch (IOException e) {
            throw new FileException("Cannot remove rulebooks file or directory");
        }
        gameplayRepository.deleteByBoardGameId(id);
        boardGameRepository.deleteById(id);
        if (boardGameRepository.existsById(id)) return;

        if (thumbnailPath != null) {
            File file = new File(thumbnailPath);
            file.delete();
        }
    }


    private String createThumbnail(MultipartFile thumbnailFile) {
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
