package pl.wj.bgstat.userboardgame.model;

import lombok.Getter;
import lombok.Setter;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameDetailsDto;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@NamedNativeQuery(name = "UserBoardGame.getWithDetailsById",
                  query = "SELECT " +
                    "        ubg.id AS id, ubg.board_game_id AS boardGameId, ubg.object_type_id AS objectTypeId," +
                    "        ubg.sleeved AS sleeved, ubg.comment AS comment, ubg.purchase_date AS purchaseDate, " +
                    "        ubg.purchase_price AS purchasePrice, ubg.sale_date AS saleDate, ubg.sale_price AS salePrice," +
                    "        bg.name AS bgName, bg.recommended_age AS bgRecommendedAge, " +
                    "        bg.min_players_number AS bgMinPlayersNumber, bg.max_players_number AS bgMaxPlayersNumber," +
                    "        bg.complexity as bgComplexity, bg.playing_time AS bgPlayingTime, bgd.description AS bgDescription " +
                    "FROM user_board_games ubg " +
                    "LEFT JOIN board_games bg ON ubg.board_game_id = bg.id " +
                    "LEFT JOIN board_game_descriptions bgd ON bgd.board_game_id = bg.id " +
                    "WHERE ubg.id = :id",
                    resultSetMapping = "Mapping.UserBoardGameDetailsDto")
@SqlResultSetMapping(name="Mapping.UserBoardGameDetailsDto",
                     classes = @ConstructorResult(targetClass = UserBoardGameDetailsDto.class,
                                             columns = {@ColumnResult(name = "id", type=Long.class),
                                                     @ColumnResult(name = "boardGameId", type=Long.class),
                                                     @ColumnResult(name = "objectTypeId", type=Long.class),
                                                     @ColumnResult(name = "sleeved", type=Boolean.class),
                                                     @ColumnResult(name = "comment", type=String.class),
                                                     @ColumnResult(name = "purchaseDate", type=Date.class),
                                                     @ColumnResult(name = "purchasePrice", type=BigDecimal.class),
                                                     @ColumnResult(name = "saleDate", type=Date.class),
                                                     @ColumnResult(name = "salePrice", type=BigDecimal.class),
                                                     @ColumnResult(name = "bgName", type=String.class),
                                                     @ColumnResult(name = "bgRecommendedAge", type=Integer.class),
                                                     @ColumnResult(name = "bgMinPlayersNumber", type=Integer.class),
                                                     @ColumnResult(name = "bgMaxPlayersNumber", type=Integer.class),
                                                     @ColumnResult(name = "bgComplexity", type=Integer.class),
                                                     @ColumnResult(name = "bgPlayingTime", type=Integer.class),
                                                     @ColumnResult(name = "bgDescription", type=String.class)}))

@Entity
@Getter
@Setter
@Table(name="user_board_games", schema="dbo")
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
    private Date purchaseDate;
    private BigDecimal purchasePrice;
    private Date saleDate;
    private BigDecimal salePrice;
}
