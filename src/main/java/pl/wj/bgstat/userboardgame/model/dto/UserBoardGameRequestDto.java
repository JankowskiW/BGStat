package pl.wj.bgstat.userboardgame.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBoardGameRequestDto {
    @NotNull @Min(1)
    private long objectTypeId;
    @NotNull @Min(1)
    private long boardGameId;
    @NotNull @Min(1)
    private long userId;
    @NotNull @Min(1)
    private long shopId;
    @NotNull
    private boolean sleeved;
    @NotBlank @Length(max = 500)
    private String comment;
    private Date purchaseDate;
    private double purchasePrice;
    private Date saleDate;
    private double salePrice;
}
