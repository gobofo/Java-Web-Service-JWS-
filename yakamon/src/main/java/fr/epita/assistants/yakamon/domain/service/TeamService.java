package fr.epita.assistants.yakamon.domain.service;

import fr.epita.assistants.yakamon.converter.YakamonConverter;
import fr.epita.assistants.yakamon.data.repository.GameRepository;
import fr.epita.assistants.yakamon.data.repository.YakamonRepository;
import fr.epita.assistants.yakamon.presentation.api.response.YakamonResponse;
import fr.epita.assistants.yakamon.utils.ErrorCode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class TeamService {

    @Inject
    GameRepository gameRepository;

    @Inject
    YakamonRepository yakamonRepository;

    @Inject
    YakamonConverter yakamonConverter;

    public List<YakamonResponse> getTeam()
    {
        if(gameRepository.findAll().count()==0)
            ErrorCode.BAD_REQUEST.throwException("Game is not running");
        return yakamonConverter.yakamonConverter(yakamonRepository.listAll());
    }
}
