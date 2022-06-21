package pl.wj.bgstat.gameplay.model;

import lombok.Getter;
import lombok.Setter;
import pl.wj.bgstat.boardgame.model.BoardGame;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="gameplays")
public class Gameplay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long objectTypeId;
    private long userId;
    private long boardGameId;
    private long userBoardGameId;
    private String comment;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int playtime;

}
