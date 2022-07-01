package pl.wj.bgstat.userboardgame.model;

import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameRequestDto;
import pl.wj.bgstat.userboardgame.model.dto.UserBoardGameResponseDto;

public class UserBoardGameMapper {
    public static UserBoardGame mapToUserBoardGame(long id, UserBoardGameRequestDto userBoardGameRequestDto) {
        UserBoardGame userBoardGame = mapToUserBoardGame(userBoardGameRequestDto);
        userBoardGame.setId(id);
        return userBoardGame;
    }

    public static UserBoardGame mapToUserBoardGame(UserBoardGameRequestDto userBoardGameRequestDto) {
        UserBoardGame userBoardGame = new UserBoardGame();
        userBoardGame.setObjectTypeId(userBoardGameRequestDto.getObjectTypeId());
        userBoardGame.setBoardGameId(userBoardGameRequestDto.getBoardGameId());
        userBoardGame.setUserId(userBoardGameRequestDto.getUserId());
        userBoardGame.setStoreId(userBoardGameRequestDto.getStoreId());
        userBoardGame.setSleeved(userBoardGameRequestDto.isSleeved());
        userBoardGame.setComment(userBoardGameRequestDto.getComment());
        userBoardGame.setPurchaseDate(userBoardGameRequestDto.getPurchaseDate());
        userBoardGame.setPurchasePrice(userBoardGameRequestDto.getPurchasePrice());
        userBoardGame.setSaleDate(userBoardGameRequestDto.getSaleDate());
        userBoardGame.setSalePrice(userBoardGameRequestDto.getSalePrice());
        return userBoardGame;
    }

    public static UserBoardGameResponseDto mapToUserBoardGameResponseDto(UserBoardGame userBoardGame) {
        return UserBoardGameResponseDto.builder()
                .id(userBoardGame.getId())
                .objectTypeId(userBoardGame.getObjectTypeId())
                .boardGameId(userBoardGame.getBoardGameId())
                .userId(userBoardGame.getUserId())
                .storeId(userBoardGame.getStoreId())
                .sleeved(userBoardGame.isSleeved())
                .comment(userBoardGame.getComment())
                .purchaseDate(userBoardGame.getPurchaseDate())
                .purchasePrice(userBoardGame.getPurchasePrice())
                .saleDate(userBoardGame.getSaleDate())
                .salePrice(userBoardGame.getSalePrice())
                .build();
    }

}
