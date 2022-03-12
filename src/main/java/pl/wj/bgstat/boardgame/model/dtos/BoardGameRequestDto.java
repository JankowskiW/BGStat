package pl.wj.bgstat.boardgame.model.dtos;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Getter
@Builder
public class BoardGameRequestDto {
    // TODO: Make possible to pass only few attributes e.g: when user want to change only name or only description
    // TODO: Make PATCH method instead of PUT method to edit only few attributes

    @NotBlank @Length(max = 255)
    private String name;
    @NotNull @Min(1)
    private int recommendedAge;
    @NotNull @Min(1)
    private int minPlayersNumber;
    @NotNull @Min(1)
    private int maxPlayersNumber;
    @NotNull @Min(1) @Max(10)
    private int complexity;
    @NotNull(message = "Field ") @Min(1)
    private int playingTime;
    @NotBlank
    private String description;
}
