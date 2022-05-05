package pl.wj.bgstat.userboardgame.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserBoardGameHeaderDto {
    private long id;
    private long bgName;
}
