package pl.wj.bgstat.domain.user;

import pl.wj.bgstat.domain.gameplay.model.dto.GameplayHeaderDto;
import pl.wj.bgstat.domain.userboardgame.model.dto.UserBoardGameHeaderDto;

import java.util.ArrayList;
import java.util.List;

public class UserServiceTestHelper {

    public static long userBoardGameId;

    public static List<UserBoardGameHeaderDto> populateUserBoardGameHeaderList(long numberOfElements) {
        List<UserBoardGameHeaderDto> userBoardGameHeaderList = new ArrayList<>();
        UserBoardGameHeaderDto userBoardGameHeader;
        for (int i = 1; i <= numberOfElements; i++) {
            userBoardGameId = i;
            userBoardGameHeader = new UserBoardGameHeaderDto() {
                @Override
                public long getId() {
                    return UserServiceTestHelper.userBoardGameId;
                }

                @Override
                public String getBgName() {
                    return "Name " + userBoardGameId;
                }
            };
            userBoardGameHeaderList.add(userBoardGameHeader);
        }
        userBoardGameId = 0;
        return userBoardGameHeaderList;
    }

    public static List<GameplayHeaderDto> populateUserGameplayHeaderList(long numberOfElements) {
        List<GameplayHeaderDto> userGameplayHeaderList = new ArrayList<>();
        GameplayHeaderDto gameplayHeaderDto;
        for (int i = 1; i <= numberOfElements; i++) {
            gameplayHeaderDto = GameplayHeaderDto.builder()
                    .id(i)
                    .boardGameName("Name " + i)
                    .playtime(50)
                    .build();
            userGameplayHeaderList.add(gameplayHeaderDto);
        }
        return userGameplayHeaderList;
    }
}
