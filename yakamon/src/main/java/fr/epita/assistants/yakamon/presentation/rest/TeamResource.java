package fr.epita.assistants.yakamon.presentation.rest;

import fr.epita.assistants.yakamon.domain.service.TeamService;
import fr.epita.assistants.yakamon.presentation.api.response.YakamonTeamResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/team")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TeamResource {

    @Inject
    TeamService teamService;

    @GET
    @Path("/")
    public YakamonTeamResponse getTeam() {
        return new YakamonTeamResponse(teamService.getTeam());
    }
}
