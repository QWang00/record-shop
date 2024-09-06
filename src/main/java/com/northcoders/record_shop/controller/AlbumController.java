package com.northcoders.record_shop.controller;

import com.northcoders.record_shop.exception.ItemNotFoundException;
import com.northcoders.record_shop.model.Album;
import com.northcoders.record_shop.service.AlbumService;
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

    @GetMapping
    public ResponseEntity<List<Album>> getAllAlums() {
        return new ResponseEntity<>(albumService.getAllAlbums(), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(albumService.getAlbumById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Album> addAlbum(@RequestBody Album album) {
        Album addedAlbum = albumService.addAlbum(album);
        return new ResponseEntity<>(addedAlbum, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Album> updateAlbumById(@PathVariable("id") Long id, @RequestBody Album album) {
        return new ResponseEntity<>(albumService.updateAlbumById(id, album), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAlbumById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(albumService.deleteAlbumById(id), HttpStatus.OK);
    }

    @GetMapping(params = "artist")
    public ResponseEntity<List<Album>> getAlbumsByArtist(@RequestParam String artist) {
        List<Album> albums = albumService.getAlbumsByArtist(artist);
        return new ResponseEntity<>(albums, HttpStatus.OK);
    }

    @GetMapping(params = "releaseYear")
    public ResponseEntity<List<Album>> getAlbumsByReleaseYear(@RequestParam int releaseYear){
        List<Album> albums = albumService.getAlbumsByReleaseYear(releaseYear);
        return new ResponseEntity<>(albums, HttpStatus.OK);
    }

    @GetMapping(params = "genre")
    public ResponseEntity<List<Album>> getAlbumsByGenre(@RequestParam String genre){
        try {
            Album.AlbumGenres albumGenre = Album.AlbumGenres.valueOf(genre.toUpperCase());
            List<Album> albums = albumService.getAlbumsByGenre(albumGenre);
            return new ResponseEntity<>(albums, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new ItemNotFoundException(String.format("Genre '%s' is not recognized.", genre));
        }
    }

}
