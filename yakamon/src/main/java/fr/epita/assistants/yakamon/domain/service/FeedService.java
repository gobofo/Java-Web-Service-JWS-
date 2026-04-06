package fr.epita.assistants.yakamon.domain.service;

import fr.epita.assistants.yakamon.data.model.ItemModel;
import fr.epita.assistants.yakamon.data.model.YakamonModel;
import fr.epita.assistants.yakamon.data.repository.GameRepository;
import fr.epita.assistants.yakamon.data.repository.ItemRepository;
import fr.epita.assistants.yakamon.data.repository.PlayerRepository;
import fr.epita.assistants.yakamon.data.repository.YakamonRepository;
import fr.epita.assistants.yakamon.utils.ErrorCode;
import fr.epita.assistants.yakamon.utils.tile.ItemType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Transactional
@ApplicationScoped
public class FeedService {

    @Inject
    GameRepository gameRepository;

    @Inject
    PlayerRepository playerRepository;

    @Inject
    YakamonRepository yakamonRepository;

    @Inject
    ItemRepository itemRepository;

    @ConfigProperty(name = "JWS_TICK_DURATION")
    Integer JWS_TICK_DURATION;

    @ConfigProperty(name = "JWS_FEED_DELAY")
    Integer JWS_FEED_DELAY;

    public YakamonModel feed(UUID uuid, int quantity) {
        if (gameRepository.findAll().count() == 0)
            ErrorCode.BAD_REQUEST.throwException("Game is not running");

        if (quantity <= 0)
            ErrorCode.BAD_REQUEST.throwException("Quantity must be greater than 0");

        var results = yakamonRepository.list("uuid", uuid);
        if (results.isEmpty())
            ErrorCode.NOT_FOUND.throwException("Yakamon not found");

        var player = playerRepository.findAll().firstResult();
        if (player.lastFeed != null) {
            LocalDateTime nextFeed = player.lastFeed.plus((long) JWS_TICK_DURATION * JWS_FEED_DELAY, ChronoUnit.MILLIS);
            if (LocalDateTime.now().isBefore(nextFeed))
                ErrorCode.TOO_MANY_REQUESTS.throwException("Feed is on cooldown");
        }

        var scroogeList = itemRepository.list("type", ItemType.SCROOGE);
        if (scroogeList.isEmpty())
            ErrorCode.BAD_REQUEST.throwException("Not enough Scrooge");

        ItemModel scrooge = scroogeList.getFirst();
        if (scrooge.getQuantity() < quantity)
            ErrorCode.BAD_REQUEST.throwException("Not enough Scrooge");

        YakamonModel yakamon = results.getFirst();
        yakamon.energyPoints += quantity;
        yakamonRepository.persist(yakamon);

        scrooge.setQuantity(scrooge.getQuantity() - quantity);
        itemRepository.persist(scrooge);

        player.lastFeed = LocalDateTime.now();

        return yakamon;
    }
}
