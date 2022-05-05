package pl.wj.bgstat.userboardgame.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name="user_board_games")
public class UserBoardGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long objectTypeId;
    private long boardGameId;
    private long userId;
    private long shopId;
    private boolean sleeved;
    private String comment;
    private Date purchaseDate;
    private double purchasePrice;
    private Date saleDate;
    private double salePrice;
}
