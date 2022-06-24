package pl.wj.bgstat.exception;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import pl.wj.bgstat.attribute.AttributeRepository;
import pl.wj.bgstat.attributeclass.AttributeClassRepository;
import pl.wj.bgstat.attributeclasstype.AttributeClassTypeRepository;
import pl.wj.bgstat.boardgame.BoardGameRepository;
import pl.wj.bgstat.boardgamedescription.BoardGameDescriptionRepository;
import pl.wj.bgstat.gameplay.GameplayRepository;
import pl.wj.bgstat.rulebook.RulebookRepository;
import pl.wj.bgstat.store.StoreRepository;
import pl.wj.bgstat.systemobjectattributeclass.SystemObjectAttributeClassRepository;
import pl.wj.bgstat.systemobjecttype.SystemObjectTypeRepository;
import pl.wj.bgstat.user.UserRepository;
import pl.wj.bgstat.userboardgame.UserBoardGameRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExceptionHelper {

    public static final List<MediaType> SUPPORTED_THUMBNAIL_MEDIA_TYPES = List.of(MediaType.IMAGE_JPEG, MediaType.IMAGE_PNG);
    public static final List<MediaType> SUPPORTED_RULEBOOK_MEDIA_TYPES = List.of(MediaType.APPLICATION_PDF);

    public static final String SYSTEM_OBJECT_ATTRIBUTE_CLASS_RESOURCE_NAME = "System object attribute class";
    public static final String SYSTEM_OBJECT_TYPE_RESOURCE_NAME = "System object type";
    public static final String ATTRIBUTE_CLASS_RESOURCE_NAME = "Attribute class";
    public static final String ATTRIBUTE_RESOURCE_NAME = "Attribute";
    public static final String ATTRIBUTE_CLASS_TYPE_RESOURCE_NAME = "Attribute class type";
    public static final String BOARD_GAME_RESOURCE_NAME = "Board game";
    public static final String BOARD_GAME_DESCRIPTION_RESOURCE_NAME = "Board game description";
    public static final String USER_BOARD_GAME_RESOURCE_NAME = "User board game";
    public static final String USER_RESOURCE_NAME = "User";
    public static final String STORE_RESOURCE_NAME = "Store";
    public static final String GAMEPLAY_RESOURCE_NAME = "Gameplay";
    public static final String RULEBOOK_RESOURCE_NAME = "Rulebook";

    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";

    public static String createForeignKeyConstraintViolationExceptionMessage(String resource, long id) {
        return String.format("%s with id %d does not exist in database", resource, id);
    }

    public static String createRequestEnumExceptionMessage(String fieldName, List<String> supportedValues) {
        return String.format("Unsupported %s value. Supported values: %s", fieldName, supportedValues.toString());
    }

    public static String createRequestFileExceptionSaveFailedMessage(String fileName) {
        return String.format("Failed to save file '%s'", fileName);
    }

    public static String createRequestFileExceptionIncorrectSizeMessage(String fileTypeName, int minHeight, int maxHeight, int minWidth, int maxWidth, int maxSize) {
        return String.format("%s height should be between %d and %d px, width should be between %d and %d px and size should be less than %d kB",
                fileTypeName, minHeight, maxHeight, minWidth, maxWidth, maxSize);
    }

    public static String createRequestFileExceptionMessage(String fileTypeName, int minHeight, int maxHeight, int minWidth, int maxWidth, int maxSize) {
        return String.format("%s height should be between %d and %d px, width should be between %d and %d px and size should be less than %d kB",
                fileTypeName, minHeight, maxHeight, minWidth, maxWidth, maxSize);
    }

    public static String createResourceExistsExceptionMessage(String resource, Optional<String> field) {
        if (field.isPresent()) {
            return String.format("%s with specified %s already exists in database", resource, field.get());
        } else {
            return String.format("%s already exists in database", resource);
        }
    }

    public static String createResourceExistsExceptionMessage(String resource, String fkResource) {
        return String.format("%s has related %s records", resource, fkResource);
    }

    public static String createResourceExistsExceptionMessage(String resource) {
        return String.format("%s already exists in database", resource);
    }

    public static String createResourceNotFoundExceptionMessage(String resource, String field, Object value) {
        return String.format("No such %s with %s: '%s'", resource, field, value);
    }

    public static void throwExceptionWhenForeignKeyConstraintViolationOccur(long id, JpaRepository repository) {
        if (repository == null) return;
        if (!repository.existsById(id)) {
            String resourceName = getResourceName(repository);
            throw new ForeignKeyConstraintViolationException(resourceName, id);
        }
    }

    public static void throwExceptionWhenNotExistsById(long id, JpaRepository repository) {
        if (repository == null) return;
        if (!repository.existsById(id)) {
            String resourceName = getResourceName(repository);
            throw new ResourceNotFoundException(resourceName, ID_FIELD, id);
        }
    }

    private static String getResourceName(JpaRepository repository) {
        if (repository instanceof AttributeRepository) {
            return ATTRIBUTE_RESOURCE_NAME;
        } else if (repository instanceof AttributeClassRepository) {
            return ATTRIBUTE_CLASS_RESOURCE_NAME;
        } else if (repository instanceof AttributeClassTypeRepository) {
            return ATTRIBUTE_CLASS_TYPE_RESOURCE_NAME;
        }else if (repository instanceof BoardGameRepository) {
            return BOARD_GAME_RESOURCE_NAME;
        } else if (repository instanceof BoardGameDescriptionRepository) {
            return BOARD_GAME_DESCRIPTION_RESOURCE_NAME;
        } else if (repository instanceof GameplayRepository) {
            return GAMEPLAY_RESOURCE_NAME;
        } else if (repository instanceof StoreRepository) {
            return STORE_RESOURCE_NAME;
        } else if (repository instanceof SystemObjectAttributeClassRepository) {
            return SYSTEM_OBJECT_ATTRIBUTE_CLASS_RESOURCE_NAME;
        } else if (repository instanceof SystemObjectTypeRepository) {
            return SYSTEM_OBJECT_TYPE_RESOURCE_NAME;
        } else if (repository instanceof UserBoardGameRepository) {
            return USER_BOARD_GAME_RESOURCE_NAME;
        }else if (repository instanceof UserRepository) {
            return USER_RESOURCE_NAME;
        }else if (repository instanceof RulebookRepository) {
            return RULEBOOK_RESOURCE_NAME;
        } else {
            return "";
        }
    }

}
