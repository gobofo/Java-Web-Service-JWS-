package fr.epita.assistants.yakamon.domain.service;

import fr.epita.assistants.yakamon.converter.MapConverter;
import fr.epita.assistants.yakamon.data.model.YakamonModel;
import fr.epita.assistants.yakamon.data.repository.GameRepository;
import fr.epita.assistants.yakamon.data.repository.PlayerRepository;
import fr.epita.assistants.yakamon.data.repository.YakadexEntryRepository;
import fr.epita.assistants.yakamon.data.repository.YakamonRepository;
import fr.epita.assistants.yakamon.presentation.rest.InventoryResource;
import fr.epita.assistants.yakamon.utils.ErrorCode;
import fr.epita.assistants.yakamon.utils.tile.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import fr.epita.assistants.yakamon.data.repository.ItemRepository;
import fr.epita.assistants.yakamon.data.model.ItemModel;
import fr.epita.assistants.yakamon.data.model.GameModel;

import java.util.List;

@Transactional
@ApplicationScoped
public class CatchService {

    @Inject
    MapConverter mapConverter;

    @Inject
    GameRepository gameRepository;

    @Inject
    PlayerRepository playerRepository;

    @Inject
    ItemService itemService;

    @Inject
    YakamonRepository yakamonRepository;
    @Inject
    YakadexEntryRepository yakadexEntryRepository;
    @Inject
    ItemRepository itemRepository;



    @ConfigProperty(name="JWS_TICK_DURATION") Integer JWS_TICK_DURATION;

    @ConfigProperty(name="JWS_CATCH_DELAY") Integer JWS_CATCH_DELAY;

    public YakamonModel catchYakamon(){

        if(gameRepository.findAll().count()==0)
            ErrorCode.BAD_REQUEST.throwException("Game is not running");

        if(playerRepository.findAll().firstResult().lastCatch != null)
        {
            java.time.LocalDateTime next_catch = playerRepository.findAll().firstResult().lastCatch.plus(JWS_TICK_DURATION * JWS_CATCH_DELAY, java.time.temporal.ChronoUnit.MILLIS);
            if(java.time.LocalDateTime.now().isBefore(next_catch))
                ErrorCode.BAD_REQUEST.throwException("Catch is on cooldown");
        }

        var items = itemService.getInventory();
        if(items.stream().filter( item -> item.getItemType().equals(ItemType.YAKABALL)).findFirst().get().getQuantity() == 0)
            ErrorCode.BAD_REQUEST.throwException("No Yakaball in inventory");

        var map = mapConverter.mapConvert(gameRepository.findAll().firstResult().map);

        int playerX = playerRepository.findAll().firstResult().posX;
        int playerY = playerRepository.findAll().firstResult().posY;

        if(map.get(playerY).get(playerX).getCollectible() == null
                || map.get(playerY).get(playerX).getCollectible().getCollectibleType() != CollectibleType.YAKAMON)
            ErrorCode.BAD_REQUEST.throwException("No Yakamon on player's position");

        if(yakamonRepository.listAll().size() == 3)
            ErrorCode.BAD_REQUEST.throwException("Team is full");


        YakamonType yakamonType = (YakamonType) map.get(playerY).get(playerX).getCollectible();
        Integer yakadexId = ((YakamonInfo) yakamonType.getCollectibleInfo()).getYakadexId();

        var yakadexEntry = yakadexEntryRepository.list("id", yakadexId).getFirst();

        yakadexEntry.caught = true;

        var yakamon = new YakamonModel();
        yakamon.nickname = yakadexEntry.name;
        yakamon.yakadexEntry = yakadexEntry;
        yakamon.energyPoints = 0;
        yakamonRepository.persist(yakamon);

        ItemModel yakaball = itemRepository.list("type", ItemType.YAKABALL).getFirst();
        yakaball.setQuantity(yakaball.getQuantity() - 1);
        itemRepository.persist(yakaball);

        String currentMap = gameRepository.findAll().firstResult().map;
        List<List<TileType>> lmap = mapConverter.mapConvert(currentMap);
        TileType tile = lmap.get(playerY).get(playerX);
        tile.setCollectible(null);
        String newMap = mapConverter.mapConvert(lmap);
        GameModel game = gameRepository.findAll().firstResult();
        game.setMap(newMap);
        gameRepository.persist(game);

        playerRepository.findAll().firstResult().lastCatch = java.time.LocalDateTime.now();

        return yakamonRepository.listAll().getLast();
    }
}
