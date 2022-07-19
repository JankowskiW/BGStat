package pl.wj.bgstat.domain.systemobjecttype.enumeration;

import lombok.Getter;

@Getter
public enum ObjectType {
    BOARD_GAME(1),
    BOARD_GAME_DESCRIPTION(2),
    USER_BOARD_GAME(3),
    GAMEPLAY(4);

    long id;

    ObjectType(long id) { this.id = id; }


}
