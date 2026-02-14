package fr.epita.assistants.yakamon.domain.service;

import fr.epita.assistants.yakamon.converter.MapConverter;
import fr.epita.assistants.yakamon.data.model.GameModel;
import fr.epita.assistants.yakamon.data.model.ItemModel;
import fr.epita.assistants.yakamon.data.model.PlayerModel;
import fr.epita.assistants.yakamon.data.repository.*;
import fr.epita.assistants.yakamon.presentation.api.response.StartResponse;
import fr.epita.assistants.yakamon.utils.ErrorCode;
import fr.epita.assistants.yakamon.utils.tile.ItemType;
import fr.epita.assistants.yakamon.utils.tile.TileType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class GameService {

    @Inject
    MapConverter mapConverter;

    @Inject
    GameRepository gameRepository;

    @Inject
    ItemRepository itemRepository;

    @Inject
    PlayerRepository playerRepository;

    @Inject
    YakadexEntryRepository yakadexEntryRepository;

    @Inject
    YakamonRepository yakamonRepository;

    @Transactional
    public List<List<TileType>> startGame(String mapPath, String playerName)
    {
        if(mapPath == null)
            ErrorCode.BAD_REQUEST.throwException("Map path is Null");
        if(playerName == null)
            ErrorCode.BAD_REQUEST.throwException("Player name is Null");

        String Smap = mapConverter.mapReader(mapPath);
        List<List<TileType>> Lmap = mapConverter.mapConvert(Smap);

        gameRepository.deleteAll();
        GameModel gameModel = new GameModel();
        gameModel.setMap(Smap);
        gameRepository.persist(gameModel);

        itemRepository.deleteAll();
        ItemModel itemModel = new ItemModel();
        itemModel.setType(ItemType.YAKABALL);
        itemModel.setQuantity(5);
        itemRepository.persist(itemModel);

        playerRepository.deleteAll();
        PlayerModel playerModel = new PlayerModel();
        playerModel.name = playerName;
        playerModel.posX = 0;
        playerModel.posY = 0;
        playerRepository.persist(playerModel);

        yakadexEntryRepository.listAll().forEach(E->E.setCaught(false));

        yakamonRepository.deleteAll();

        return Lmap;
    }

}
