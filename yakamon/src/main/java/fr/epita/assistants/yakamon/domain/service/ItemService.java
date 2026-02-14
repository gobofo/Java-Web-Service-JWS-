package fr.epita.assistants.yakamon.domain.service;

import fr.epita.assistants.yakamon.converter.ItemConverter;
import fr.epita.assistants.yakamon.data.repository.GameRepository;
import fr.epita.assistants.yakamon.data.repository.ItemRepository;
import fr.epita.assistants.yakamon.utils.ErrorCode;
import fr.epita.assistants.yakamon.utils.Item;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ItemService {

    @Inject
    ItemRepository itemRepository;

    @Inject
    GameRepository gameRepository;

    @Inject
    ItemConverter itemConverter;

    public List<Item> getInventory()
    {
        if(gameRepository.findAll().count()==0)
            ErrorCode.BAD_REQUEST.throwException("Game is not running");

        return itemConverter.itemConverter(itemRepository.listAll());
    }
}
