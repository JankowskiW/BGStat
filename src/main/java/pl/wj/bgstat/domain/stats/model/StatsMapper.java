package pl.wj.bgstat.domain.stats.model;

import pl.wj.bgstat.domain.stats.model.dto.StatsBoardGameGameplaysDto;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class StatsMapper {

    public static List<StatsBoardGameGameplaysDto> mapToStatsBoardGameGameplaysDtoList(List<Object[]> objectsList) {
        return objectsList.stream()
                .map(objects -> mapToStatsBoardGameGameplaysDto(objects))
                .collect(Collectors.toList());
    }

    private static StatsBoardGameGameplaysDto mapToStatsBoardGameGameplaysDto(Object[] objects) {
        return StatsBoardGameGameplaysDto.builder()
                .boardGameId(((BigInteger) objects[0]).longValue())
                .boardGameName((String) objects[1])
                .numOfGameplays((Integer) objects[2])
                .avgTimeOfGameplay((Integer) objects[3])
                .build();
    }
}
