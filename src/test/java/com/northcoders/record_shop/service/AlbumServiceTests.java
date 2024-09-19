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
        Album album1 = new Album(null, "Oasis", 1994, Album.AlbumGenres.BRITPOP, "Definitely Maybe","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");
        Album album2 = new Album(null, "Oasis", 1995, Album.AlbumGenres.BRITPOP, "What's the Story Morning Glory?","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");
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
        Album album1 = new Album(1L, "Oasis", 1994, Album.AlbumGenres.BRITPOP, "Definitely Maybe","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");

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
        Album album = new Album(null,"The Beatles" , 1969, Album.AlbumGenres.ROCK,"Abbey Road","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");
        Album albumWithId = new Album(1L,"The Beatles" , 1969, Album.AlbumGenres.ROCK,"Abbey Road","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");

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
        Album album = new Album(100L,"The Beatles" , 1969, Album.AlbumGenres.ROCK,"Abbey Road","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");

        when(mockAlbumRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> albumServiceImpl.updateAlbumById(100L, album))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("The album with id '100' cannot be found");
        verify(mockAlbumRepository, never()).save(any(Album.class));
    }

    @Test
    void testUpdateAlbumById_AlbumFound(){
        Album albumFound = new Album(100L,"The Beatles" , 1969, Album.AlbumGenres.ROCK,"Abbey Road","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");
        Album albumUpdated = new Album(100L,"The Beatles!" , 1968, Album.AlbumGenres.BRITPOP,"Abbey Road?","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");

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
        Album album = new Album(100L,"The Beatles" , 1969, Album.AlbumGenres.ROCK,"Abbey Road","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");

        when(mockAlbumRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> albumServiceImpl.deleteAlbumById(100L))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage("The album with id '100' cannot be found");
        verify(mockAlbumRepository, never()).delete(any(Album.class));
    }

    @Test
    void testDeleteAlbumById_AlbumFound(){

        Long albumId = 1L;
        Album album = new Album(albumId,"The Beatles" , 1969, Album.AlbumGenres.ROCK,"Abbey Road","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");
        String expectedMessage = "Album with ID " + albumId + " is deleted successfully.";
        when(mockAlbumRepository.findById(albumId)).thenReturn(Optional.of(album));
        doNothing().when(mockAlbumRepository).deleteById(albumId);

        String actualResult = albumServiceImpl.deleteAlbumById(albumId);

        assertThat(actualResult.equals(expectedMessage ));
        verify(mockAlbumRepository).deleteById(albumId);
    }

    @Test
    void testGetAlbumsByArtist_ArtistNotFound(){
        String artistNotExists = "John Doe";
        when(mockAlbumRepository.findByArtist(artistNotExists)).thenReturn(new ArrayList<>());

        assertThatThrownBy(()-> albumServiceImpl.getAlbumsByArtist(artistNotExists))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage(String.format("Cannot find albums for artist '%s'.", artistNotExists));

    }

    @Test
    void testGetAlbumsByArtist_ArtistFound(){
        List<Album> albums = new ArrayList<>();
        Album album1 = new Album(1L, "Oasis", 1994, Album.AlbumGenres.BRITPOP, "Definitely Maybe","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");
        Album album2 = new Album(null, "Oasis", 1995, Album.AlbumGenres.BRITPOP, "What's the Story Morning Glory?","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");
        albums.add(album1);
        albums.add(album2);
        when(mockAlbumRepository.findByArtist("Oasis")).thenReturn(albums);
        List<Album> actualResult = albumServiceImpl.getAlbumsByArtist("Oasis");
        assertThat(actualResult).isEqualTo(albums);
        assertThat(actualResult).hasSize(2);
        assertThat(actualResult.get(0).getName().equals("Definitely Maybe"));
        assertThat(actualResult.get(0).getArtist().equals("Oasis"));
        assertThat(actualResult.get(0).getGenre().equals(Album.AlbumGenres.BRITPOP));
        assertThat(actualResult.get(0).getReleaseYear() == 1994);
        assertThat(actualResult.get(1).getName().equals("What's the Story Morning Glory?"));
        assertThat(actualResult.get(1).getArtist().equals("Oasis"));
        assertThat(actualResult.get(1).getGenre().equals(Album.AlbumGenres.BRITPOP));
        assertThat(actualResult.get(1).getReleaseYear() == 1995);

    }

    @Test
    void testGetAlbumsByReleaseYear_ReleaseYearNotFound(){
        int releaseYearNotExists = 1000;
        when(mockAlbumRepository.findByReleaseYear(releaseYearNotExists)).thenReturn(new ArrayList<>());

        assertThatThrownBy(()-> albumServiceImpl.getAlbumsByReleaseYear(releaseYearNotExists))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage(String.format("Cannot find albums released in year '%s'.", releaseYearNotExists));

    }

    @Test
    void testGetAlbumsByReleaseYear_ReleaseYearFound(){
        List<Album> albums = new ArrayList<>();
        Album album1 = new Album(1L, "Oasis", 1995, Album.AlbumGenres.BRITPOP, "Definitely Maybe","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");
        Album album2 = new Album(2L, "Oasis", 1995, Album.AlbumGenres.BRITPOP, "What's the Story Morning Glory?","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");
        albums.add(album1);
        albums.add(album2);
        when(mockAlbumRepository.findByReleaseYear(1995)).thenReturn(albums);
        List<Album> actualResult = albumServiceImpl.getAlbumsByReleaseYear(1995);
        assertThat(actualResult).isEqualTo(albums);
        assertThat(actualResult).hasSize(2);
        assertThat(actualResult.get(0).getName().equals("Definitely Maybe"));
        assertThat(actualResult.get(0).getArtist().equals("Oasis"));
        assertThat(actualResult.get(0).getGenre().equals(Album.AlbumGenres.BRITPOP));
        assertThat(actualResult.get(0).getReleaseYear() == 1995);
        assertThat(actualResult.get(1).getName().equals("What's the Story Morning Glory?"));
        assertThat(actualResult.get(1).getArtist().equals("Oasis"));
        assertThat(actualResult.get(1).getGenre().equals(Album.AlbumGenres.BRITPOP));
        assertThat(actualResult.get(1).getReleaseYear() == 1995);

    }

    @Test
    void testGetAlbumsByGenre_NoAlbumsInThisGenre(){
        Album.AlbumGenres genreWithNoAlbums= Album.AlbumGenres.CLASSIC;
        when(mockAlbumRepository.findByGenre(genreWithNoAlbums)).thenReturn(new ArrayList<>());

        assertThatThrownBy(()-> albumServiceImpl.getAlbumsByGenre(genreWithNoAlbums))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage(String.format("Cannot find albums in '%s'.", genreWithNoAlbums));

    }

    @Test
    void testGetAlbumsByGenre_AlbumsFound(){
        List<Album> albums = new ArrayList<>();
        Album album1 = new Album(1L, "Oasis", 1995, Album.AlbumGenres.BRITPOP, "Definitely Maybe","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");
        Album album2 = new Album(2L, "Oasis", 1994, Album.AlbumGenres.BRITPOP, "What's the Story Morning Glory?","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");
        albums.add(album1);
        albums.add(album2);
        when(mockAlbumRepository.findByGenre(Album.AlbumGenres.BRITPOP)).thenReturn(albums);
        List<Album> actualResult = albumServiceImpl.getAlbumsByGenre(Album.AlbumGenres.BRITPOP);
        assertThat(actualResult).isEqualTo(albums);
        assertThat(actualResult).hasSize(2);
        assertThat(actualResult.get(0).getName().equals("Definitely Maybe"));
        assertThat(actualResult.get(0).getArtist().equals("Oasis"));
        assertThat(actualResult.get(0).getGenre().equals(Album.AlbumGenres.BRITPOP));
        assertThat(actualResult.get(0).getReleaseYear() == 1995);
        assertThat(actualResult.get(1).getName().equals("What's the Story Morning Glory?"));
        assertThat(actualResult.get(1).getArtist().equals("Oasis"));
        assertThat(actualResult.get(1).getGenre().equals(Album.AlbumGenres.BRITPOP));
        assertThat(actualResult.get(1).getReleaseYear() == 1994);

    }

    @Test
    void testGetAlbumsByName_NameNotFound(){
        String nameNotExists = "Not a name";
        when(mockAlbumRepository.findByName(nameNotExists)).thenReturn(new ArrayList<>());

        assertThatThrownBy(()-> albumServiceImpl.getAlbumsByName(nameNotExists))
                .isInstanceOf(ItemNotFoundException.class)
                .hasMessage(String.format("Cannot find albums with the name of '%s'.", nameNotExists));

    }

    @Test
    void testGetAlbumsByName_NameFound(){
        List<Album> albums = new ArrayList<>();
        Album album1 = new Album(1L, "Oasis", 1999, Album.AlbumGenres.BRITPOP, "Definitely Maybe","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");
        Album album2 = new Album(2L, "Oasis", 1995, Album.AlbumGenres.BRITPOP, "Definitely Maybe","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");
        albums.add(album1);
        albums.add(album2);
        when(mockAlbumRepository.findByName("Definitely Maybe")).thenReturn(albums);
        List<Album> actualResult = albumServiceImpl.getAlbumsByName("Definitely Maybe");
        assertThat(actualResult).isEqualTo(albums);
        assertThat(actualResult).hasSize(2);
        assertThat(actualResult.get(0).getName().equals("Definitely Maybe"));
        assertThat(actualResult.get(0).getArtist().equals("Oasis"));
        assertThat(actualResult.get(0).getGenre().equals(Album.AlbumGenres.BRITPOP));
        assertThat(actualResult.get(0).getReleaseYear() == 1999);
        assertThat(actualResult.get(1).getName().equals("Definitely Maybe"));
        assertThat(actualResult.get(1).getArtist().equals("Oasis"));
        assertThat(actualResult.get(1).getGenre().equals(Album.AlbumGenres.BRITPOP));
        assertThat(actualResult.get(1).getReleaseYear() == 1995);

    }



}






