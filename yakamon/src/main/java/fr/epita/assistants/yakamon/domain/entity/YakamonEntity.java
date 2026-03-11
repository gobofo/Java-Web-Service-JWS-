package fr.epita.assistants.yakamon.domain.entity;

import lombok.Value;

import java.util.UUID;

@Value
public class YakamonEntity {

    UUID uuid;
    String nickname;
    Integer yakadexId;
    Integer energyPoints;

}
