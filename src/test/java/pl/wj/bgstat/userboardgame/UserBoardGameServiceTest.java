package pl.wj.bgstat.userboardgame;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class  UserBoardGameServiceTest {

    @Mock
    private UserBoardGameRepository userBoardGameRepository;
    @InjectMocks
    private UserBoardGameService userBoardGameService;

}