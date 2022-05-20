package pl.wj.bgstat.boardgame.model.dto;

public interface BoardGameGameplayStatsDto {
    long getBoardGameId();
    String getBoardGameName();

    /**
     * Represents the number of gameplays of board game in given period.
     */
    int getNumOfGameplays();
    /**
     * Represents the gameplay average time in minutes of board game in given period
     */
    int getAvgTimeOfGameplay();
}
