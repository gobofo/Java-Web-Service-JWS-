package fr.epita.assistants.yakamon.presentation.rest;

import fr.epita.assistants.yakamon.domain.service.TeamService;
import fr.epita.assistants.yakamon.presentation.api.response.TeamResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/team")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TeamResource {

    @Inject
    TeamService teamService;

    @GET
    @Path("/")
    public TeamResponse getTeam() {
        return new TeamResponse(teamService.getTeam());
    }
}
