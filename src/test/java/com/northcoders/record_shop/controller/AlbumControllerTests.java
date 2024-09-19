package com.northcoders.record_shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.northcoders.record_shop.exception.GlobalExceptionHandler;
import com.northcoders.record_shop.exception.ItemNotFoundException;
import com.northcoders.record_shop.model.Album;
import com.northcoders.record_shop.service.AlbumServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
@SpringBootTest
@AutoConfigureMockMvc
public class AlbumControllerTests {

    @Mock
    private AlbumServiceImpl mockAlbumServiceImpl;

    @InjectMocks
    private AlbumController albumController;

    @Autowired
    private MockMvc mockMvcController;

    private ObjectMapper mapper;

    @BeforeEach
    public void setup(){
        mockMvcController = MockMvcBuilders.standaloneSetup(albumController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        mapper = new ObjectMapper();
    }

    @Test
    void testGetAllAlbums() throws Exception{
        List<Album> albums = new ArrayList<>();
        Album album1 = new Album(1L, "Oasis", 1994, Album.AlbumGenres.BRITPOP, "Definitely Maybe","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");
        Album album2 = new Album(2L, "Oasis", 1995, Album.AlbumGenres.BRITPOP, "What's the Story Morning Glory?","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");
        albums.add(album1);
        albums.add(album2);

        when(mockAlbumServiceImpl.getAllAlbums()).thenReturn(albums);
        this.mockMvcController.perform(
                MockMvcRequestBuilders.get("/albums/all"))
                .andExpect((status().isOk()))
                .andExpect((MockMvcResultMatchers.jsonPath("$[0].id").value(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Definitely Maybe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].artist").value("Oasis"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].releaseYear").value(1994))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("What's the Story Morning Glory?"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].artist").value("Oasis"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].releaseYear").value(1995));
    }

    @Test
    void testGetAlbumById_IdExists() throws Exception{
        Album album1 = new Album(1L, "Oasis", 1994, Album.AlbumGenres.BRITPOP, "Definitely Maybe","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");

        when(mockAlbumServiceImpl.getAlbumById(1L)).thenReturn(album1);
        this.mockMvcController.perform(
                MockMvcRequestBuilders.get("/albums/1"))
                .andExpect((status().isOk()))
                .andExpect((MockMvcResultMatchers.jsonPath("$.id").value(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Definitely Maybe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.artist").value("Oasis"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseYear").value(1994));


    }

    @Test
    void testGetAlbumById_IdNotExists() throws Exception{
        Long idNotExists = 100L;
        String errorMessage = String.format("The album with id %d cannot be found", idNotExists);

        when(mockAlbumServiceImpl.getAlbumById(idNotExists)).thenThrow(new ItemNotFoundException(errorMessage));

        this.mockMvcController.perform(MockMvcRequestBuilders.get("/albums/" + idNotExists))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(errorMessage));
    }

    @Test
    void testAddAlbum() throws Exception{
        Album album = new Album(null,"The Beatles" , 1969, Album.AlbumGenres.ROCK,"Abbey Road","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");
        Album albumWithId = new Album(1L,"The Beatles" , 1969, Album.AlbumGenres.ROCK,"Abbey Road","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");

        when(mockAlbumServiceImpl.addAlbum(album)).thenReturn(albumWithId);
        this.mockMvcController.perform(MockMvcRequestBuilders.post("/albums")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(album)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.artist").value("The Beatles"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Abbey Road"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseYear").value(1969))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(Album.AlbumGenres.ROCK.toString()));

    }

    @Test
    void testUpdateAlbumById_AlbumIdNotFound() throws Exception{
        Long idNotExists = 100L;
        String errorMessage = String.format("The album with id %d cannot be found", idNotExists);
        Album album = new Album(idNotExists,"The Beatles" , 1969, Album.AlbumGenres.ROCK,"Abbey Road","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");

        when(mockAlbumServiceImpl.updateAlbumById(idNotExists, album)).thenThrow(new ItemNotFoundException(errorMessage));
        this.mockMvcController.perform(MockMvcRequestBuilders.put("/albums/" + idNotExists)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(album)))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(errorMessage));
    }

    @Test
    void testUpdateAlbumById_AlbumFound() throws Exception{
        Album albumFound = new Album(1L,"The Beatles?" , 1968, Album.AlbumGenres.BRITPOP,"Abbey Road!","Definitely Maybe");
        Album albumUpdated= new Album(1L,"The Beatles" , 1969, Album.AlbumGenres.ROCK,"Abbey Road","Definitely Maybe");

        when(mockAlbumServiceImpl.updateAlbumById(eq(1L), any(Album.class))).thenReturn(albumUpdated);
        this.mockMvcController.perform(MockMvcRequestBuilders.put("/albums/1")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(albumUpdated)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.artist").value("The Beatles"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Abbey Road"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseYear").value(1969))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(Album.AlbumGenres.ROCK.toString()));
    }

    @Test
    void testDeleteAlbumById_AlbumFound() throws Exception{
        Long albumId = 1L;
        String expectedMessage = "Album with ID " + albumId + " is deleted successfully.";
        when(mockAlbumServiceImpl.deleteAlbumById(albumId)).thenReturn(expectedMessage);

        this.mockMvcController.perform(MockMvcRequestBuilders.delete("/albums/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedMessage));
    }

    @Test
    void testDeleteAlbumById_AlbumIdNotExists() throws Exception{
        Long idNotExists = 100L;
        String errorMessage = String.format("The album with id %d cannot be found", idNotExists);
        when(mockAlbumServiceImpl.deleteAlbumById(idNotExists)).thenThrow(new ItemNotFoundException(errorMessage));
        this.mockMvcController.perform(MockMvcRequestBuilders.delete("/albums/100"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorMessage));
    }

    @Test
    void testGetAlbumsByArtist_ArtistNotFound() throws Exception {
        String artistNotExists = "John";
        String errorMessage = String.format("Cannot find albums for artist '%s'.", artistNotExists);

        when(mockAlbumServiceImpl.getAlbumsByArtist(artistNotExists)).thenThrow(new ItemNotFoundException(errorMessage));

        this.mockMvcController.perform(MockMvcRequestBuilders.get("/albums/by-artist?artist=John"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorMessage));

    }

    @Test
    void testGetAlbumsByArtist_ArtistFound() throws Exception {
        List<Album> albums = new ArrayList<>();
        Album album1 = new Album(1L, "Oasis", 1994, Album.AlbumGenres.BRITPOP, "Definitely Maybe", "https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");
        Album album2 = new Album(2L, "Oasis", 1995, Album.AlbumGenres.BRITPOP, "What's the Story Morning Glory?", "https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");
        albums.add(album1);
        albums.add(album2);
        when(mockAlbumServiceImpl.getAlbumsByArtist("Oasis")).thenReturn(albums);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.get("/albums/by-artist?artist=Oasis"))
                .andExpect((status().isOk()))
                .andExpect((MockMvcResultMatchers.jsonPath("$[0].id").value(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Definitely Maybe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].artist").value("Oasis"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].releaseYear").value(1994))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].genre").value(Album.AlbumGenres.BRITPOP.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("What's the Story Morning Glory?"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].artist").value("Oasis"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].genre").value(Album.AlbumGenres.BRITPOP.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].releaseYear").value(1995));

    }

    @Test
    void testGetAlbumsByReleaseYear_YearNotFound() throws Exception {
        int releaseYearNotExists = 1000;
        String errorMessage = String.format("Cannot find albums released in year '%s'.", releaseYearNotExists);

        when(mockAlbumServiceImpl.getAlbumsByReleaseYear(releaseYearNotExists)).thenThrow(new ItemNotFoundException(errorMessage));

        this.mockMvcController.perform(MockMvcRequestBuilders.get("/albums/by-release-year?releaseYear=1000"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorMessage));

    }

    @Test
    void testGetAlbumsByReleaseYear_YearFound() throws Exception {
        List<Album> albums = new ArrayList<>();
        Album album1 = new Album(1L, "Oasis", 1995, Album.AlbumGenres.BRITPOP, "Definitely Maybe","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");
        Album album2 = new Album(2L, "Oasis", 1995, Album.AlbumGenres.BRITPOP, "What's the Story Morning Glory?","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");
        albums.add(album1);
        albums.add(album2);
        when(mockAlbumServiceImpl.getAlbumsByReleaseYear(1995)).thenReturn(albums);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.get("/albums/by-release-year?releaseYear=1995"))
                .andExpect((status().isOk()))
                .andExpect((MockMvcResultMatchers.jsonPath("$[0].id").value(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Definitely Maybe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].artist").value("Oasis"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].releaseYear").value(1995))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].genre").value(Album.AlbumGenres.BRITPOP.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("What's the Story Morning Glory?"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].artist").value("Oasis"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].genre").value(Album.AlbumGenres.BRITPOP.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].releaseYear").value(1995));

    }

    @Test
    void testGetAlbumsByGenre_AlbumsFoundInThisGenre() throws Exception {
        List<Album> albums = new ArrayList<>();
        Album album1 = new Album(1L, "Oasis", 1995, Album.AlbumGenres.BRITPOP, "Definitely Maybe","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");
        Album album2 = new Album(2L, "Oasis", 1995, Album.AlbumGenres.BRITPOP, "What's the Story Morning Glory?","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");
        albums.add(album1);
        albums.add(album2);
        when(mockAlbumServiceImpl.getAlbumsByGenre(Album.AlbumGenres.BRITPOP)).thenReturn(albums);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.get("/albums/by-genre?genre=BRITPOP"))
                .andExpect((status().isOk()))
                .andExpect((MockMvcResultMatchers.jsonPath("$[0].id").value(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Definitely Maybe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].artist").value("Oasis"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].releaseYear").value(1995))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].genre").value(Album.AlbumGenres.BRITPOP.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("What's the Story Morning Glory?"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].artist").value("Oasis"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].genre").value(Album.AlbumGenres.BRITPOP.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].releaseYear").value(1995));

    }

    @Test
    void testGetAlbumsByGenre_GenreExistsButNoAlbumsInThisGenre() throws Exception {
        String genre = "ROCK";
        String errorMessage = String.format("Cannot find albums in '%s'.", genre);

        when(mockAlbumServiceImpl.getAlbumsByGenre(Album.AlbumGenres.ROCK))
                .thenThrow(new ItemNotFoundException(errorMessage));

        this.mockMvcController.perform(MockMvcRequestBuilders.get("/albums/by-genre?genre=ROCK"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(errorMessage));
    }

    @Test
    void testGetAlbumsByGenre_GenreNotRecognised() throws Exception {
        String invalidGenre = "HIPPOP";
        String errorMessage = String.format("Genre '%s' is not recognized.", invalidGenre);

        this.mockMvcController.perform(MockMvcRequestBuilders.get("/albums/by-genre?genre=HIPPOP")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(errorMessage));
    }

    @Test
    void testGetAlbumsByName_NameNotFound() throws Exception {
        String nameNotExists = "Not";
        String errorMessage = String.format("Cannot find albums with the name of '%s'.", nameNotExists);

        when(mockAlbumServiceImpl.getAlbumsByName(nameNotExists)).thenThrow(new ItemNotFoundException(errorMessage));

        this.mockMvcController.perform(MockMvcRequestBuilders.get("/albums/by-name?name=Not"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorMessage));

    }

    @Test
    void testGetAlbumsByName_NameFound() throws Exception {
        List<Album> albums = new ArrayList<>();
        Album album1 = new Album(1L, "Oasis", 1999, Album.AlbumGenres.BRITPOP, "Definitely Maybe","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");
        Album album2 = new Album(2L, "Oasis", 1995, Album.AlbumGenres.BRITPOP, "Definitely Maybe","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg");
        albums.add(album1);
        albums.add(album2);
        when(mockAlbumServiceImpl.getAlbumsByName("Definitely Maybe")).thenReturn(albums);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.get("/albums/by-name?name=Definitely Maybe"))
                .andExpect((status().isOk()))
                .andExpect((MockMvcResultMatchers.jsonPath("$[0].id").value(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Definitely Maybe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].artist").value("Oasis"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].releaseYear").value(1999))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].genre").value(Album.AlbumGenres.BRITPOP.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Definitely Maybe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].artist").value("Oasis"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].genre").value(Album.AlbumGenres.BRITPOP.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].releaseYear").value(1995));

    }


}
