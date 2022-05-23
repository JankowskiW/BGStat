package pl.wj.bgstat.gameplay.model.dto;

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
    @NotNull @Min(1)
    private long userId;
    @NotNull @Min(1)
    private long boardGameId;
    @NotNull @Min(1)
    private long userBoardGameId;
    @NotBlank
    private String comment;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "Please provide a start time.")
    private LocalDateTime startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "Please provide a end time.")
    private LocalDateTime endTime;
    @NotNull @Min(1)
    private int playtime;
}
