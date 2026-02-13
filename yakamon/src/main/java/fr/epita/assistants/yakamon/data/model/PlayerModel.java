package fr.epita.assistants.yakamon.data.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "player")
public class PlayerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID uuid;

    @Column(length = 20, nullable = false)
    public String name;

    @Column(name = "pos_x", nullable = false)
    public Integer posX;

    @Column(name = "pos_y", nullable = false)
    public Integer posY;

    @Column(name = "last_move")
    public LocalDateTime lastMove;

    @Column(name = "last_catch")
    public LocalDateTime lastCatch;

    @Column(name = "last_collect")
    public LocalDateTime lastCollect;

    @Column(name = "last_feed")
    public LocalDateTime lastFeed;
}
