package pl.wj.bgstat.domain.gameplay.model.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameplayRequestDto {
    private long objectTypeId;
    @NotNull(message = "User id cannot be empty")
    @Min(value = 1, message = "User id should be positive integer number")
    private long userId;
    @NotNull(message = "Board game id cannot be empty")
    @Min(value = 1, message = "Board game id should be positive integer number")
    private long boardGameId;
    @NotNull(message = "User board game id cannot be empty")
    @Min(value = 1, message = "User board game id should be positive integer number")
    private long userBoardGameId;
    @NotBlank(message = "Comment cannot be blank")
    private String comment;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "Gameplay start time cannot be null")
    private LocalDateTime startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "Gameplay end time cannot be null")
    private LocalDateTime endTime;
    @NotNull(message = "Playtime cannot be empty")
    @Min(value = 1, message = "Playtime should be positive integer number")
    private int playtime;
}
