package fr.epita.assistants.yakamon.presentation.rest;

import fr.epita.assistants.yakamon.domain.service.ItemService;
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
    ItemService itemService;

    @Path("/")
    @GET
    public InventoryResponse inventory()
    {
        return new InventoryResponse(itemService.getInventory());
    }

}
