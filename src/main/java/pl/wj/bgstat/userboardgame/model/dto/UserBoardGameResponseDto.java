package pl.wj.bgstat.userboardgame.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
public class UserBoardGameResponseDto {
    private long id;
    private long boardGameId;
    private long objectTypeId;
    private boolean sleeved;
    private String comment;
    private Date purchaseDate;
    private BigDecimal purchasePrice;
    private Date saleDate;
    private BigDecimal salePrice;
    private String bgName;
    private int bgRecommendedAge;
    private int bgMinPlayersNumber;
    private int bgMaxPlayersNumber;
    private int bgComplexity;
    private int bgPlayingTime;
    private String bgDescription;
}
