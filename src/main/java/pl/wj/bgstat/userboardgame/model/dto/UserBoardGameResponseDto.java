package pl.wj.bgstat.userboardgame.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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
    private double purchasePrice;
    private Date saleDate;
    private double salePrice;
    private String bgName;
    private int bgRecommendedAge;
    private int bgMinPlayersNumber;
    private int bgMaxPlayersNumber;
    private int bgComplexity;
    private int bgPlayingTime;
    private String bgDescription;
}
