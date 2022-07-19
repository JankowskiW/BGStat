package pl.wj.bgstat.domain.userboardgame.model.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBoardGameRequestDto {
    @NotNull(message = "Object type id cannot be null")
    @Min(value = 1, message = "Object type id should be positive integer number")
    private long objectTypeId;
    @NotNull(message = "Board game id cannot be null")
    @Min(value = 1, message = "Board game id should be positive integer number")
    private long boardGameId;
    @NotNull(message = "User id cannot be null")
    @Min(value = 1, message = "User id should be positive integer number")
    private long userId;
    private Long storeId;
    @NotNull(message = "Sleeved flag cannot be null")
    private boolean sleeved;
    @NotBlank(message = "Comment cannot be blank")
    @Length(max = 500, message = "Comment cannot be longer than 500 characters")
    private String comment;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;
    private BigDecimal purchasePrice;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate saleDate;
    private BigDecimal salePrice;
}
