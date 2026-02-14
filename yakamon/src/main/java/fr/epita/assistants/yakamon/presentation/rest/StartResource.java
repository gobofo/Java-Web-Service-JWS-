package fr.epita.assistants.yakamon.presentation.rest;

import fr.epita.assistants.yakamon.domain.service.GameService;
import fr.epita.assistants.yakamon.presentation.api.request.StartRequest;
import fr.epita.assistants.yakamon.presentation.api.response.StartResponse;
import fr.epita.assistants.yakamon.utils.ErrorCode;
import fr.epita.assistants.yakamon.utils.tile.TileType;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/start")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StartResource {

    @Inject
    GameService gameService;

    @POST
    @Path("/")
    public StartResponse start(StartRequest request) {

        if(request==null)
            ErrorCode.BAD_REQUEST.throwException("Start request is Null");

        List<List<TileType>> map = gameService.startGame(request.getMapPath(), request.getPlayerName());

        StartResponse startResponse = new StartResponse();

        startResponse.setTiles(map);

        return startResponse;
    }
}
