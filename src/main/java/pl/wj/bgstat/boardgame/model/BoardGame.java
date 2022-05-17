package pl.wj.bgstat.boardgame.model;

import lombok.Getter;
import lombok.Setter;
import pl.wj.bgstat.attribute.model.Attribute;
import pl.wj.bgstat.boardgamedescription.model.BoardGameDescription;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name="board_games")
public class BoardGame implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long objectTypeId;
    private String name;
    private int recommendedAge;
    private int minPlayersNumber;
    private int maxPlayersNumber;
    private int complexity;
    private int estimatedPlaytime;

    @OneToOne(cascade = {CascadeType.REMOVE,
                         CascadeType.PERSIST,
                         CascadeType.MERGE},
              mappedBy = "boardGame")
    private BoardGameDescription boardGameDescription;

    @OneToMany(cascade = {CascadeType.REMOVE})
    @JoinColumns({
            @JoinColumn(updatable=false, insertable=false, name="objectId", referencedColumnName="id"),
            @JoinColumn(updatable=false, insertable=false, name="objectTypeId", referencedColumnName="objectTypeId")
    })
    private Set<Attribute> attributes = new HashSet<>();
}
