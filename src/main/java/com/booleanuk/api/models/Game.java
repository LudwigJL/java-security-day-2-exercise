package com.booleanuk.api.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String title;

    @Column
    private String gameType;

    @Column
    private int age;

    @Column
    private String genre;

    public Game(String title, String gameType, int age, String genre) {
        this.title = title;
        this.gameType = gameType;
        this.age = age;
        this.genre = genre;
    }
}
