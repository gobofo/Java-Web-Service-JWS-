package fr.epita.assistants.yakamon.data.repository;

import fr.epita.assistants.yakamon.data.model.GameModel;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.runtime.Hibernate;

public class GameRepository implements PanacheRepository<GameModel> {
}
