package fr.epita.assistants.yakamon_testsuite;


import fr.epita.assistants.yakamon.converter.ItemConverter;
import fr.epita.assistants.yakamon.converter.MapConverter;
import fr.epita.assistants.yakamon.converter.YakadexConverter;
import fr.epita.assistants.yakamon.converter.YakamonConverter;
import fr.epita.assistants.yakamon.data.model.ItemModel;
import fr.epita.assistants.yakamon.data.model.YakadexEntryModel;
import fr.epita.assistants.yakamon.data.model.YakamonModel;
import fr.epita.assistants.yakamon.presentation.api.response.YakamonResponse;
import fr.epita.assistants.yakamon.presentation.api.response.YakadexEntryResponse;
import fr.epita.assistants.yakamon.utils.ElementType;
import fr.epita.assistants.yakamon.utils.Item;
import fr.epita.assistants.yakamon.utils.tile.ItemType;
import fr.epita.assistants.yakamon.utils.tile.TerrainType;
import fr.epita.assistants.yakamon.utils.tile.TileType;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class Test {

    private MapConverter mapConverter;
    private ItemConverter itemConverter;
    private YakamonConverter yakamonConverter;
    private YakadexConverter yakadexConverter;

    @BeforeEach
    void setUp() {
        mapConverter = new MapConverter();
        itemConverter = new ItemConverter();
        yakamonConverter = new YakamonConverter();
        yakadexConverter = new YakadexConverter();
    }

    // mapConvert(string)

    // chaine vide retourne une liste vide
    @org.junit.jupiter.api.Test
    void testMapConvertStringEmptyReturnsEmptyList() {
        List<List<TileType>> result = mapConverter.mapConvert("");
        assertTrue(result.isEmpty());
    }

    // "1GN;" une ligne avec une seule tuile GRASS
    @org.junit.jupiter.api.Test
    void testMapConvertStringSingleGrassTile() {
        List<List<TileType>> result = mapConverter.mapConvert("1GN;");
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).size());
        assertEquals(TerrainType.GRASS, result.get(0).get(0).getTerrainType());
    }

    // "3GN;" une ligne avec 3 tuiles GRASS
    @org.junit.jupiter.api.Test
    void testMapConvertStringRleExpansion() {
        List<List<TileType>> result = mapConverter.mapConvert("3GN;");
        assertEquals(1, result.size());
        assertEquals(3, result.get(0).size());
        assertEquals(TerrainType.GRASS, result.get(0).get(0).getTerrainType());
    }

    // deux lignes donc deux listes distinctes avec les bonnes tailles
    @org.junit.jupiter.api.Test
    void testMapConvertStringTwoLines() {
        List<List<TileType>> result = mapConverter.mapConvert("1GN;2GN;");
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).size());
        assertEquals(2, result.get(1).size());
    }

    // terrain inconnu remplace par GRASS par defaut
    @org.junit.jupiter.api.Test
    void testMapConvertStringUnknownTerrainDefaultsToGrass() {
        List<List<TileType>> result = mapConverter.mapConvert("1ZN;");
        assertEquals(1, result.size());
        assertEquals(TerrainType.GRASS, result.get(0).get(0).getTerrainType());
    }

    // deux groupes differents sur une ligne
    @org.junit.jupiter.api.Test
    void testMapConvertStringTwoGroupsOnOneLine() {
        List<List<TileType>> result = mapConverter.mapConvert("2GN1WN;");
        assertEquals(1, result.size());
        assertEquals(3, result.get(0).size());
        assertEquals(TerrainType.GRASS, result.get(0).get(0).getTerrainType());
        assertEquals(TerrainType.WATER, result.get(0).get(2).getTerrainType());
    }

    // tuile avec collectible YAKABALL
    @org.junit.jupiter.api.Test
    void testMapConvertStringTileWithYakaball() {
        List<List<TileType>> result = mapConverter.mapConvert("1GY;");
        assertEquals(1, result.size());
        assertNotNull(result.get(0).get(0).getCollectible());
        assertEquals(ItemType.YAKABALL, result.get(0).get(0).getCollectible());
    }

    // mapConvert(List)

    // une tuile GRASS sans collectible
    @org.junit.jupiter.api.Test
    void testMapConvertListSingleGrassTile() {
        TileType tile = new TileType(TerrainType.GRASS, null);
        String result = mapConverter.mapConvert(List.of(List.of(tile)));
        assertEquals("1GN;", result);
    }

    // deux tuiles GRASS identiques
    @org.junit.jupiter.api.Test
    void testMapConvertListTwoSameTilesCompressed() {
        TileType tile = new TileType(TerrainType.GRASS, null);
        String result = mapConverter.mapConvert(List.of(List.of(tile, tile)));
        assertEquals("2GN;", result);
    }

    // 10 tuiles identiques
    @org.junit.jupiter.api.Test
    void testMapConvertListSplitsGroupAtNine() {
        TileType tile = new TileType(TerrainType.GRASS, null);
        List<TileType> line = new ArrayList<>();
        for (int i = 0; i < 10; i++) line.add(tile);

        String result = mapConverter.mapConvert(List.of(line));
        assertEquals("9GN1GN;", result);
    }

    // deux terrains differents
    @org.junit.jupiter.api.Test
    void testMapConvertListDifferentTilesNotCompressed() {
        TileType tileG = new TileType(TerrainType.GRASS, null);
        TileType tileW = new TileType(TerrainType.WATER, null);
        String result = mapConverter.mapConvert(List.of(List.of(tileG, tileW)));
        assertEquals("1GN1WN;", result);
    }

    // tuile avec collectible YAKABALL
    @org.junit.jupiter.api.Test
    void testMapConvertListTileWithYakaball() {
        TileType tile = new TileType(TerrainType.GRASS, ItemType.YAKABALL);
        String result = mapConverter.mapConvert(List.of(List.of(tile)));
        assertEquals("1GY;", result);
    }

    // aller-retour String -> List -> String
    @org.junit.jupiter.api.Test
    void testMapConvertRoundTrip() {
        String original = "2GN;1WN;";
        List<List<TileType>> parsed = mapConverter.mapConvert(original);
        String result = mapConverter.mapConvert(parsed);
        assertEquals(original, result);
    }

    // ItemConverter

    // liste vide
    @org.junit.jupiter.api.Test
    void testItemConverterEmptyList() {
        List<Item> result = itemConverter.itemConverter(new ArrayList<>());
        assertTrue(result.isEmpty());
    }

    // un ItemModel
    @org.junit.jupiter.api.Test
    void testItemConverterSingleItem() {
        ItemModel model = new ItemModel();
        model.type = ItemType.YAKABALL;
        model.quantity = 3;

        List<Item> result = itemConverter.itemConverter(List.of(model));
        assertEquals(1, result.size());
        assertEquals(ItemType.YAKABALL, result.get(0).getItemType());
        assertEquals(3, result.get(0).getQuantity());
    }

    // deux ItemModel
    @org.junit.jupiter.api.Test
    void testItemConverterMultipleItemsPreservesOrder() {
        ItemModel m1 = new ItemModel();
        m1.type = ItemType.YAKABALL;
        m1.quantity = 5;

        ItemModel m2 = new ItemModel();
        m2.type = ItemType.SCROOGE;
        m2.quantity = 1;

        List<Item> result = itemConverter.itemConverter(List.of(m1, m2));
        assertEquals(2, result.size());
        assertEquals(ItemType.YAKABALL, result.get(0).getItemType());
        assertEquals(ItemType.SCROOGE, result.get(1).getItemType());
    }

    // YakamonConverter

    // liste vide
    @org.junit.jupiter.api.Test
    void testYakamonConverterEmptyList() {
        List<YakamonResponse> result = yakamonConverter.yakamonConverter(new ArrayList<>());
        assertTrue(result.isEmpty());
    }

    // un YakamonModel
    @org.junit.jupiter.api.Test
    void testYakamonConverterSingleModel() {
        YakadexEntryModel entry = new YakadexEntryModel();
        entry.id = 7;

        YakamonModel model = new YakamonModel();
        UUID uuid = UUID.randomUUID();
        model.uuid = uuid;
        model.nickname = "Yakamon";
        model.yakadexEntry = entry;
        model.energyPoints = 80;

        YakamonResponse entity = yakamonConverter.yakamonConverter(model);
        assertEquals(uuid, entity.getUuid());
        assertEquals("Yakamon", entity.getNickname());
        assertEquals(7, entity.getYakadexId());
        assertEquals(80, entity.getEnergyPoints());
    }

    // liste de deux modeles
    @org.junit.jupiter.api.Test
    void testYakamonConverterListPreservesOrder() {
        YakadexEntryModel e1 = new YakadexEntryModel();
        e1.id = 1;
        YakadexEntryModel e2 = new YakadexEntryModel();
        e2.id = 2;

        YakamonModel m1 = new YakamonModel();
        m1.uuid = UUID.randomUUID();
        m1.nickname = "Alpha";
        m1.yakadexEntry = e1;
        m1.energyPoints = 100;

        YakamonModel m2 = new YakamonModel();
        m2.uuid = UUID.randomUUID();
        m2.nickname = "Beta";
        m2.yakadexEntry = e2;
        m2.energyPoints = 50;

        List<YakamonResponse> result = yakamonConverter.yakamonConverter(List.of(m1, m2));
        assertEquals(2, result.size());
        assertEquals("Alpha", result.get(0).getNickname());
        assertEquals("Beta", result.get(1).getNickname());
    }

    // YakadexConverter

    // liste vide
    @org.junit.jupiter.api.Test
    void testYakadexConverterEmptyList() {
        List<YakadexEntryResponse> result = yakadexConverter.yakadexConverter(new ArrayList<>());
        assertTrue(result.isEmpty());
    }

    // entree capturee sans secondType
    @org.junit.jupiter.api.Test
    void testYakadexConverterCaughtNoSecondType() {
        YakadexEntryModel model = new YakadexEntryModel();
        model.id = 1;
        model.name = "Yakamon";
        model.caught = true;
        model.firstType = ElementType.FIRE;
        model.secondType = null;
        model.evolveThreshold = 16;
        model.evolution = null;
        model.description = "description";

        List<YakadexEntryResponse> result = yakadexConverter.yakadexConverter(List.of(model));
        YakadexEntryResponse entity = result.get(0);
        assertEquals(1, entity.getId());
        assertEquals("Yakamon", entity.getName());
        assertEquals(ElementType.FIRE.toString(), entity.getFirstType());
        assertNull(entity.getSecondType());
        assertEquals(16, entity.getEvolveThreshold());
        assertEquals("description", entity.getDescription());
        assertTrue(entity.getCaught());
    }

    // entree non capturee
    @org.junit.jupiter.api.Test
    void testYakadexConverterNotCaughtHasNullFields() {
        YakadexEntryModel model = new YakadexEntryModel();
        model.id = 2;
        model.name = "Yakamon";
        model.caught = false;
        model.firstType = ElementType.WATER;
        model.description = "description";

        List<YakadexEntryResponse> result = yakadexConverter.yakadexConverter(List.of(model));
        YakadexEntryResponse entity = result.get(0);
        assertEquals(2, entity.getId());
        assertEquals("Yakamon", entity.getName());
        assertNull(entity.getFirstType());
        assertNull(entity.getDescription());
        assertFalse(entity.getCaught());
    }

    // entree capturee avec deux types
    @org.junit.jupiter.api.Test
    void testYakadexConverterCaughtWithSecondType() {
        YakadexEntryModel model = new YakadexEntryModel();
        model.id = 3;
        model.name = "Yakamon";
        model.caught = true;
        model.firstType = ElementType.WATER;
        model.secondType = ElementType.FIRE;
        model.evolveThreshold = null;
        model.evolution = null;
        model.description = "description";

        List<YakadexEntryResponse> result = yakadexConverter.yakadexConverter(List.of(model));
        YakadexEntryResponse entity = result.get(0);
        assertEquals(ElementType.WATER.toString(), entity.getFirstType());
        assertEquals(ElementType.FIRE.toString(), entity.getSecondType());
    }

    // entree capturee avec une evolution
    @org.junit.jupiter.api.Test
    void testYakadexConverterCaughtWithEvolution() {
        YakadexEntryModel evolution = new YakadexEntryModel();
        evolution.id = 99;

        YakadexEntryModel model = new YakadexEntryModel();
        model.id = 4;
        model.name = "Yakamon";
        model.caught = true;
        model.firstType = ElementType.FIRE;
        model.secondType = null;
        model.evolveThreshold = 20;
        model.evolution = evolution;
        model.description = "description";

        List<YakadexEntryResponse> result = yakadexConverter.yakadexConverter(List.of(model));
        YakadexEntryResponse entity = result.get(0);
        assertEquals(99, entity.getEvolutionId());
        assertEquals(20, entity.getEvolveThreshold());
    }

    // une entree capturee + une non capturee
    @org.junit.jupiter.api.Test
    void testYakadexConverterMixedList() {
        YakadexEntryModel caught = new YakadexEntryModel();
        caught.id = 1;
        caught.name = "Visible";
        caught.caught = true;
        caught.firstType = ElementType.FIRE;
        caught.secondType = null;
        caught.evolveThreshold = null;
        caught.evolution = null;
        caught.description = "connu";

        YakadexEntryModel notCaught = new YakadexEntryModel();
        notCaught.id = 2;
        notCaught.name = "Inconnu";
        notCaught.caught = false;
        notCaught.firstType = ElementType.WATER;
        notCaught.description = "jamais vu";

        List<YakadexEntryResponse> result = yakadexConverter.yakadexConverter(List.of(caught, notCaught));
        assertEquals(2, result.size());
        assertEquals(ElementType.FIRE.toString(), result.get(0).getFirstType());
        assertNull(result.get(1).getFirstType());
        assertNull(result.get(1).getDescription());
    }
}
