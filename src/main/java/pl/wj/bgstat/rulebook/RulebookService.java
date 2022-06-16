package pl.wj.bgstat.rulebook;

import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.wj.bgstat.boardgame.BoardGameRepository;
import pl.wj.bgstat.boardgame.model.BoardGame;
import pl.wj.bgstat.exception.RequestEnumException;
import pl.wj.bgstat.exception.RequestFileException;
import pl.wj.bgstat.exception.ResourceExistsException;
import pl.wj.bgstat.exception.ResourceNotFoundException;
import pl.wj.bgstat.rulebook.enumeration.LanguageISO;
import pl.wj.bgstat.rulebook.model.Rulebook;
import pl.wj.bgstat.rulebook.model.dto.RulebookRequestDto;
import pl.wj.bgstat.rulebook.model.dto.RulebookResponseDto;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
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
            throw new ResourceExistsException("Rulebook");
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
            e.printStackTrace();
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
            try {
                File file = new File(rulebook.getPath());
                file.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        rulebookRepository.deleteById(id);
    }
}
