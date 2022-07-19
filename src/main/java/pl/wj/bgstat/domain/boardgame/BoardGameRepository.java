package pl.wj.bgstat.domain.boardgame;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.wj.bgstat.domain.boardgame.model.BoardGame;
import pl.wj.bgstat.domain.boardgame.model.dto.BoardGameHeaderDto;
import pl.wj.bgstat.domain.boardgame.model.dto.BoardGameThumbnailResponseDto;

import java.util.Optional;

@Repository
public interface BoardGameRepository extends JpaRepository<BoardGame, Long> {

    @Query("SELECT new pl.wj.bgstat.domain.boardgame.model.dto.BoardGameHeaderDto(bg.id, bg.name) FROM BoardGame bg")
    Page<BoardGameHeaderDto> findBoardGameHeaders(Pageable pageable);

    @Query("SELECT bg FROM BoardGame bg LEFT JOIN FETCH bg.boardGameDescription bgd WHERE bg.id = :id")
    Optional<BoardGame> findWithDescriptionById(long id);

    boolean existsByNameAndIdNot(String name, long id);
    boolean existsByName(String name);

    @Query("SELECT new pl.wj.bgstat.domain.boardgame.model.dto.BoardGameThumbnailResponseDto(bg.id, bg.thumbnailPath) " +
            "FROM BoardGame bg WHERE bg.id = :id")
    BoardGameThumbnailResponseDto findThumbnailPath(long id);

    @Modifying
    @Query("UPDATE BoardGame bg SET bg.thumbnailPath = :thumbnailPath WHERE bg.id = :boardGameId")
    void updateThumbnailPathById(long boardGameId, String thumbnailPath);
}
