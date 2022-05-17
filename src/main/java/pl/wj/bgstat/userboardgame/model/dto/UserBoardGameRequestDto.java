package pl.wj.bgstat.userboardgame.model.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.Date;

@Setter
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
    private long storeId;
    @NotNull
    private boolean sleeved;
    @NotBlank @Length(max = 500)
    private String comment;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Please provide a purchase date")
    private Date purchaseDate;
    @NotNull @Min(1)
    private BigDecimal purchasePrice;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date saleDate;
    @Min(1)
    private BigDecimal salePrice;
}
