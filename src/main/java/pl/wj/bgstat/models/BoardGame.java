package pl.wj.bgstat.models;

import lombok.Getter;
import lombok.Setter;

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

    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "boardGame")
    private BoardGameDescription boardGameDescription;
}
