package pl.wj.bgstat.boardgame.model.dto;


import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardGamePartialRequestDto {
    @Length(min = 1, message = "Name cannot by empty")
    private String name;
    @Min(value = 1, message = "Recommended age cannot be 0")
    private Integer recommendedAge;
    @Min(value = 1, message = "Minimum number of players cannot be 0")
    private Integer minPlayersNumber;
    @Min(value = 1, message = "Maximum number of players cannot be 0")
    private Integer maxPlayersNumber;
    @Min(value = 1, message = "Minimum number of players should be between 1 and 10")
    @Max(value = 10, message = "Minimum number of players should be between 1 and 10")
    private Integer complexity;
    @Min(value = 1, message = "Estimated playtime cannot be 0")
    private Integer estimatedPlaytime;
    @Length(min = 1, message = "Description cannot by empty")
    private String description;
}
