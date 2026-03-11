package fr.epita.assistants.yakamon.converter;

import fr.epita.assistants.yakamon.data.model.YakamonModel;
import fr.epita.assistants.yakamon.presentation.api.response.YakamonResponse;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class YakamonConverter {
        public List<YakamonResponse> yakamonConverter(List<YakamonModel> yakamonModels)
        {
            return yakamonModels.stream().map(this::yakamonConverter).toList();
        }


        public YakamonResponse yakamonConverter(YakamonModel yakamonModel)
        {
            return new YakamonResponse(
                    yakamonModel.uuid,
                    yakamonModel.nickname,
                    yakamonModel.yakadexEntry.id,
                    yakamonModel.energyPoints
            );
        }
}
