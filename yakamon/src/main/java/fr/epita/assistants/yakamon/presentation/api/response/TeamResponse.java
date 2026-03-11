package fr.epita.assistants.yakamon.presentation.api.response;

import fr.epita.assistants.yakamon.data.model.YakamonModel;
import fr.epita.assistants.yakamon.domain.entity.YakamonEntity;
import lombok.Data;
import lombok.Value;

import java.util.List;

@Value
public class TeamResponse {

    List<YakamonEntity> yakamons;

}
