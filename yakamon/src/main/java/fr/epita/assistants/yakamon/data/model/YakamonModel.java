package fr.epita.assistants.yakamon.data.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "yakamon")
public class YakamonModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID uuid;

    @Column(length = 20, nullable = false)
    public String nickname;

    @Column(name="energy_points", nullable = false)
    public Integer energyPoints;

    @ManyToOne
    @JoinColumn(name = "yakadex_entry_id", referencedColumnName = "id", nullable = false)
    public YakadexEntryModel yakadexEntry;
}
