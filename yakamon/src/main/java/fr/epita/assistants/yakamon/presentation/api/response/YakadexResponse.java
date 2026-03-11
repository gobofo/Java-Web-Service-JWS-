package fr.epita.assistants.yakamon.presentation.api.response;

import fr.epita.assistants.yakamon.data.model.YakadexEntryModel;
import fr.epita.assistants.yakamon.data.repository.YakadexEntryRepository;
import fr.epita.assistants.yakamon.domain.entity.YakadexEntity;
import lombok.Value;

import java.util.List;

@Value
public class YakadexResponse {
    List<YakadexEntity> entries;
}
