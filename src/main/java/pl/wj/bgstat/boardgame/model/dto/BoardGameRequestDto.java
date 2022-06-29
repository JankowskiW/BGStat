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
    @Length(min = 1, max = 255,  message = "Name cannot by empty and should not be longer than 255 characters")
    private String name;
    private long objectTypeId;
    @NotNull(message = "Recommended age should not be null")
    @Min(value = 1, message = "Recommended age should be positive integer number")
    private int recommendedAge;
    @NotNull(message = "Minimum number of players should not be null")
    @Min(value = 1, message = "Minimum number of players should be positive integer number")
    private int minPlayersNumber;
    @NotNull(message = "Maximum number of players should not be null")
    @Min(value = 1, message = "Maximum number of players should be positive integer number")
    private int maxPlayersNumber;
    @NotNull(message = "Complexity should not be null")
    @Min(value = 1, message = "Complexity should be integer number between 1 and 10")
    @Max(value = 10, message = "Complexity should be integer number between 1 and 10")
    private int complexity;
    @NotNull(message = "Estimated playtime should not be null")
    @Min(value = 1, message = "Estimated playtime should be positive integer number")
    private int estimatedPlaytime;
    @Length(min = 1, max = 400, message = "Description cannot by empty or longer than 400 characters")
    private String description;

}
