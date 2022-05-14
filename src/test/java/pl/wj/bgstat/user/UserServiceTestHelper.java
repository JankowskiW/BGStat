package pl.wj.bgstat.user;

import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameDetails;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameHeader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserServiceTestHelper {

    public static long userBoardGameId;

    public static List<UserBoardGameHeader> populateUserBoardGameHeaderList(long numberOfElements) {
        List<UserBoardGameHeader> userBoardGameHeaderList = new ArrayList<>();
        UserBoardGameHeader userBoardGameHeader;
        for (int i = 1; i <= numberOfElements; i++) {
            userBoardGameId = i;
            userBoardGameHeader = new UserBoardGameHeader() {
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
        return userBoardGameHeaderList;
    }
}
