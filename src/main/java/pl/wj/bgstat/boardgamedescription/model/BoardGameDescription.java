package pl.wj.bgstat.boardgamedescription.model;

import lombok.*;
import pl.wj.bgstat.boardgame.model.BoardGame;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="board_game_descriptions")
public class BoardGameDescription {
    @Id
    private long boardGameId;
    private long objectTypeId;
    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private BoardGame boardGame;
}
