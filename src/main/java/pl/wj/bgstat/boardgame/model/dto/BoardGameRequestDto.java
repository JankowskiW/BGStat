package pl.wj.bgstat.boardgame.model.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardGameRequestDto {
    @NotBlank @Length(max = 255)
    private String name;
    private long objectTypeId;
    @NotNull @Min(1)
    private int recommendedAge;
    @NotNull @Min(1)
    private int minPlayersNumber;
    @NotNull @Min(1)
    private int maxPlayersNumber;
    @NotNull @Min(1) @Max(10)
    private int complexity;
    @NotNull @Min(1)
    private int playingTime;
    @NotBlank
    private String description;
}
