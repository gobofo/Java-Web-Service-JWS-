package fr.epita.assistants.yakamon.presentation.rest;

import fr.epita.assistants.yakamon.converter.YakamonConverter;
import fr.epita.assistants.yakamon.domain.service.FeedService;
import fr.epita.assistants.yakamon.domain.service.TeamService;
import fr.epita.assistants.yakamon.presentation.api.request.FeedRequest;
import fr.epita.assistants.yakamon.presentation.api.response.YakamonResponse;
import fr.epita.assistants.yakamon.presentation.api.response.YakamonTeamResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.UUID;

@Path("/team")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TeamResource {

    @Inject
    TeamService teamService;

    @Inject
    FeedService feedService;

    @Inject
    YakamonConverter yakamonConverter;

    @GET
    @Path("/")
    public YakamonTeamResponse getTeam() {
        return new YakamonTeamResponse(yakamonConverter.yakamonConverter(teamService.getTeam()));
    }

    @POST
    @Path("/{uuid}/feed")
    public YakamonResponse feedYakamon(@PathParam("uuid") UUID uuid, FeedRequest request) {
        return yakamonConverter.yakamonConverter(feedService.feed(uuid, request.getQuantity()));
    }
}
