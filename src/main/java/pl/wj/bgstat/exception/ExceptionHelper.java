package pl.wj.bgstat.exception;

public class ExceptionHelper {

    public static final String SYSTEM_OBJECT_ATTRIBUTE_CLASS_RESOURCE_NAME = "System object attribute class";
    public static final String SYSTEM_OBJECT_TYPE_RESOURCE_NAME = "System object type";
    public static final String ATTRIBUTE_CLASS_RESOURCE_NAME = "Attribute class";
    public static final String ATTRIBUTE_RESOURCE_NAME = "Attribute";
    public static final String ATTRIBUTE_CLASS_TYPE_RESOURCE_NAME = "Attribute class type";
    public static final String BOARD_GAME_RESOURCE_NAME = "Board game";
    public static final String BOARD_GAME_DESCRIPTION_RESOURCE_NAME = "Board game description";
    public static final String USER_BOARD_GAME_RESOURCE_NAME = "User board game";
    public static final String USER_RESOURCE_NAME = "User";
    public static final String SHOP_RESOURCE_NAME = "Shop";

    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";

    public static String createResourceExistsExceptionMessage(String resource, String field) {
        return String.format("%s with specified %s already exists in database", resource, field);
    }

    public static String createResourceExistsExceptionMessage(String resource) {
        return String.format("%s already exists in database", resource);
    }

    public static String createResourceNotFoundExceptionMessage(String resource, String field, Object value) {
        return String.format("No such %s with %s: '%s'", resource, field, value);
    }

}
