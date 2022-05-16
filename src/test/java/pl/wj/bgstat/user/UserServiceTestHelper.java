package pl.wj.bgstat.user;

import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameHeaderDto;

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
}
