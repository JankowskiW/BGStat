package pl.wj.bgstat.domain.userboardgame.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface UserBoardGameDetailsDto {
    long getId();
    long getBoardGameId();
    long getObjectTypeId();
    boolean isSleeved();
    String getComment();
    LocalDate getPurchaseDate();
    BigDecimal getPurchasePrice();
    LocalDate getSaleDate();
    BigDecimal getSalePrice();
    String getBgName();
    int getBgRecommendedAge();
    int getBgMinPlayersNumber();
    int getBgMaxPlayersNumber();
    int getBgComplexity();
    int getBgEstimatedPlaytime();
    String getBgDescription();
}
