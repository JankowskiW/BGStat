package pl.wj.bgstat.userboardgame;

import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameHeaderDto;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameResponseDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserBoardGameServiceTestHelper {

    public static List<UserBoardGameResponseDto> populateUserBoardGameResponseDtoList(int numberOfElements) {
        List<UserBoardGameResponseDto> userBoardGameResponseDtoList = new ArrayList<>();
        UserBoardGameResponseDto userBoardGameResponseDto;
        for (int i = 1; i <= numberOfElements; i++) {
            userBoardGameResponseDto = new UserBoardGameResponseDto(
                  i, i, 1, true, "Comment", new Date(), new BigDecimal(150.0),
                    null, null, "Board Game Name " + i, 10,
                    1, 5, 3, 120, "DESCRIPTION " + i
            );
            userBoardGameResponseDtoList.add(userBoardGameResponseDto);
        }
        return userBoardGameResponseDtoList;
    }

    public static List<UserBoardGameHeaderDto> populateUserBoardGameHeaderDtoList(
            List<UserBoardGameResponseDto> userBoardGameResponseDtoList) {
        List<UserBoardGameHeaderDto> userBoardGameHeaderDtoList = new ArrayList<>();
        userBoardGameResponseDtoList.stream().forEach(
                ubg -> userBoardGameHeaderDtoList.add(new UserBoardGameHeaderDto(ubg.getId(), ubg.getBgName()))
        );
        return userBoardGameHeaderDtoList;
    }
}
