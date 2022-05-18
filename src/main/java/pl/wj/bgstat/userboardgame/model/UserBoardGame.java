package pl.wj.bgstat.userboardgame.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
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
    private long storeId;
    private boolean sleeved;
    private String comment;
    private LocalDate purchaseDate;
    private BigDecimal purchasePrice;
    private LocalDate saleDate;
    private BigDecimal salePrice;
}
