package fr.epita.assistants.yakamon.presentation.api.response;

import lombok.Value;

import java.util.UUID;

@Value
public class CatchResponse {

    UUID uuid;
    String nickname;
    Integer yakadexId;
    Integer energyPoints;

}
