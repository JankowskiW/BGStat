package pl.wj.bgstat.userboardgame.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.wj.bgstat.userboardgame.model.dto.*;

import java.util.List;
import java.util.stream.Collectors;

public class UserBoardGameMapper {
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

    public static Page<UserBoardGameHeaderDto> mapToUserBoardGameHeaderDtoPage(Page<UserBoardGameHeader> userBoardGameHeaderPage) {
        List<UserBoardGameHeaderDto> userBoardGameDetailsDtoList =
                userBoardGameHeaderPage.stream()
                    .map(userBoardGameHeader -> mapToUserBoardGameHeaderDto(userBoardGameHeader))
                    .collect(Collectors.toList());
        return new PageImpl<>(userBoardGameDetailsDtoList, userBoardGameHeaderPage.getPageable(),
                userBoardGameHeaderPage.getTotalElements());
    }

    public static UserBoardGameHeaderDto mapToUserBoardGameHeaderDto(UserBoardGameHeader userBoardGameHeader) {
        return UserBoardGameHeaderDto.builder()
                .id(userBoardGameHeader.getId())
                .bgName(userBoardGameHeader.getBgName())
                .build();
    }

    public static UserBoardGameDetailsDto mapToUserBoardGameDetailsDto(UserBoardGameDetails userBoardGameDetails) {
        return UserBoardGameDetailsDto.builder()
                .id(userBoardGameDetails.getId())
                .boardGameId(userBoardGameDetails.getBoardGameId())
                .objectTypeId(userBoardGameDetails.getObjectTypeId())
                .sleeved(userBoardGameDetails.isSleeved())
                .comment(userBoardGameDetails.getComment())
                .purchaseDate(userBoardGameDetails.getPurchaseDate())
                .purchasePrice(userBoardGameDetails.getPurchasePrice())
                .saleDate(userBoardGameDetails.getSaleDate())
                .salePrice(userBoardGameDetails.getSalePrice())
                .bgName(userBoardGameDetails.getBgName())
                .bgRecommendedAge(userBoardGameDetails.getBgRecommendedAge())
                .bgMinPlayersNumber(userBoardGameDetails.getBgMinPlayersNumber())
                .bgMaxPlayersNumber(userBoardGameDetails.getBgMaxPlayersNumber())
                .bgComplexity(userBoardGameDetails.getBgComplexity())
                .bgEstimatedPlaytime(userBoardGameDetails.getBgEstimatedPlaytime())
                .bgDescription(userBoardGameDetails.getBgDescription())
                .build();
    }

}
