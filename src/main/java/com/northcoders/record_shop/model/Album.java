package com.northcoders.record_shop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Album {

    public enum AlbumGenres {
        BRITPOP,
        ROCK,
        ALTERNATIVE_ROCK,
        CLASSIC
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    Long id;

    @Column
    String artist;

    @Column
    int releaseYear;

    @Enumerated(EnumType.STRING)
    @Column
    AlbumGenres genre;

    @Column
    String name;

    @Column
    String imageUrl;
}
