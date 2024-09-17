package com.booleanuk.api.controller;

import com.booleanuk.api.models.Game;
import com.booleanuk.api.models.LibraryUser;
import com.booleanuk.api.payload.responses.*;
import com.booleanuk.api.repository.GameRepository;
import com.booleanuk.api.repository.LibraryUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("games")
public class GameController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private LibraryUserRepository libraryUserRepository;

    @GetMapping
    public ResponseEntity<Response<?>> getAllGames(){
        List<Game> games = this.gameRepository.findAll();

        GameListResponse gameListResponse = new GameListResponse();
        gameListResponse.set(games);

        return ResponseEntity.ok(gameListResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getGame(@PathVariable int id) {
        Game game = this.gameRepository.findById(id).orElse(null);

        if (game == null){
            ErrorResponse error = new ErrorResponse();
            error.set("No game with that id found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        GameResponse gameResponse = new GameResponse();
        gameResponse.set(game);

        return ResponseEntity.ok(gameResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createGame(@RequestBody Game game){
        if (game.getTitle() == null || game.getGameType() == null || game.getGenre() == null || game.getAge() < 0){
            ErrorResponse error = new ErrorResponse();
            error.set("Could not create a new game, please check all fields are correct");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        gameRepository.save(game);
        GameResponse gameResponse = new GameResponse();
        gameResponse.set(game);

        return new ResponseEntity<>(gameResponse, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateBook(@PathVariable int id, @RequestBody Game game) {
        Game gameToUpdate = this.gameRepository.findById(id).orElse(null);
        if (gameToUpdate == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        gameToUpdate.setTitle(game.getTitle());
        gameToUpdate.setGameType(game.getGameType());
        gameToUpdate.setAge(game.getAge());
        gameToUpdate.setGenre(game.getGenre());

        try {
            gameToUpdate = this.gameRepository.save(gameToUpdate);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        GameResponse gameResponse = new GameResponse();
        gameResponse.set(gameToUpdate);
        return new ResponseEntity<>(gameResponse, HttpStatus.CREATED);
    }

    //Return book
    @DeleteMapping("/{gameId}/libraryuser/{userId}")
    public ResponseEntity<Response<?>> deleteGame(@PathVariable int gameId, @PathVariable int userId){
        Game game = this.gameRepository.findById(gameId).orElse(null);
        LibraryUser libraryUser = this.libraryUserRepository.findById(userId).orElse(null);

        if (game == null || libraryUser == null){
            ErrorResponse error = new ErrorResponse();
            error.set("No games or user found with that id to remove");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        gameRepository.delete(game);
        libraryUser.removeGame(game);

        GameResponse gameResponse = new GameResponse();
        gameResponse.set(game);

        return new ResponseEntity<>(gameResponse, HttpStatus.CREATED);
    }
}
