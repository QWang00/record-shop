package com.northcoders.record_shop.loader;

import com.northcoders.record_shop.model.Album;
import com.northcoders.record_shop.repository.AlbumRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoSeedDataLoader {

    @Bean(name = "demoDataLoader")
    public CommandLineRunner demoDataLoader(AlbumRepository albumRepository) {
        return args -> {
            albumRepository.deleteAll();

            albumRepository.save(new Album(null, "Oasis", 1994, Album.AlbumGenres.BRITPOP, "Definitely Maybe"));
            albumRepository.save(new Album(null, "Oasis", 1995, Album.AlbumGenres.BRITPOP, "What's the Story Morning Glory?"));
            albumRepository.save(new Album(null, "The Beatles", 1968, Album.AlbumGenres.ROCK, "The White Album"));
            albumRepository.save(new Album(null, "The Beatles", 1969, Album.AlbumGenres.ROCK, "Abbey Road"));
            albumRepository.save(new Album(null, "Suede", 1996, Album.AlbumGenres.BRITPOP, "Coming Up"));
            albumRepository.save(new Album(null, "Suede", 1994, Album.AlbumGenres.BRITPOP, "Dog Man Star"));
            albumRepository.save(new Album(null, "Muse", 2006, Album.AlbumGenres.ALTERNATIVE_ROCK, "Black Holes and Revelations"));
            albumRepository.save(new Album(null, "Muse", 2003, Album.AlbumGenres.ALTERNATIVE_ROCK, "Absolution"));
            albumRepository.save(new Album(null, "Muse", 2001, Album.AlbumGenres.ALTERNATIVE_ROCK, "Origin of Symmetry"));

            albumRepository.save(new Album(null, "Blur", 1994, Album.AlbumGenres.BRITPOP, "Parklife"));
            albumRepository.save(new Album(null, "Pulp", 1994, Album.AlbumGenres.BRITPOP, "His 'n' Hers"));
            albumRepository.save(new Album(null, "Oasis", 1998, Album.AlbumGenres.BRITPOP, "The Masterplan"));
            albumRepository.save(new Album(null, "Radiohead", 1995, Album.AlbumGenres.ALTERNATIVE_ROCK, "The Bends"));
            albumRepository.save(new Album(null, "Oasis", 1997, Album.AlbumGenres.BRITPOP, "Be Here Now"));

            System.out.println("Demo data has been loaded.");
        };
    }
}
