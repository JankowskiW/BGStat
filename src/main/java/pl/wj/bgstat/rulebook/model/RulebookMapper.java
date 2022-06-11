package pl.wj.bgstat.rulebook.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.digester.Rule;
import pl.wj.bgstat.rulebook.model.dto.RulebookRequestDto;
import pl.wj.bgstat.rulebook.model.dto.RulebookResponseDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RulebookMapper {

    public static Rulebook mapToRulebook(RulebookRequestDto rulebookRequestDto, String path) {
        Rulebook rulebook = new Rulebook();
        rulebook.setBoardGameId(rulebookRequestDto.getBoardGameId());
        rulebook.setLanguageIso(rulebookRequestDto.getLanguageIso());
        rulebook.setPath(path);
        return rulebook;
    }

    public static RulebookResponseDto mapToRulebookResponseDto(Rulebook rulebook) {
        return RulebookResponseDto.builder()
                .id(rulebook.getId())
                .boardGameId(rulebook.getBoardGameId())
                .languageIso(rulebook.getLanguageIso())
                .path(rulebook.getPath())
                .build();
    }
}
