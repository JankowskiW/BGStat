package pl.wj.bgstat.gameplay.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

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
    private Date startTime;
    private Date endTime;
    private int playtime;
}
