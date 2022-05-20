package pl.wj.bgstat.userboardgame;

import pl.wj.bgstat.userboardgame.model.UserBoardGame;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameDetailsDto;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameRequestDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

public class UserBoardGameServiceTestHelper {

    public static UserBoardGameDetailsDto createUserBoardGameDetailsImpl(long userBoardGameId) {
        return new UserBoardGameDetailsDto() {
            @Override
            public long getId() {
                return userBoardGameId;
            }

            @Override
            public long getBoardGameId() {
                return 1;
            }

            @Override
            public long getObjectTypeId() {
                return 1;
            }

            @Override
            public boolean isSleeved() {
                return true;
            }

            @Override
            public String getComment() {
                return "Comment";
            }

            @Override
            public LocalDate getPurchaseDate() {
                return LocalDate.of(2021, Month.APRIL,01);
            }

            @Override
            public BigDecimal getPurchasePrice() {
                return new BigDecimal(150.55);
            }

            @Override
            public LocalDate getSaleDate() {
                return null;
            }

            @Override
            public BigDecimal getSalePrice() {
                return null;
            }

            @Override
            public String getBgName() {
                return "Board Game Name";
            }

            @Override
            public int getBgRecommendedAge() {
                return 10;
            }

            @Override
            public int getBgMinPlayersNumber() {
                return 1;
            }

            @Override
            public int getBgMaxPlayersNumber() {
                return 6;
            }

            @Override
            public int getBgComplexity() {
                return 4;
            }

            @Override
            public int getBgEstimatedPlaytime() {
                return 120;
            }

            @Override
            public String getBgDescription() {
                return "Description";
            }
        };
    }

    public static UserBoardGameRequestDto createUserBoardGameRequestDto() {
        return new UserBoardGameRequestDto(
                2,
                1,
                1,
                1,
                true,
                "Comment",
                LocalDate.of(1998,Month.MARCH,2),
                new BigDecimal(150.50),
                null,
                null
        );
    }

    public static UserBoardGame createUserBoardGame(long id, long boardGameId, long userId, long storeId) {
        UserBoardGame userBoardGame = new UserBoardGame();
        userBoardGame.setId(id);
        userBoardGame.setObjectTypeId(1L);
        userBoardGame.setBoardGameId(boardGameId);
        userBoardGame.setUserId(userId);
        userBoardGame.setStoreId(storeId);
        userBoardGame.setSleeved(true);
        userBoardGame.setComment("Comment");
        userBoardGame.setPurchaseDate(LocalDate.of(2022, Month.DECEMBER, 31));
        userBoardGame.setPurchasePrice(new BigDecimal(155.55));
        userBoardGame.setSaleDate(null);
        userBoardGame.setSalePrice(null);
        return userBoardGame;
    }
}
