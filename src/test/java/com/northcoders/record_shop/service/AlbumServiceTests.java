package com.northcoders.record_shop.service;

import com.northcoders.record_shop.exception.ItemNotFoundException;
import com.northcoders.record_shop.model.Album;
import com.northcoders.record_shop.repository.AlbumRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    void testGetAllAlbums_ReturnsListOfAlbums(){
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

    @Test
    void testGetAlbumById_IdNotExists(){
        Long idNotExists = 100L;
        when(mockAlbumRepository.findById(idNotExists)).thenReturn(Optional.empty());

        assertThatThrownBy(()-> albumServiceImpl.getAlbumById(idNotExists))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage(String.format("The album with id '%s' cannot be found", idNotExists));

    }

    @Test
    void testGetAlbumById_IdExists(){
        Album album1 = new Album(1L, "Oasis", 1994, Album.AlbumGenres.BRITPOP, "Definitely Maybe");

        when(mockAlbumRepository.findById(1L)).thenReturn(Optional.of(album1));
        Album actualResult = albumServiceImpl.getAlbumById(1L);
        assertThat(actualResult).isEqualTo(album1);
        assertThat(actualResult.getName().equals("Definitely Maybe"));
        assertThat(actualResult.getArtist().equals("Oasis"));
        assertThat(actualResult.getGenre().equals(Album.AlbumGenres.BRITPOP));
        assertThat(actualResult.getReleaseYear() == 1994);

    }





}
