package fr.epita.assistants.yakamon.presentation.rest;

import fr.epita.assistants.yakamon.converter.YakamonConverter;
import fr.epita.assistants.yakamon.domain.service.EvolveService;
import fr.epita.assistants.yakamon.domain.service.FeedService;
import fr.epita.assistants.yakamon.domain.service.ReleaseService;
import fr.epita.assistants.yakamon.domain.service.RenameService;
import fr.epita.assistants.yakamon.domain.service.TeamService;
import fr.epita.assistants.yakamon.presentation.api.request.FeedRequest;
import fr.epita.assistants.yakamon.presentation.api.request.RenameRequest;
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
    EvolveService evolveService;

    @Inject
    ReleaseService releaseService;

    @Inject
    RenameService renameService;

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

    @POST
    @Path("/{uuid}/evolve")
    public YakamonResponse evolveYakamon(@PathParam("uuid") UUID uuid) {
        return yakamonConverter.yakamonConverter(evolveService.evolve(uuid));
    }

    @PATCH
    @Path("/{uuid}/rename")
    public YakamonResponse renameYakamon(@PathParam("uuid") UUID uuid, RenameRequest request) {
        return yakamonConverter.yakamonConverter(renameService.rename(uuid, request.getNewNickname()));
    }

    @DELETE
    @Path("/{uuid}/release")
    public jakarta.ws.rs.core.Response releaseYakamon(@PathParam("uuid") UUID uuid) {
        releaseService.release(uuid);
        return jakarta.ws.rs.core.Response.noContent().build();
    }
}
