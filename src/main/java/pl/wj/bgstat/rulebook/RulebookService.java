package pl.wj.bgstat.rulebook;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.wj.bgstat.boardgame.BoardGameRepository;
import pl.wj.bgstat.exception.RequestEnumException;
import pl.wj.bgstat.exception.RequestFileException;
import pl.wj.bgstat.exception.ResourceExistsException;
import pl.wj.bgstat.exception.ResourceNotFoundException;
import pl.wj.bgstat.rulebook.enumeration.LanguageISO;
import pl.wj.bgstat.rulebook.model.Rulebook;
import pl.wj.bgstat.rulebook.model.dto.RulebookRequestDto;
import pl.wj.bgstat.rulebook.model.dto.RulebookResponseDto;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

import static pl.wj.bgstat.exception.ExceptionHelper.*;
import static pl.wj.bgstat.rulebook.model.RulebookMapper.mapToRulebook;
import static pl.wj.bgstat.rulebook.model.RulebookMapper.mapToRulebookResponseDto;

@Service
@RequiredArgsConstructor
public class RulebookService {
    private static final String RULEBOOKS_PATH = "\\\\localhost\\resources\\rulebooks";

    private final BoardGameRepository boardGameRepository;
    private final RulebookRepository rulebookRepository;

    public RulebookResponseDto addRulebook(RulebookRequestDto rulebookRequestDto, MultipartFile rulebookFile) {
        throwExceptionWhenNotExistsById(rulebookRequestDto.getBoardGameId(), boardGameRepository);
        if (rulebookRequestDto.getLanguageIso() == null) {
            throw new RequestEnumException("languageIso", Arrays.stream(LanguageISO.values()).map(Enum::toString).collect(Collectors.toList()));
        }
        if (rulebookRepository.existsByBoardGameIdAndLanguageIso(rulebookRequestDto.getBoardGameId(), rulebookRequestDto.getLanguageIso())) {
            throw new ResourceExistsException(RULEBOOK_RESOURCE_NAME, Optional.empty());
        }
        String path = String.format("%s\\%d\\%d_%s.pdf", RULEBOOKS_PATH, rulebookRequestDto.getBoardGameId(),
                rulebookRequestDto.getBoardGameId(), rulebookRequestDto.getLanguageIso());

        try {
            File boardGameDirectory = new File(String.format("%s\\%d", RULEBOOKS_PATH, rulebookRequestDto.getBoardGameId()));
            if (!boardGameDirectory.exists()) {
                boardGameDirectory.mkdirs();
            }
            rulebookFile.transferTo(new File(path));
        } catch (IOException e) {
            throw new RequestFileException(rulebookFile.getName());
        }
        Rulebook rulebook = mapToRulebook(rulebookRequestDto, path);
        rulebookRepository.save(rulebook);
        return mapToRulebookResponseDto(rulebook);
    }

    public void deleteRulebook(long id) {
        Rulebook rulebook = rulebookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RULEBOOK_RESOURCE_NAME, ID_FIELD, id));
        if (rulebook.getPath() != null) {
            File file = new File(rulebook.getPath());
            file.delete();
        }
        rulebookRepository.deleteById(id);
    }

    @Transactional(rollbackOn={IOException.class})
    public void deleteAllRulebooksByBoardGameId(long boardGameId) throws IOException {
        throwExceptionWhenNotExistsById(boardGameId, boardGameRepository);
        rulebookRepository.deleteByBoardGameId(boardGameId);
        Path path = Paths.get(String.format("%s\\%d\\", RULEBOOKS_PATH, boardGameId));
        if(Files.notExists(path)) return;
        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }
}
