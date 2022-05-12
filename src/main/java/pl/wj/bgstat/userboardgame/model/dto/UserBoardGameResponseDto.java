package pl.wj.bgstat.userboardgame.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
public class UserBoardGameResponseDto {
    private long id;
    private long objectTypeId;
    private long boardGameId;
    private long userId;
    private long storeId;
    private boolean sleeved;
    private String comment;
    private Date purchaseDate;
    private BigDecimal purchasePrice;
    private Date saleDate;
    private BigDecimal salePrice;
}
