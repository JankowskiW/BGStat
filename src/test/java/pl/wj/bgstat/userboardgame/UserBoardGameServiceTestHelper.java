package pl.wj.bgstat.userboardgame;

import pl.wj.bgstat.userboardgame.model.UserBoardGame;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameHeaderDto;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameRequestDto;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameDetailsDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserBoardGameServiceTestHelper {

    public static List<UserBoardGameDetailsDto> populateUserBoardGameResponseDtoList(int numberOfElements) {
        List<UserBoardGameDetailsDto> userBoardGameDetailsDtoList = new ArrayList<>();
        UserBoardGameDetailsDto userBoardGameDetailsDto;
        for (int i = 1; i <= numberOfElements; i++) {
            userBoardGameDetailsDto = new UserBoardGameDetailsDto(
                  i, i, 1, true, "Comment", new Date(), new BigDecimal(150.0),
                    null, null, "Board Game Name " + i, 10,
                    1, 5, 3, 120, "DESCRIPTION " + i
            );
            userBoardGameDetailsDtoList.add(userBoardGameDetailsDto);
        }
        return userBoardGameDetailsDtoList;
    }

    public static List<UserBoardGameHeaderDto> populateUserBoardGameHeaderDtoList(
            List<UserBoardGameDetailsDto> userBoardGameDetailsDtoList) {
        List<UserBoardGameHeaderDto> userBoardGameHeaderDtoList = new ArrayList<>();
        userBoardGameDetailsDtoList.stream().forEach(
                ubg -> userBoardGameHeaderDtoList.add(new UserBoardGameHeaderDto(ubg.getId(), ubg.getBgName()))
        );
        return userBoardGameHeaderDtoList;
    }

    public static UserBoardGameRequestDto createUserBoardGameRequestDto() {
        return new UserBoardGameRequestDto(
                2,
                1,
                1,
                1,
                true,
                "Comment",
                new Date(),
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
        userBoardGame.setPurchaseDate(new Date());
        userBoardGame.setPurchasePrice(new BigDecimal(155.55));
        userBoardGame.setSaleDate(null);
        userBoardGame.setSalePrice(null);
        return userBoardGame;
    }
}
