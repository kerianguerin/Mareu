package com.openclassrooms.mareu.model;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;


public class Place {
    /**
     * An array of Meetings Place List items.
     */
    public static final List<Place> FAKE_PLACES = Arrays.asList(
            new Place(1, "Salle A"),
            new Place(2, "Salle B"),
            new Place(3, "Salle C"),
            new Place(4, "Salle D"),
            new Place(5, "Salle E"),
            new Place(6, "Salle F"),
            new Place(7, "Salle G"),
            new Place(8, "Salle H"),
            new Place(9, "Salle I"),
            new Place(10, "Salle J")
    );

    private final Integer id;

    @NonNull
    private final String name;

    private Place(Integer id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }


    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    @NonNull
    public static Place getPlaceById(int id) {
        for (Place place : getAllPlaces()) {
            if (place.id == id)
                return place;
        }
        return null;
    }

    @NonNull
    public static Place[] getAllPlaces() {
        return new Place[]{
                new Place(1, "Salle A"),
                new Place(2, "Salle B"),
                new Place(3, "Salle C"),
                new Place(4, "Salle D"),
                new Place(5, "Salle E"),
                new Place(6, "Salle F"),
                new Place(7, "Salle G"),
                new Place(8, "Salle H"),
                new Place(9, "Salle I"),
                new Place(10, "Salle J")

        };
    }

    @Override
    @NonNull
    public String toString() {
        return getName();
    }
}
