package fr.epita.assistants.yakamon.presentation.rest;

import fr.epita.assistants.yakamon.domain.service.CatchService;
import fr.epita.assistants.yakamon.presentation.api.response.CatchResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/catch")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CatchResource {

    @Inject
    CatchService catchService;

    @Path("/")
    @POST
    public CatchResponse catchYakamon(){
        var catchEntity = catchService.catchYakamon();
        return new CatchResponse(catchEntity.getUuid(), catchEntity.getNickname(), catchEntity.yakadexEntry.id , catchEntity.getEnergyPoints());
    }
}
