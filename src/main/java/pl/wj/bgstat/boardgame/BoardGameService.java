package pl.wj.bgstat.boardgame;

import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.InvalidMimeTypeException;
import org.springframework.util.MimeType;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import pl.wj.bgstat.boardgame.model.BoardGame;
import pl.wj.bgstat.boardgame.model.BoardGameMapper;
import pl.wj.bgstat.boardgame.model.dto.*;
import pl.wj.bgstat.exception.ExceptionHelper;
import pl.wj.bgstat.exception.RequestFileException;
import pl.wj.bgstat.exception.ResourceExistsException;
import pl.wj.bgstat.exception.ResourceNotFoundException;
import pl.wj.bgstat.systemobjecttype.SystemObjectTypeRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDate;

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

    public BoardGameResponseDto addBoardGame(BoardGameRequestDto boardGameRequestDto, MultipartFile thumbnail) throws IOException, HttpMediaTypeNotSupportedException {


        MediaType mediaType = MediaType.valueOf(thumbnail.getContentType());

        System.out.println("Name = " + thumbnail.getName());
        System.out.println("OrigName = " + thumbnail.getOriginalFilename());
        System.out.println("CType = " + thumbnail.getContentType());
        System.out.println("Size = " + thumbnail.getSize());
        System.out.println("Res = " + thumbnail.getResource());

        if (!SUPPORTED_THUMBNAIL_MEDIA_TYPES.stream().anyMatch(p -> p.equals(mediaType))) {
            throw new HttpMediaTypeNotSupportedException(mediaType, ExceptionHelper.SUPPORTED_THUMBNAIL_MEDIA_TYPES);
        }

        boardGameRequestDto.setObjectTypeId(validateSystemObjectTypeId(boardGameRequestDto.getObjectTypeId()));
        throwExceptionWhenExistsByName(boardGameRequestDto.getName());
        throwExceptionWhenNotExistsById(boardGameRequestDto.getObjectTypeId(), systemObjectTypeRepository);
        BoardGame boardGame = BoardGameMapper.mapToBoardGame(boardGameRequestDto);

        if (!thumbnail.isEmpty()) {
//            File tmpThumbnail = new File("src/main/resources/tagetFile.");
//            try (OutputStream os = new FileOutputStream(tmpThumbnail)) {
//                os.write(thumbnail.getBytes());
//            }

            BufferedImage bThumbnail = ImageIO.read(thumbnail.getInputStream());

            if (!(bThumbnail.getHeight() >= MIN_THUMBNAIL_HEIGHT && bThumbnail.getHeight() <= MAX_THUMBNAIL_HEIGHT &&
                    bThumbnail.getWidth() >= MIN_THUMBNAIL_WIDTH && bThumbnail.getWidth() <= MAX_THUMBNAIL_WIDTH &&
                    thumbnail.getSize() > MAX_THUMBNAIL_SIZE))
                throw new RequestFileException(createRequestFileExceptionMessage("Thumbnail",
                        MIN_THUMBNAIL_HEIGHT, MAX_THUMBNAIL_HEIGHT, MIN_THUMBNAIL_WIDTH,
                        MAX_THUMBNAIL_WIDTH, MAX_THUMBNAIL_SIZE));
            boardGame.setThumbnailPath(thumbnail.getContentType());
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
