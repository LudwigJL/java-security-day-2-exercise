package com.booleanuk.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties(value = {"title", "game_studio", "genre", "num_of_players", "age"})
    private LibraryUser libraryUser;

    public Game(String title, String gameType, int age, String genre) {
        this.title = title;
        this.gameType = gameType;
        this.age = age;
        this.genre = genre;
    }

    public void setLibraryUser(LibraryUser libraryUser) {
        this.libraryUser = null;
    }
}
