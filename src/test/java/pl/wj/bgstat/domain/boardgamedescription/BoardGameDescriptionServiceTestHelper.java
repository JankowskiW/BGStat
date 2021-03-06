package pl.wj.bgstat.domain.boardgamedescription;

import pl.wj.bgstat.domain.boardgame.model.BoardGame;
import pl.wj.bgstat.domain.boardgamedescription.model.BoardGameDescription;

import java.util.ArrayList;
import java.util.List;

public class BoardGameDescriptionServiceTestHelper {

    public static List<BoardGameDescription> populateBoardGameDescriptionList(int numberOfElements) {
        List<BoardGameDescription> boardGameDescriptionList = new ArrayList<>();
        for (int i = 1; i <= numberOfElements; i++) {
            boardGameDescriptionList.add(
               new BoardGameDescription(i,"BoardGame Description No." + i, new BoardGame()));
        }
        return boardGameDescriptionList;
    }
}
