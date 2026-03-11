package fr.epita.assistants.yakamon.domain.service;

import fr.epita.assistants.yakamon.converter.MapConverter;
import fr.epita.assistants.yakamon.data.model.GameModel;
import fr.epita.assistants.yakamon.data.model.ItemModel;
import fr.epita.assistants.yakamon.data.repository.GameRepository;
import fr.epita.assistants.yakamon.data.repository.ItemRepository;
import fr.epita.assistants.yakamon.data.repository.PlayerRepository;
import fr.epita.assistants.yakamon.utils.ErrorCode;
import fr.epita.assistants.yakamon.utils.tile.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;

@Transactional
@ApplicationScoped
public class CollectService {

    @Inject
    GameRepository gameRepository;

    @Inject
    PlayerRepository playerRepository;

    @Inject
    ItemRepository itemRepository;

    @Inject
    MapConverter mapConverter;

    @ConfigProperty(name = "JWS_TICK_DURATION")
    Integer JWS_TICK_DURATION;

    @ConfigProperty(name = "JWS_COLLECT_DELAY")
    Integer JWS_COLLECT_DELAY;

    @ConfigProperty(name = "JWS_COLLECT_MULTIPLIER")
    Integer JWS_COLLECT_MULTIPLIER;

    public TileType collect() {
        if (gameRepository.findAll().count() == 0)
            ErrorCode.BAD_REQUEST.throwException("Game is not running");

        if (playerRepository.findAll().firstResult().lastCollect != null) {
            java.time.LocalDateTime nextCollect = playerRepository.findAll().firstResult().lastCollect.plus(JWS_TICK_DURATION * JWS_COLLECT_DELAY, java.time.temporal.ChronoUnit.MILLIS);
            if (java.time.LocalDateTime.now().isBefore(nextCollect))
                ErrorCode.BAD_REQUEST.throwException("Collect is on cooldown");
        }

        var map = mapConverter.mapConvert(gameRepository.findAll().firstResult().map);

        int playerX = playerRepository.findAll().firstResult().posX;
        int playerY = playerRepository.findAll().firstResult().posY;

        if (map.get(playerY).get(playerX).getCollectible() == null || map.get(playerY).get(playerX).getCollectible().getCollectibleType() != CollectibleType.ITEM)
            ErrorCode.BAD_REQUEST.throwException("No item on player's position");

        ItemType itemType = (ItemType) map.get(playerY).get(playerX).getCollectible();
        if (itemType.getValue().equals("NONE"))
            ErrorCode.BAD_REQUEST.throwException("Collectible is not an item");
        var items = itemRepository.list("type", itemType);
        ItemModel item;
        if (items.isEmpty()) {
            item = new ItemModel();
            item.setType(itemType);
            item.setQuantity(0);
            itemRepository.persist(item);
        } else {
            item = items.getFirst();
        }
        item.setQuantity(item.getQuantity() + JWS_COLLECT_MULTIPLIER);
        itemRepository.persist(item);

        String currentMap = gameRepository.findAll().firstResult().map;
        List<List<TileType>> lmap = mapConverter.mapConvert(currentMap);
        TileType tile = lmap.get(playerY).get(playerX);
        tile.setCollectible(ItemType.NONE);
        String newMap = mapConverter.mapConvert(lmap);
        GameModel game = gameRepository.findAll().firstResult();
        game.setMap(newMap);
        gameRepository.persist(game);

        playerRepository.findAll().firstResult().lastCollect = java.time.LocalDateTime.now();

        return tile;
    }
}
