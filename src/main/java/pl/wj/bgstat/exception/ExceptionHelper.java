package pl.wj.bgstat.exception;

public class ExceptionHelper {

    public static final String SYSTEM_OBJECT_ATTRIBUTE_CLASS_RESOURCE_NAME = "System object attribute class";
    public static final String SYSTEM_OBJECT_TYPE_RESOURCE_NAME = "System object type";
    public static final String ATTRIBUTE_CLASS_RESOURCE_NAME = "Attribute class";
    public static final String ATTRIBUTE_CLASS_TYPE_RESOURCE_NAME = "Attribute class type";
    public static final String BOARD_GAME_RESOURCE_NAME = "Board game";
    public static final String BOARD_GAME_DESCRIPTION_RESOURCE_NAME = "Board game description";
    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";

    public static String createResourceExistsExceptionMessage(String resource, String field) {
        return String.format("%s with specified %s already exists in database", resource, field);
    }

    public static String createResourceNotFoundExceptionMessage(String resource, String field, Object value) {
        return String.format("No such %s with %s: '%s'", resource, field, value);
    }
}
