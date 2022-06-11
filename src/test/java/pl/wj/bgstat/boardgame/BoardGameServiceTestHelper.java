package pl.wj.bgstat.boardgame;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pl.wj.bgstat.boardgame.model.BoardGame;
import pl.wj.bgstat.boardgame.model.dto.BoardGameGameplaysStatsDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGameHeaderDto;
import pl.wj.bgstat.boardgame.model.dto.BoardGameRequestDto;
import pl.wj.bgstat.boardgamedescription.model.BoardGameDescription;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BoardGameServiceTestHelper {

    private static final int RECOMMENDED_AGE = 14;
    private static final int MIN_PLAYERS_NUMBER = 1;
    private static final int MAX_PLAYERS_NUMBER = 4;
    private static final int COMPLEXITY = 2;
    private static final int ESTIMATED_PLAYTIME = 120;

    private static final String NOT_OK_FILE_NAME = "notOkFile.png";
    private static final String OK_FILE_NAME = "okFile.png";
    private static final String NOT_OK_FILE_PATH = "src/test/resources/" + NOT_OK_FILE_NAME;
    private static final String OK_FILE_PATH = "src/test/resources/" + OK_FILE_NAME;


    public static MultipartFile createMultipartFile(String mediaType, boolean correctResolution) {
        // TODO: 11.06.2022 Try to mock everywhere mutlipart file with input stream instead of create actual multipart file 
        MultipartFile mpf = null;
        try {
            if (correctResolution) {
                File file = new File(OK_FILE_PATH);
                InputStream is = new FileInputStream(file);
                mpf = new MockMultipartFile(OK_FILE_NAME, OK_FILE_NAME, mediaType, is);
            } else {
                File file = new File(NOT_OK_FILE_PATH);
                InputStream is = new FileInputStream(file);
                mpf = new MockMultipartFile(NOT_OK_FILE_NAME, NOT_OK_FILE_NAME, mediaType, is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
               return mpf;
        }
    }

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
            boardGame.setEstimatedPlaytime(ESTIMATED_PLAYTIME);
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
        for (BoardGame boardGame : boardGameList) {
            boardGameHeaderList.add(new BoardGameHeaderDto(
                    boardGame.getId(), boardGame.getName()));
        }
        return boardGameHeaderList;
    }

    public static BoardGameRequestDto createBoardGameRequestDto(int currentSize, long objectTypeId) {
        return new BoardGameRequestDto(
                "Name No. " + (currentSize + 1),
                objectTypeId,
                18,
                1,
                4,
                5,
                150,
                "DESCRIPTION OF Name No. " + (currentSize + 1));
    }

    public static BoardGameGameplaysStatsDto createBoardGameGameplaysStatsDto(boolean isEmpty) {
        int numOfGp;
        double avgTime;
        if (isEmpty) {
            numOfGp = 0;
            avgTime = 0;
        }
        else {
            numOfGp = 37;
            avgTime = 99;
        }
        return new BoardGameGameplaysStatsDto() {
            @Override
            public long getBoardGameId() {
                return 1;
            }

            @Override
            public String getBoardGameName() {
                return "Name";
            }

            @Override
            public int getNumOfGameplays() {
                return numOfGp;
            }

            @Override
            public double getAvgTimeOfGameplay() {
                return avgTime;
            }
        };
    }
}
