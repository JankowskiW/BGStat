package pl.wj.bgstat.rulebook;

import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.wj.bgstat.boardgame.BoardGameRepository;
import pl.wj.bgstat.exception.ResourceExistsException;
import pl.wj.bgstat.rulebook.model.dto.RulebookRequestDto;
import pl.wj.bgstat.rulebook.model.dto.RulebookResponseDto;

import static pl.wj.bgstat.exception.ExceptionHelper.throwExceptionWhenNotExistsById;

@Service
@RequiredArgsConstructor
public class RulebookService {

    private static final String RULEBOOKS_PATH = "\\\\localhost\\resources\\rulebooks";

    private final BoardGameRepository boardGameRepository;
    private final RulebookRepository rulebookRepository;


    public RulebookResponseDto addOrReplaceRulebook(RulebookRequestDto rulebookRequestDto, MultipartFile rulebook) {
        throwExceptionWhenNotExistsById(rulebookRequestDto.getBoardGameId(), boardGameRepository);
        if (rulebookRepository.existsByBoardGameIdAndLanguageIso(rulebookRequestDto.getBoardGameId(), rulebookRequestDto.getLanguageIso().toString())) {
            throw new ResourceExistsException("Rulebook");
        }



        throw new NotYetImplementedException();
    }
}
