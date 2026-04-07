package fr.epita.assistants.yakamon.domain.service;

import fr.epita.assistants.yakamon.converter.MapConverter;
import fr.epita.assistants.yakamon.data.model.YakamonModel;
import fr.epita.assistants.yakamon.data.repository.GameRepository;
import fr.epita.assistants.yakamon.data.repository.PlayerRepository;
import fr.epita.assistants.yakamon.data.repository.YakamonRepository;
import fr.epita.assistants.yakamon.utils.ElementType;
import fr.epita.assistants.yakamon.utils.ErrorCode;
import fr.epita.assistants.yakamon.utils.tile.TerrainType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
@ApplicationScoped
public class ReleaseService {

    @Inject
    GameRepository gameRepository;

    @Inject
    PlayerRepository playerRepository;

    @Inject
    YakamonRepository yakamonRepository;

    @Inject
    MapConverter mapConverter;

    public void release(UUID uuid) {
        if (gameRepository.findAll().count() == 0)
            ErrorCode.BAD_REQUEST.throwException("Game is not running");

        var results = yakamonRepository.list("uuid", uuid);
        if (results.isEmpty())
            ErrorCode.NOT_FOUND.throwException("Yakamon not found");

        YakamonModel yakamon = results.getFirst();

        var player = playerRepository.findAll().firstResult();
        var map = mapConverter.mapConvert(gameRepository.findAll().firstResult().map);
        TerrainType terrain = map.get(player.posY).get(player.posX).getTerrainType();

        if (!terrain.isWalkable()) {
            List<YakamonModel> remaining = yakamonRepository.listAll()
                    .stream()
                    .filter(y -> !y.uuid.equals(uuid))
                    .toList();

            boolean canStillWalk = remaining.stream().anyMatch(y -> {
                ElementType first = y.yakadexEntry.firstType;
                ElementType second = y.yakadexEntry.secondType;
                return terrain.getCompatibleType().contains(first)
                        || (second != null && terrain.getCompatibleType().contains(second));
            });

            if (!canStillWalk)
                ErrorCode.FORBIDDEN.throwException("Cannot release the last yakamon able to walk on current tile");
        }

        yakamonRepository.delete(yakamon);
    }
}
