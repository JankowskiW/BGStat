package pl.wj.bgstat.exception;

public class ExceptionHelper {

    public static final String DATABASE_ACCESS_ER_MSG = "Database access error";

    // Attribute Class Module
    public static final String ATTRIBUTE_CLASS_NOT_FOUND_EX_MSG = "No such attribute class with id: ";
    public static final String ATTRIBUTE_CLASS_EXISTS_EX_MSG = "Attribute class with given name already exists in database";

    // Attribute Class Type Module
    public static final String ATTRIBUTE_CLASS_TYPE_NOT_FOUND_EX_MSG = "No such attribute class type with id: ";
    public static final String ATTRIBUTE_CLASS_TYPE_EXISTS_EX_MSG = "Attribute class type with given name already exists in database";

    // Board Game Module
    public static final String BOARD_GAME_NOT_FOUND_EX_MSG = "No such board game type with id: ";
    public static final String BOARD_GAME_TYPE_EXISTS_EX_MSG = "Board game type with given name already exists in database";

    // Board Game Description Module
    public static final String BOARD_GAME_DESCRIPTION_NOT_FOUND_EX_MSG = "No such board game description type with id: ";

    // System Object Type Module
    public static final String SYSTEM_OBJECT_TYPE_NOT_FOUND_EX_MSG = "No such system object type with id: ";
    public static final String SYSTEM_OBJECT_TYPE_EXISTS_EX_MSG = "System object type with given name already exists in database";

    // System Object Attribute Class Module
    public static final String SYSTEM_OBJECT_ATTRIBUTE_CLASS_EXISTS_EX_MSG = "Attribute class to system object type assignment already exists in database";
    public static final String SYSTEM_OBJECT_ATTRIBUTE_CLASS_NOT_FOUND_EX_MSG = "No such attribute class to system object type assignment";
}
