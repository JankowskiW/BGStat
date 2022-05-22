package pl.wj.bgstat.gameplay.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameplayResponseDto {
    private long id;
    private long objectTypeId;
    private long userId;
    private long boardGameId;
    private long userBoardGameId;
    private String comment;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int playtime;
}
