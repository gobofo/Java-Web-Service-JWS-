package fr.epita.assistants.yakamon.presentation.api.response;

import fr.epita.assistants.yakamon.utils.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import java.util.List;

@Value
public class InventoryResponse {

    List<Item> items;

}
