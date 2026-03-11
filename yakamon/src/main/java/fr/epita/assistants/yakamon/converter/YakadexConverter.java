package fr.epita.assistants.yakamon.converter;

import fr.epita.assistants.yakamon.data.model.YakadexEntryModel;
import fr.epita.assistants.yakamon.domain.entity.YakadexEntity;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class YakadexConverter {


    public List<YakadexEntity> yakadexConverter(List<YakadexEntryModel> listYakadexEntryModel)
    {
        var list = new ArrayList<YakadexEntity>();

//                listYakadexEntryModel.stream().map(e -> new YakadexEntity(
//                e.getId(),
//                e.getName(),
//                e.getFirstType().toString(),
//                e.getSecondType() != null ? e.getSecondType().toString() : "",
//                e.getEvolveThreshold() != null ? e.getEvolveThreshold() : 0,
//                e.getEvolution() != null ? e.getEvolution().id : -1,
//                e.getCaught(),
//                e.getDescription()
//        )).toList();


        for(var e : listYakadexEntryModel)
        {
            if (e.caught)
                list.add(new YakadexEntity(
                        e.getId(),
                        e.getName(),
                        e.getFirstType().toString(),
                        e.getSecondType() != null ? e.getSecondType().toString() : null,
                        e.getEvolveThreshold(),
                        e.getEvolution() != null ? e.getEvolution().id: null,
                        e.getCaught(),
                        e.getDescription()));
            else
                list.add(new YakadexEntity(
                        e.getId(),
                        e.getName(),
                        null,
                        null ,
                        null,
                        null,
                        e.getCaught(),
                        null));
        }
        return list;

    }

}
