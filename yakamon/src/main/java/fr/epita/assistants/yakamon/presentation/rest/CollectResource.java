package fr.epita.assistants.yakamon.presentation.rest;

import fr.epita.assistants.yakamon.domain.service.CollectService;
import fr.epita.assistants.yakamon.presentation.api.response.CollectResponse;
import fr.epita.assistants.yakamon.utils.tile.TileType;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/collect")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CollectResource {

    @Inject
    CollectService collectService;

    @POST
    @Path("/")
    public CollectResponse collect() {
        TileType updatedTile = collectService.collect();
        return new CollectResponse(updatedTile);
    }
}
