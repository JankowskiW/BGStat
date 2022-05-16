package pl.wj.bgstat.userboardgame.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public interface UserBoardGameDetails {
    long getId();
    long getBoardGameId();
    long getObjectTypeId();
    boolean isSleeved();
    String getComment();
    Date getPurchaseDate();
    BigDecimal getPurchasePrice();
    Date getSaleDate();
    BigDecimal getSalePrice();
    String getBgName();
    int getBgRecommendedAge();
    int getBgMinPlayersNumber();
    int getBgMaxPlayersNumber();
    int getBgComplexity();
    int getBgEstimatedPlaytime();
    String getBgDescription();
}
