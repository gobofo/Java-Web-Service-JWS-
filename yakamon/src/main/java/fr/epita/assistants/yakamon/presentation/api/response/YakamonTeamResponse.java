package fr.epita.assistants.yakamon.presentation.api.response;

import lombok.Value;

import java.util.List;

@Value
public class YakamonTeamResponse {

    List<YakamonResponse> yakamons;

}
