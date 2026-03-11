package fr.epita.assistants.yakamon.converter;

import fr.epita.assistants.yakamon.data.model.YakamonModel;
import fr.epita.assistants.yakamon.presentation.api.response.CatchResponse;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CatchConverter {


    public CatchResponse CatchConverter(YakamonModel yakamonModel)
    {
        return new CatchResponse(yakamonModel.getUuid(), yakamonModel.getNickname(), yakamonModel.getYakadexEntry().getId(), yakamonModel.getEnergyPoints());
    }

}
