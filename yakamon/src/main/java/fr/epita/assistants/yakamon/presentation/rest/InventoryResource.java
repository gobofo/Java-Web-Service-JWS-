package fr.epita.assistants.yakamon.presentation.rest;

import fr.epita.assistants.yakamon.domain.service.PlayerService;
import fr.epita.assistants.yakamon.presentation.api.response.InventoryResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/inventory")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InventoryResource {

    @Inject
    PlayerService playerService;

    @Path("/")
    @GET
    public InventoryResponse inventory()
    {
        return new InventoryResponse(playerService.getInventory());
    }

}
