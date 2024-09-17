package com.booleanuk.api.controller;

import com.booleanuk.api.models.Game;
import com.booleanuk.api.models.LibraryUser;
import com.booleanuk.api.models.User;

import com.booleanuk.api.payload.responses.ErrorResponse;
import com.booleanuk.api.payload.responses.LibraryUserListResponse;
import com.booleanuk.api.payload.responses.LibraryUserResponse;
import com.booleanuk.api.payload.responses.Response;
import com.booleanuk.api.repository.GameRepository;
import com.booleanuk.api.repository.LibraryUserRepository;
import com.booleanuk.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("libraryuser")
public class LibraryUserController {

    @Autowired
    private LibraryUserRepository libraryUserRepository;

    @Autowired
    private GameRepository gameRepository;

    @GetMapping
    public ResponseEntity<Response<?>> getAllUsers(){
        List<LibraryUser> libraryUsers = this.libraryUserRepository.findAll();

        LibraryUserListResponse libraryUserListResponse = new LibraryUserListResponse();
        libraryUserListResponse.set(libraryUsers);

        return ResponseEntity.ok(libraryUserListResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getAUser(@PathVariable int id){
        LibraryUser libraryUser = this.libraryUserRepository.findById(id).orElse(null);

        if (libraryUser == null){
            ErrorResponse error = new ErrorResponse();
            error.set("No user with tha id is found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        LibraryUserResponse libraryUserResponse = new LibraryUserResponse();
        libraryUserResponse.set(libraryUser);

        return ResponseEntity.ok(libraryUserResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createUser(@RequestBody LibraryUser libraryUser){
        if (libraryUser.getFirstName() == null || libraryUser.getLastName() == null || libraryUser.getEmail() == null || libraryUser.getPhone() == null){
            ErrorResponse error = new ErrorResponse();
            error.set("Could not create a new user, please check all fields are correct");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        this.libraryUserRepository.save(libraryUser);

        LibraryUserResponse libraryUserResponse = new LibraryUserResponse();
        libraryUserResponse.set(libraryUser);

        return new ResponseEntity<>(libraryUserResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}/games/{gamesId}")
    public ResponseEntity<Response<?>> updateUser(@PathVariable int userId, @PathVariable(required = false) int gamesId, @RequestBody LibraryUser libraryUser){
        LibraryUser libraryUserToUpdate = this.libraryUserRepository.findById(userId).orElse(null);
        Game game = this.gameRepository.findById(gamesId).orElse(null);

        if (libraryUserToUpdate == null){
            ErrorResponse error = new ErrorResponse();
            error.set("No user with that id to update");

            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        libraryUserToUpdate.setFirstName(libraryUser.getFirstName());
        libraryUserToUpdate.setLastName(libraryUser.getLastName());
        libraryUserToUpdate.setEmail(libraryUser.getEmail());
        libraryUserToUpdate.setPhone(libraryUser.getPhone());

        if (game != null){
            game.setLibraryUser(libraryUser);
            libraryUserToUpdate.getGames().add(game);
        }

        this.libraryUserRepository.save(libraryUserToUpdate);

        LibraryUserResponse libraryUserResponse = new LibraryUserResponse();
        libraryUserResponse.set(libraryUserToUpdate);

        return new ResponseEntity<>(libraryUserResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteUser(@PathVariable int id){
        LibraryUser libraryUserToRemove = this.libraryUserRepository.findById(id).orElse(null);

        if (libraryUserToRemove == null){
            ErrorResponse error = new ErrorResponse();
            error.set("No user with that id found");

            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        this.libraryUserRepository.delete(libraryUserToRemove);

        LibraryUserResponse libraryUserResponse = new LibraryUserResponse();
        libraryUserResponse.set(libraryUserToRemove);

        return ResponseEntity.ok(libraryUserResponse);
    }
}