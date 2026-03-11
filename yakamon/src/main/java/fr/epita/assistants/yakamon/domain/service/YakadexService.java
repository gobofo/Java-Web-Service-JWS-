package fr.epita.assistants.yakamon.domain.service;

import fr.epita.assistants.yakamon.converter.YakadexConverter;
import fr.epita.assistants.yakamon.data.model.YakadexEntryModel;
import fr.epita.assistants.yakamon.data.repository.GameRepository;
import fr.epita.assistants.yakamon.data.repository.YakadexEntryRepository;
import fr.epita.assistants.yakamon.data.repository.YakamonRepository;
import fr.epita.assistants.yakamon.domain.entity.YakadexEntity;
import fr.epita.assistants.yakamon.utils.ErrorCode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class YakadexService {

    @Inject
    GameRepository gameRepository;

    @Inject
    YakadexEntryRepository yakadexEntryRepository;

    @Inject
    YakadexConverter yakadexConverter;

    public List<YakadexEntity> yakadex(boolean onlyMissing)
    {
        if(gameRepository.findAll().count()==0)
            ErrorCode.BAD_REQUEST.throwException("Game is not running");
        List<YakadexEntryModel> listYakadexEntryModel;
        if(onlyMissing)
            listYakadexEntryModel = yakadexEntryRepository.list("caught", false);
        else
            listYakadexEntryModel = yakadexEntryRepository.listAll();

        return yakadexConverter.yakadexConverter(listYakadexEntryModel);
    }
}
