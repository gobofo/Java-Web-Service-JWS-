package fr.epita.assistants.yakamon.presentation.rest;

import fr.epita.assistants.yakamon.data.repository.GameRepository;
import fr.epita.assistants.yakamon.domain.service.GameService;
import fr.epita.assistants.yakamon.domain.service.YakadexService;
import fr.epita.assistants.yakamon.presentation.api.request.YakadexRequest;
import fr.epita.assistants.yakamon.presentation.api.response.YakadexResponse;
import fr.epita.assistants.yakamon.utils.ErrorCode;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/yakadex")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class YakadexResource {

    @Inject
    YakadexService yakadexService;

    @GET
    @Path("/")
    public YakadexResponse getYakadex(@QueryParam("only_missing") boolean onlyMissing) {

        return new YakadexResponse(yakadexService.yakadex(onlyMissing));

    }

}