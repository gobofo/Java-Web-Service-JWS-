package fr.epita.assistants.yakamon.presentation.rest;


import fr.epita.assistants.yakamon.domain.service.MoveService;
import fr.epita.assistants.yakamon.presentation.api.request.MoveRequest;
import fr.epita.assistants.yakamon.presentation.api.response.MoveResponse;
import fr.epita.assistants.yakamon.utils.ErrorCode;
import fr.epita.assistants.yakamon.utils.Point;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/move")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MoveResource {

    @Inject
    MoveService moveService;

    @POST
    @Path("/")
    public MoveResponse move(MoveRequest moveRequest)
    {
        if(moveRequest == null)
            ErrorCode.BAD_REQUEST.throwException("Move request is null");

        Point newPos = moveService.move(moveRequest.getDirection());

        return new MoveResponse(newPos.getPosX(), newPos.getPosY());
    }

}
