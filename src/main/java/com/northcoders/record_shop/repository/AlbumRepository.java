package com.northcoders.record_shop.repository;

import com.northcoders.record_shop.model.Album;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends CrudRepository<Album, Long> {
    List<Album> findByArtist(String artist);

    List<Album> findByReleaseYear(int releaseYear);

    List<Album> findByGenre(Album.AlbumGenres genre);

    List<Album> findByName(String name);
}
