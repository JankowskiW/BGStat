package pl.wj.bgstat.user;

import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.exception.ResourceNotFoundException;
import pl.wj.bgstat.userboardgame.UserBoardGameRepository;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameHeader;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameHeaderDto;

import java.util.ArrayList;

import static pl.wj.bgstat.exception.ExceptionHelper.*;
import static pl.wj.bgstat.userboardgame.model.UserBoardGameMapper.mapToUserBoardGameHeaderDtoPage;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserBoardGameRepository userBoardGameRepository;

    public Page<UserBoardGameHeaderDto> getUserBoardGameHeaders(long id, Pageable pageable) {
        throwExceptionWhenNotExistsById(id, userRepository);
        return mapToUserBoardGameHeaderDtoPage(userBoardGameRepository.findUserBoardGameHeaders(id, pageable));
    }


    private void throwExceptionWhenNotExistsById(long id, JpaRepository repository) {
        if (!repository.existsById(id)) {
            String resourceName = "";
            if (repository instanceof  UserRepository) {
                resourceName = USER_RESOURCE_NAME;
            }
            throw new ResourceNotFoundException(resourceName, ID_FIELD, id);
        }
    }
}
