package com.andati.imbank.domain;

import java.util.List;

public class Profile {
    private String homeWorld;
    private List<String> films;
    private List<String> species;
    private List<String> vehicles;
    private List<String> starships;

    public Profile(String homeWorld, List<String> films, List<String> species, List<String> vehicles, List<String> starships) {
        this.homeWorld = homeWorld;
        this.films = films;
        this.species = species;
        this.vehicles = vehicles;
        this.starships = starships;
    }

    public List<String> getFilms() {
        return films;
    }

    public void setFilms(List<String> films) {
        this.films = films;
    }

    public String getHomeWorld() {
        return homeWorld;
    }

    public void setHomeWorld(String homeWorld) {
        this.homeWorld = homeWorld;
    }

    public List<String> getSpecies() {
        return species;
    }

    public void setSpecies(List<String> species) {
        this.species = species;
    }

    public List<String> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<String> vehicles) {
        this.vehicles = vehicles;
    }

    public List<String> getStarships() {
        return starships;
    }

    public void setStarships(List<String> starships) {
        this.starships = starships;
    }
}
