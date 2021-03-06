package pl.wj.bgstat.domain.gameplay.model;

import pl.wj.bgstat.domain.gameplay.model.dto.GameplayRequestDto;
import pl.wj.bgstat.domain.gameplay.model.dto.GameplayResponseDto;

public class GameplayMapper {

    public static Gameplay mapToGameplay(long id, GameplayRequestDto gameplayRequestDto) {
        Gameplay gameplay = mapToGameplay(gameplayRequestDto);
        gameplay.setId(id);
        return gameplay;
    }

    public static Gameplay mapToGameplay(GameplayRequestDto gameplayRequestDto) {
        Gameplay gameplay = new Gameplay();
        gameplay.setObjectTypeId(gameplayRequestDto.getObjectTypeId());
        gameplay.setUserId(gameplayRequestDto.getUserId());
        gameplay.setBoardGameId(gameplayRequestDto.getBoardGameId());
//        gameplay.setBoardGame(new BoardGame());
//        gameplay.getBoardGame().setId(gameplayRequestDto.getBoardGameId());
        gameplay.setUserBoardGameId(gameplayRequestDto.getUserBoardGameId());
        gameplay.setComment(gameplayRequestDto.getComment());
        gameplay.setStartTime(gameplayRequestDto.getStartTime());
        gameplay.setEndTime(gameplayRequestDto.getEndTime());
        gameplay.setPlaytime(gameplayRequestDto.getPlaytime());
        return gameplay;
    }

    public static GameplayResponseDto mapToGameplayResponseDto(Gameplay gameplay) {
        return GameplayResponseDto.builder()
                .id(gameplay.getId())
                .objectTypeId(gameplay.getObjectTypeId())
                .userId(gameplay.getUserId())
                .boardGameId(gameplay.getBoardGameId())
//                .boardGameId(gameplay.getBoardGame().getId())
                .userBoardGameId(gameplay.getUserBoardGameId())
                .comment(gameplay.getComment())
                .startTime(gameplay.getStartTime())
                .endTime(gameplay.getEndTime())
                .playtime(gameplay.getPlaytime())
                .build();
    }
}
