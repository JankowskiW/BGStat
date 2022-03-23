package pl.wj.bgstat.boardgamedescription.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private BoardGame boardGame;
}
