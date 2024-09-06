package com.northcoders.record_shop.service;

import com.northcoders.record_shop.model.Album;

import java.util.List;

public interface AlbumService {
    List<Album> getAllAlbums();

    Album getAlbumById(Long id);

    Album addAlbum(Album album);

    Album updateAlbumById(Long id, Album album);

    String deleteAlbumById(Long id);

    List<Album> getAlbumsByArtist(String artist);

    List<Album> getAlbumsByReleaseYear(int releaseYear);
}
