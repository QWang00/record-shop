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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
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
        Album album1 = new Album(1L, "Oasis", 1994, Album.AlbumGenres.BRITPOP, "Definitely Maybe");
        Album album2 = new Album(2L, "Oasis", 1995, Album.AlbumGenres.BRITPOP, "What's the Story Morning Glory?");
        albums.add(album1);
        albums.add(album2);

        when(mockAlbumServiceImpl.getAllAlbums()).thenReturn(albums);
        this.mockMvcController.perform(
                MockMvcRequestBuilders.get("/albums"))
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
        Album album1 = new Album(1L, "Oasis", 1994, Album.AlbumGenres.BRITPOP, "Definitely Maybe");

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


}
