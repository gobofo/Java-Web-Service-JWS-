package fr.epita.assistants.yakamon.domain.service;

import fr.epita.assistants.yakamon.converter.YakamonConverter;
import fr.epita.assistants.yakamon.data.model.YakamonModel;
import fr.epita.assistants.yakamon.data.repository.GameRepository;
import fr.epita.assistants.yakamon.data.repository.YakamonRepository;
import fr.epita.assistants.yakamon.domain.entity.YakamonEntity;
import fr.epita.assistants.yakamon.utils.ErrorCode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class TeamService {

    @Inject
    GameRepository gameRepository;

    @Inject
    YakamonRepository yakamonRepository;

    @Inject
    YakamonConverter yakamonConverter;

    public List<YakamonEntity> getTeam()
    {
        if(gameRepository.findAll().count()==0)
            ErrorCode.BAD_REQUEST.throwException("Game is not running");
        return yakamonConverter.yakamonConverter(yakamonRepository.listAll());
    }
}
