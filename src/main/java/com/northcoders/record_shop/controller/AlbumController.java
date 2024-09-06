package com.northcoders.record_shop.controller;

import com.northcoders.record_shop.exception.ItemNotFoundException;
import com.northcoders.record_shop.model.Album;
import com.northcoders.record_shop.service.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("albums")
public class AlbumController {
    private final AlbumService albumService;

    @Autowired
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @Operation(summary = "Get all albums", description = "Retrieve all albums from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all albums"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/all")
    public ResponseEntity<List<Album>> getAllAlums() {
        return new ResponseEntity<>(albumService.getAllAlbums(), HttpStatus.OK);
    }

    @Operation(summary = "Get album by ID", description = "Retrieve an album by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved album"),
            @ApiResponse(responseCode = "404", description = "Album not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(albumService.getAlbumById(id), HttpStatus.OK);
    }

    @Operation(summary = "Add a new album", description = "Add a new album to the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Album created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<Album> addAlbum(@RequestBody Album album) {
        Album addedAlbum = albumService.addAlbum(album);
        return new ResponseEntity<>(addedAlbum, HttpStatus.CREATED);
    }

    @Operation(summary = "Update album by ID", description = "Update an existing album by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Album updated successfully"),
            @ApiResponse(responseCode = "404", description = "Album not found"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Album> updateAlbumById(@PathVariable("id") Long id, @RequestBody Album album) {
        return new ResponseEntity<>(albumService.updateAlbumById(id, album), HttpStatus.OK);
    }

    @Operation(summary = "Delete album by ID", description = "Delete an album by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Album deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Album not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAlbumById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(albumService.deleteAlbumById(id), HttpStatus.OK);
    }

    @Operation(summary = "Get albums by artist", description = "Retrieve albums by artist name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved albums by artist"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/by-artist")
    public ResponseEntity<List<Album>> getAlbumsByArtist(@RequestParam String artist) {
        List<Album> albums = albumService.getAlbumsByArtist(artist);
        return new ResponseEntity<>(albums, HttpStatus.OK);
    }

    @Operation(summary = "Get albums by release year", description = "Retrieve albums by release year")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved albums by release year"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/by-release-year")
    public ResponseEntity<List<Album>> getAlbumsByReleaseYear(@RequestParam int releaseYear) {
        List<Album> albums = albumService.getAlbumsByReleaseYear(releaseYear);
        return new ResponseEntity<>(albums, HttpStatus.OK);
    }

    @Operation(summary = "Get albums by genre", description = "Retrieve albums by genre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved albums by genre"),
            @ApiResponse(responseCode = "400", description = "Bad request for invalid genre"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/by-genre")
    public ResponseEntity<List<Album>> getAlbumsByGenre(@RequestParam String genre) {
        try {
            Album.AlbumGenres albumGenre = Album.AlbumGenres.valueOf(genre.toUpperCase());
            List<Album> albums = albumService.getAlbumsByGenre(albumGenre);
            return new ResponseEntity<>(albums, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new ItemNotFoundException(String.format("Genre '%s' is not recognized.", genre));
        }
    }

    @Operation(summary = "Get albums by name", description = "Retrieve albums by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved albums by name"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/by-name")
    public ResponseEntity<List<Album>> getAlbumsByName(@RequestParam String name) {
        List<Album> albums = albumService.getAlbumsByName(name);
        return new ResponseEntity<>(albums, HttpStatus.OK);
    }
}