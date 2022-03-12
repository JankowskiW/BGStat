package pl.wj.bgstat.boardgamedescription;


import lombok.Getter;
import lombok.Setter;
import pl.wj.bgstat.boardgame.model.BoardGame;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="board_game_descriptions")
public class BoardGameDescription {
    @Id
    private long boardGameId;
    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToOne
    @MapsId
    private BoardGame boardGame;
}
