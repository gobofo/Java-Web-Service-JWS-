package fr.epita.assistants.yakamon.data.model;

import fr.epita.assistants.yakamon.utils.ElementType;
import jakarta.persistence.*;

@Entity
@Table(name = "yakadex_entry")
public class YakadexEntryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(length = 20, nullable = false)
    public String name;

    @Column(nullable = false)
    public Boolean caught;

    @Enumerated(EnumType.STRING)
    @Column(name = "first_type", nullable = false)
    public ElementType firstType;

    @Enumerated(EnumType.STRING)
    @Column(name = "second_type")
    public ElementType secondType;

    @Column(nullable = false)
    public String description;

    @OneToOne
    @JoinColumn(name = "evolution_id", referencedColumnName = "id")
    public YakadexEntryModel evolution;

    @Column(name = "evolve_threshold")
    public Integer evolveThreshold;
}
