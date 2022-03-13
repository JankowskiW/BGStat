package pl.wj.bgstat.boardgame.model;

import lombok.Getter;
import lombok.Setter;
import pl.wj.bgstat.boardgamedescription.BoardGameDescription;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="board_games")
public class BoardGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private int recommendedAge;
    private int minPlayersNumber;
    private int maxPlayersNumber;
    private int complexity;
    private int playingTime;

    @OneToOne(cascade = {CascadeType.REMOVE,
                         CascadeType.PERSIST,
                         CascadeType.MERGE},
              mappedBy = "boardGame")
    private BoardGameDescription boardGameDescription;
}
