package pl.wj.bgstat.stats.model.dto;

public interface StatsBoardGameGameplaysDto {
    long getBoardGameId();
    String getBoardGameName();

    /**
     * Represents the number of gameplays of board game in given period.
     */
    int getNumOfGameplays();
    /**
     * Represents the gameplay average time in minutes of board game in given period
     */
    double getAvgTimeOfGameplay();
}

