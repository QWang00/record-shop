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

            albumRepository.save(new Album(null, "Oasis", 1994, Album.AlbumGenres.BRITPOP, "Definitely Maybe","https://album-images-bucket.s3.eu-west-2.amazonaws.com/Definitely+Maybe.jpeg" ));
            albumRepository.save(new Album(null, "Oasis", 1995, Album.AlbumGenres.BRITPOP, "What's the Story Morning Glory?", "https://album-images-bucket.s3.eu-west-2.amazonaws.com/What's+the+Story+Morning+Glory%3F%22.jpeg"));
            albumRepository.save(new Album(null, "The Beatles", 1968, Album.AlbumGenres.ROCK, "The White Album", "https://album-images-bucket.s3.eu-west-2.amazonaws.com/The+White+Album.jpeg"));
            albumRepository.save(new Album(null, "The Beatles", 1969, Album.AlbumGenres.ROCK, "Abbey Road", "https://album-images-bucket.s3.eu-west-2.amazonaws.com/Abbey+Road.jpeg"));
            albumRepository.save(new Album(null, "Suede", 1996, Album.AlbumGenres.BRITPOP, "Coming Up", "https://album-images-bucket.s3.eu-west-2.amazonaws.com/Coming+Up.jpeg"));
            albumRepository.save(new Album(null, "Suede", 1994, Album.AlbumGenres.BRITPOP, "Dog Man Star", "https://album-images-bucket.s3.eu-west-2.amazonaws.com/Dog+Man+Star.jpeg"));
            albumRepository.save(new Album(null, "Muse", 2006, Album.AlbumGenres.ALTERNATIVE_ROCK, "Black Holes and Revelations", "https://album-images-bucket.s3.eu-west-2.amazonaws.com/Black+Holes+and+Revelations.jpeg"));
            albumRepository.save(new Album(null, "Muse", 2003, Album.AlbumGenres.ALTERNATIVE_ROCK, "Absolution", "https://album-images-bucket.s3.eu-west-2.amazonaws.com/Absolution.jpeg"));
            albumRepository.save(new Album(null, "Muse", 2001, Album.AlbumGenres.ALTERNATIVE_ROCK, "Origin of Symmetry", "https://album-images-bucket.s3.eu-west-2.amazonaws.com/Origin+of+Symmetry.jpeg"));

            albumRepository.save(new Album(null, "Blur", 1994, Album.AlbumGenres.BRITPOP, "Parklife", "https://album-images-bucket.s3.eu-west-2.amazonaws.com/Parklife.jpeg"));
            albumRepository.save(new Album(null, "Pulp", 1994, Album.AlbumGenres.BRITPOP, "His 'n' Hers", "https://album-images-bucket.s3.eu-west-2.amazonaws.com/His+%27n%27+Hers.jpeg"));
            albumRepository.save(new Album(null, "Oasis", 1998, Album.AlbumGenres.BRITPOP, "The Masterplan", "https://album-images-bucket.s3.eu-west-2.amazonaws.com/The+Masterplan.jpeg"));
            albumRepository.save(new Album(null, "Radiohead", 1995, Album.AlbumGenres.ALTERNATIVE_ROCK, "The Bends","https://album-images-bucket.s3.eu-west-2.amazonaws.com/The+Bends.jpeg" ));
            albumRepository.save(new Album(null, "Oasis", 1997, Album.AlbumGenres.BRITPOP, "Be Here Now", "https://album-images-bucket.s3.eu-west-2.amazonaws.com/Be+Here+Now.jpeg"));

            System.out.println("Demo data has been loaded.");
        };
    }
}
