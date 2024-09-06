package com.northcoders.record_shop.service;

import com.northcoders.record_shop.exception.ItemNotFoundException;
import com.northcoders.record_shop.model.Album;
import com.northcoders.record_shop.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AlbumServiceImpl implements AlbumService {
    private final AlbumRepository albumRepository;

    @Autowired
    public AlbumServiceImpl(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    @Override
    public List<Album> getAllAlbums() {
        List<Album> albums = new ArrayList<>();
        albumRepository.findAll().forEach(albums::add);
        return albums;
    }

    @Override
    public Album getAlbumById(Long id) {
        Optional<Album> album = albumRepository.findById(id);
        if (album.isPresent()){
            return album.get();
        }
        throw new ItemNotFoundException(String.format("The album with id '%s' cannot be found", id));
    }

    @Override
    public Album addAlbum(Album album) {
        return albumRepository.save(album);
    }

    @Override
    public Album updateAlbumById(Long id, Album album) {
        Optional<Album> optionalAlbum = albumRepository.findById(id);
        if(optionalAlbum.isEmpty()) throw new ItemNotFoundException(String.format("The album with id '%s' cannot be found", id));
        Album albumFound = optionalAlbum.get();
        albumFound.setArtist(album.getArtist());
        albumFound.setGenre(album.getGenre());
        albumFound.setName(album.getName());
        albumFound.setReleaseYear(album.getReleaseYear());
        return albumRepository.save(albumFound);
    }

    @Override
    public String deleteAlbumById(Long id) {
        Optional<Album> optionalAlbum = albumRepository.findById(id);
        if(optionalAlbum.isEmpty()) throw new ItemNotFoundException(String.format("The album with id '%s' cannot be found", id));
        albumRepository.deleteById(id);
        return ("Album with ID " + id + " is deleted successfully.");
    }


}
