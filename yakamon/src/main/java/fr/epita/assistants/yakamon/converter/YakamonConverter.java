package fr.epita.assistants.yakamon.converter;

import fr.epita.assistants.yakamon.data.model.YakamonModel;
import fr.epita.assistants.yakamon.domain.entity.YakamonEntity;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class YakamonConverter {
        public List<YakamonEntity> yakamonConverter(List<YakamonModel> yakamonModels)
        {
            return yakamonModels.stream().map(this::yakamonConverter).toList();
        }


        public YakamonEntity yakamonConverter(YakamonModel yakamonModel)
        {
            return new YakamonEntity(
                    yakamonModel.uuid,
                    yakamonModel.nickname,
                    yakamonModel.yakadexEntry.id,
                    yakamonModel.energyPoints
            );
        }
}
