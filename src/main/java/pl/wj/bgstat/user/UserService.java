package pl.wj.bgstat.user;

import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.wj.bgstat.boardgame.BoardGameRepository;
import pl.wj.bgstat.boardgame.model.dto.BoardGameGameplaysStatsDto;
import pl.wj.bgstat.exception.ResourceNotFoundException;
import pl.wj.bgstat.gameplay.GameplayRepository;
import pl.wj.bgstat.gameplay.GameplayService;
import pl.wj.bgstat.gameplay.model.dto.GameplayHeaderDto;
import pl.wj.bgstat.gameplay.model.dto.GameplaysStatsDto;
import pl.wj.bgstat.user.model.User;
import pl.wj.bgstat.userboardgame.UserBoardGameRepository;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameHeaderDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static pl.wj.bgstat.exception.ExceptionHelper.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserBoardGameRepository userBoardGameRepository;
    private final GameplayRepository gameplayRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found in the database"));
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }


    // TODO: 12.07.2022 Change User in parameter to UserRequestDto and User in return value to UserResponseDto 
    public User addUser(User user) {
        // TODO: 12.07.2022 Check if user with given username already exists and if it is then throw an exception
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Page<UserBoardGameHeaderDto> getUserBoardGameHeaders(long id, Pageable pageable) {
        throwExceptionWhenNotExistsById(id, userRepository);
        return userBoardGameRepository.findUserBoardGameHeaders(id, pageable);
    }

    public Page<GameplayHeaderDto> getUserGameplayHeaders(long id, Pageable pageable) {
        throwExceptionWhenNotExistsById(id, userRepository);
        return gameplayRepository.findUserGameplayHeaders(id, pageable);
    }
}
