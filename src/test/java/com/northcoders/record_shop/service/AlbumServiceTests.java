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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
    void testGetAlbumById_IdFound(){
        Album album1 = new Album(1L, "Oasis", 1994, Album.AlbumGenres.BRITPOP, "Definitely Maybe");

        when(mockAlbumRepository.findById(1L)).thenReturn(Optional.of(album1));
        Album actualResult = albumServiceImpl.getAlbumById(1L);
        assertThat(actualResult).isEqualTo(album1);
        assertThat(actualResult.getName().equals("Definitely Maybe"));
        assertThat(actualResult.getArtist().equals("Oasis"));
        assertThat(actualResult.getGenre().equals(Album.AlbumGenres.BRITPOP));
        assertThat(actualResult.getReleaseYear() == 1994);

    }

    @Test
    void testAddAlbum(){
        Album album = new Album(null,"The Beatles" , 1969, Album.AlbumGenres.ROCK,"Abbey Road");
        Album albumWithId = new Album(1L,"The Beatles" , 1969, Album.AlbumGenres.ROCK,"Abbey Road");

        when(mockAlbumRepository.save(album)).thenReturn(albumWithId);

        Album actualResult = albumServiceImpl.addAlbum(album);
        verify(mockAlbumRepository, times(1)).save(album);
        assertThat(actualResult.getId() == 1L);
        assertThat(actualResult.getArtist().equals("The Beatles"));
        assertThat(actualResult.getName().equals("Abbey Road"));
        assertThat(actualResult.getReleaseYear() == 1969);
        assertThat(actualResult.getGenre().equals(Album.AlbumGenres.ROCK));
    }

    @Test
    void testUpdateAlbumById_AlbumIdNotExists(){
        Album album = new Album(100L,"The Beatles" , 1969, Album.AlbumGenres.ROCK,"Abbey Road");

        when(mockAlbumRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> albumServiceImpl.updateAlbumById(100L, album))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("The album with id '100' cannot be found");
        verify(mockAlbumRepository, never()).save(any(Album.class));
    }

    @Test
    void testUpdateAlbumById_AlbumFound(){
        Album albumFound = new Album(100L,"The Beatles" , 1969, Album.AlbumGenres.ROCK,"Abbey Road");
        Album albumUpdated = new Album(100L,"The Beatles!" , 1968, Album.AlbumGenres.BRITPOP,"Abbey Road?");

        when(mockAlbumRepository.findById(100L)).thenReturn(Optional.of(albumFound));
        when(mockAlbumRepository.save(any(Album.class))).thenReturn(albumUpdated);

        Album actualResult = albumServiceImpl.updateAlbumById(100L, albumUpdated);

        assertThat(actualResult.getId() == 100L);
        assertThat(actualResult.getArtist().equals("The Beatles!"));
        assertThat(actualResult.getName().equals("Abbey Road?"));
        assertThat(actualResult.getReleaseYear() == 1968);
        assertThat(actualResult.getGenre().equals(Album.AlbumGenres.BRITPOP));
        verify(mockAlbumRepository).save(albumUpdated);
    }

    @Test
    void testDeleteAlbumById_AlbumIdNotExists(){
        Album album = new Album(100L,"The Beatles" , 1969, Album.AlbumGenres.ROCK,"Abbey Road");

        when(mockAlbumRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> albumServiceImpl.deleteAlbumById(100L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("The album with id '100' cannot be found");
        verify(mockAlbumRepository, never()).delete(any(Album.class));
    }

    @Test
    void testDeleteAlbumById_AlbumFound(){

        Long albumId = 1L;
        Album album = new Album(albumId,"The Beatles" , 1969, Album.AlbumGenres.ROCK,"Abbey Road");
        String expectedMessage = "Album with ID " + albumId + " is deleted successfully.";
        when(mockAlbumRepository.findById(albumId)).thenReturn(Optional.of(album));
        doNothing().when(mockAlbumRepository).deleteById(albumId);

        String actualResult = albumServiceImpl.deleteAlbumById(albumId);

        assertThat(actualResult.equals(expectedMessage ));
        verify(mockAlbumRepository).deleteById(albumId);
    }





}






