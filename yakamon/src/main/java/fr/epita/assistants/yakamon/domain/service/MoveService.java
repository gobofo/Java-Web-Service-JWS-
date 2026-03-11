package fr.epita.assistants.yakamon.domain.service;

import fr.epita.assistants.yakamon.converter.MapConverter;
import fr.epita.assistants.yakamon.data.model.PlayerModel;
import fr.epita.assistants.yakamon.data.repository.GameRepository;
import fr.epita.assistants.yakamon.data.repository.PlayerRepository;
import fr.epita.assistants.yakamon.domain.entity.MoveEntity;
import fr.epita.assistants.yakamon.utils.Direction;
import fr.epita.assistants.yakamon.utils.ErrorCode;
import fr.epita.assistants.yakamon.utils.Point;
import fr.epita.assistants.yakamon.utils.tile.TileType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@ApplicationScoped
public class MoveService {

    @Inject
    GameRepository gameRepository;

    @Inject
    PlayerRepository playerRepository;

    @Inject
    MapConverter mapConverter;

    @ConfigProperty(name="JWS_TICK_DURATION") Integer JWS_TICK_DURATION;

    @ConfigProperty(name="JWS_MOVEMENT_DELAY") Integer JWS_MOVEMENT_DELAY;

    private Boolean cooldown(PlayerModel player)
    {
        if(player.lastMove == null)
            return false;
        LocalDateTime next_move = player.lastMove.plus(JWS_TICK_DURATION * JWS_MOVEMENT_DELAY, ChronoUnit.MILLIS);;
        return LocalDateTime.now().isBefore(next_move);

    }

    @Transactional
    public Point move(String direction)
    {

        if(gameRepository.findAll().count()==0)
            ErrorCode.BAD_REQUEST.throwException("Game is not running");

        Point point = null;
        try {
            point = Direction.valueOf(direction).getPoint();
        } catch (Exception e)
        {
            ErrorCode.BAD_REQUEST.throwException("Invalid direction");
        }

        PlayerModel player = playerRepository.listAll().getFirst();

        if(cooldown(player)){
            ErrorCode.TOO_MANY_REQUESTS.throwException("You can't move now");
        }

        Point destination = new Point(player.posX + point.getPosX(), player.posY + point.getPosY());

        List<List<TileType>> map = mapConverter.mapConvert(gameRepository.listAll().getFirst().getMap());

        if(destination.getPosX() < 0 || destination.getPosY() < 0 || destination.getPosY() >= map.size() || destination.getPosX() >= map.get(destination.getPosY()).size())
        {
            ErrorCode.BAD_REQUEST.throwException("Out of bounds");
        }

        if(!map.get(destination.getPosY()).get(destination.getPosX()).getTerrainType().isWalkable())
            ErrorCode.BAD_REQUEST.throwException("Not walkable");


        player.setLastMove(LocalDateTime.now());
        player.setPosX(destination.getPosX());
        player.setPosY(destination.getPosY());

        return destination;

    }


}
