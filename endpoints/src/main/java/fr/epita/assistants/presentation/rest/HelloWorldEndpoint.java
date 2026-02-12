package fr.epita.assistants.presentation.rest;

import fr.epita.assistants.presentation.rest.response.HelloResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldEndpoint {
    @Path("/")
    @GET
    public Response helloWorld() {
        HelloResponse content = new HelloResponse("Hello world!");
        return Response.ok(content).build();
    }

}
