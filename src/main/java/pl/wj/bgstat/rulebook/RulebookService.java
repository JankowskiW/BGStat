package pl.wj.bgstat.rulebook;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.wj.bgstat.boardgame.BoardGameRepository;
import pl.wj.bgstat.exception.RequestEnumException;
import pl.wj.bgstat.exception.RequestFileException;
import pl.wj.bgstat.exception.ResourceExistsException;
import pl.wj.bgstat.rulebook.enumeration.LanguageISO;
import pl.wj.bgstat.rulebook.model.Rulebook;
import pl.wj.bgstat.rulebook.model.dto.RulebookRequestDto;
import pl.wj.bgstat.rulebook.model.dto.RulebookResponseDto;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import static pl.wj.bgstat.exception.ExceptionHelper.throwExceptionWhenNotExistsById;
import static pl.wj.bgstat.rulebook.model.RulebookMapper.mapToRulebook;
import static pl.wj.bgstat.rulebook.model.RulebookMapper.mapToRulebookResponseDto;

@Service
@RequiredArgsConstructor
public class RulebookService {

    private static final String RULEBOOKS_PATH = "\\\\localhost\\resources\\rulebooks";

    private final BoardGameRepository boardGameRepository;
    private final RulebookRepository rulebookRepository;

    public RulebookResponseDto addOrReplaceRulebook(RulebookRequestDto rulebookRequestDto, MultipartFile rulebookFile) {
        throwExceptionWhenNotExistsById(rulebookRequestDto.getBoardGameId(), boardGameRepository);
        if (rulebookRequestDto.getLanguageIso() == null) {
            throw new RequestEnumException("languageIso", Arrays.stream(LanguageISO.values()).map(Enum::toString).collect(Collectors.toList()));
        }
        if (rulebookRepository.existsByBoardGameIdAndLanguageIso(rulebookRequestDto.getBoardGameId(), rulebookRequestDto.getLanguageIso())) {
            throw new ResourceExistsException("Rulebook");
        }
        String path = RULEBOOKS_PATH + String.format("\\%d\\%d_%s.pdf", rulebookRequestDto.getBoardGameId(),
                rulebookRequestDto.getBoardGameId(), rulebookRequestDto.getLanguageIso());
        try {
            File boardGameDirectory = new File(String.format("%s\\%d", RULEBOOKS_PATH, rulebookRequestDto.getBoardGameId()));
            if (!boardGameDirectory.exists()) {
                if(!boardGameDirectory.mkdirs())
                    throw new RequestFileException(rulebookFile.getName());
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
}
