package fr.epita.assistants.yakamon.converter;

import fr.epita.assistants.yakamon.data.model.ItemModel;
import fr.epita.assistants.yakamon.utils.Item;
import fr.epita.assistants.yakamon.utils.tile.ItemType;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ItemConverter {

    public List<Item> itemConverter(List<ItemModel> listItemModel)
    {
        List<Item> listItem = new ArrayList<>();

        for(var e : listItemModel)
        {
            listItem.add(new Item(e.getType(), e.getQuantity()));
        }

        return listItem;
    }
}
