package fr.epita.assistants.yakamon.converter;

import fr.epita.assistants.yakamon.utils.ErrorCode;
import fr.epita.assistants.yakamon.utils.tile.Collectible;
import fr.epita.assistants.yakamon.utils.tile.CollectibleUtils;
import fr.epita.assistants.yakamon.utils.tile.TerrainType;
import fr.epita.assistants.yakamon.utils.tile.TileType;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MapConverter {

    public String mapReader(String mapPath)
    {

        String map = "";
        try {
            map = Files.readString(Path.of(mapPath));
        } catch (IOException e) {
            ErrorCode.BAD_REQUEST.throwException("Map not found at given path");
        }

        return map.replaceAll("\n", ";");
    }

    public List<List<TileType>> mapConvert(String Smap)
    {
        List<List<TileType>> Lmap = new ArrayList<>();


        List<TileType> line = new ArrayList<>();
        for (int i = 0; i < Smap.length(); i+=3) {

            if(Smap.charAt(i) == ';') {
                Lmap.add(line);
                line = new ArrayList<>();
                i++;
                if(i>=Smap.length())
                    break;
            }

            char number = Smap.charAt(i);
            char terrainType = Smap.charAt(i+1);
            char collectible = Smap.charAt(i+2);

            for (int j = 0; j < number - '0'; j++) {
                TileType tile = new TileType(TerrainType.getTerrain(terrainType), CollectibleUtils.getCollectible(collectible));

                line.add(tile);
            }
        }
        if(!line.isEmpty())
            Lmap.add(line);

        return Lmap;
    }

    public String mapConvert(List<List<TileType>> Lmap)
    {
        StringBuilder Smap = new StringBuilder();

        for (List<TileType> line : Lmap)
        {
            if(line.isEmpty())
                ErrorCode.INTERNAL_SERVER_ERROR.throwException("Empty line during conversion");

            char terrainType = line.getFirst().getTerrainType().getValue();
            char collectible = line.getFirst().getCollectible().getCollectibleInfo().getValue();
            char number = '1';
            for (var e : line.subList(1, line.size()))
            {
                char actualTerrainType = e.getTerrainType().getValue();
                char actualCollectible = e.getCollectible().getCollectibleInfo().getValue();
                if(number == '9' || terrainType!=actualTerrainType || collectible!=actualCollectible)
                {
                    Smap.append(number + terrainType + collectible);
                    number = '1';
                    terrainType = actualTerrainType;
                    collectible = actualCollectible;
                    continue;
                }
                number++;
            }
            Smap.append(';');
        }

        return Smap.toString();
    }

}
