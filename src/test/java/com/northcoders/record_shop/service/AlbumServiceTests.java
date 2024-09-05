package com.northcoders.record_shop.service;

import com.northcoders.record_shop.model.Album;
import com.northcoders.record_shop.repository.AlbumRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AlbumServiceTests {
    @Mock
    private AlbumRepository mockAlbumRepository;

    @InjectMocks
    private AlbumServiceImpl albumServiceImpl;

    @Autowired
    private AlbumRepository albumRepository;

    @Test
    void testGetAllAlbums(){
        List<Album> albums = new ArrayList<>();
        Album album1 = new Album(null, "Oasis", 1994, Album.AlbumGenres.BRITPOP, "Definitely Maybe");
        Album album2 = new Album(null, "Oasis", 1995, Album.AlbumGenres.BRITPOP, "What's the Story Morning Glory?");
        albums.add(album1);
        albums.add(album2);

        when(mockAlbumRepository.findAll()).thenReturn(albums);
        List<Album> actualResult = albumServiceImpl.getAllAlbums();
        assertThat(actualResult).hasSize(2);
        assertThat(actualResult).isEqualTo(albums);

    }


}
