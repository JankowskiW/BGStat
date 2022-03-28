package pl.wj.bgstat.boardgame;

import pl.wj.bgstat.boardgame.model.BoardGame;
import pl.wj.bgstat.boardgame.model.dto.BoardGameHeaderDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGameRequestDto;
import pl.wj.bgstat.boardgamedescription.model.BoardGameDescription;

import java.util.ArrayList;
import java.util.List;

public class BoardGameServiceTestHelper {


    private static final int RECOMMENDED_AGE = 14;
    private static final int MIN_PLAYERS_NUMBER = 1;
    private static final int MAX_PLAYERS_NUMBER = 4;
    private static final int COMPLEXITY = 2;
    private static final int PLAYING_TIME = 120;


    public static List<BoardGame> populateBoardGameList(int numberOfElements) {
        List<BoardGame> boardGameList = new ArrayList<>();
        BoardGame boardGame;
        BoardGameDescription boardGameDescription;
        for (int i = 1; i <= numberOfElements; i++) {
            boardGame = new BoardGame();
            boardGame.setId(i);
            boardGame.setName("Name No. " + i);
            boardGame.setRecommendedAge(RECOMMENDED_AGE);
            boardGame.setMinPlayersNumber(MIN_PLAYERS_NUMBER);
            boardGame.setMaxPlayersNumber(MAX_PLAYERS_NUMBER);
            boardGame.setComplexity(COMPLEXITY);
            boardGame.setPlayingTime(PLAYING_TIME);
            boardGameDescription = new BoardGameDescription();
            boardGameDescription.setBoardGameId(i);
            boardGameDescription.setBoardGame(boardGame);
            boardGameDescription.setDescription("DESCRIPTION OF " + boardGame.getName());
            boardGame.setBoardGameDescription(boardGameDescription);
            boardGameList.add(boardGame);
        }

        return boardGameList;
    }

    public static List<BoardGameHeaderDto> populateBoardGameHeaderDtoList(List<BoardGame> boardGameList) {
        List<BoardGameHeaderDto> boardGameHeaderList = new ArrayList<>();
        for (int i = 0; i < boardGameList.size(); i++)
            boardGameHeaderList.add(new BoardGameHeaderDto(
                    boardGameList.get(i).getId(), boardGameList.get(i).getName()));
        return boardGameHeaderList;
    }

    public static BoardGameRequestDto createBoardGameRequestDto(int currentSize) {
        return new BoardGameRequestDto(
                "Name No. " + (currentSize + 1),
                18,
                1,
                4,
                5,
                150,
                "DESCRIPTION OF Name No. " + (currentSize + 1));
    }
}
