package fr.epita.assistants.yakamon.utils;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static jakarta.ws.rs.core.Response.Status;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    BAD_REQUEST(Status.BAD_REQUEST, "Bad request"),
    INTERNAL_SERVER_ERROR(Status.INTERNAL_SERVER_ERROR, "Internal server error"),
    TOO_MANY_REQUESTS(Status.TOO_MANY_REQUESTS, "Too many request");

    private final Response.Status errorCode;

    private final String errorMessage;

    public WebApplicationException getException() {
        return new WebApplicationException(Response.status(errorCode).entity(new ErrorInfo(errorMessage)).build());
    }

    public void throwException() {
        throw getException();
    }

    public void throwException(String prefix) {
        throw new WebApplicationException(Response.status(errorCode).entity(new ErrorInfo(prefix + ": " + errorMessage)).build());
    }
}
