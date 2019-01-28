package com.andati.imbank.web;

import com.andati.imbank.domain.*;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProfileController {
    String uri = "https://swapi.co/api/people/";

    @RequestMapping("/profiledetails/{id}")
    public Profile profileDetails(@PathVariable(required = false) String id) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        String url = uri+id+"/";
        Person characterProfile = restTemplate.getForObject(url,Person.class);

        String homeWorld = getHomeWorld(characterProfile.getHomeworld());

        List<String> films= new ArrayList<>();
        for(String film: characterProfile.getFilms()) {
            films.add(getFilm(film));
        }

        List<String> species= new ArrayList<>();
        for(String speci: characterProfile.getSpecies()) {
            species.add(getSpecies(speci));
        }

        List<String> vehicles= new ArrayList<>();
        for(String vehicle: characterProfile.getVehicles()) {
            vehicles.add(getVehicle(vehicle));
        }

        List<String> starships= new ArrayList<>();
        for(String starship: characterProfile.getStarships()) {
            starships.add(getStarship(starship));
        }
        return new Profile(homeWorld,films, species, vehicles, starships);
    }

    private String getHomeWorld(String homeurl) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        HomeWorld homeWorld = restTemplate.getForObject(homeurl,HomeWorld.class);
        return homeWorld.getName();
    }

    private String getFilm(String filmUrl) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        Film film = restTemplate.getForObject(filmUrl,Film.class);
        return film.getTitle();
    }

    private String getSpecies(String speciesUrl) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        Species Speci = restTemplate.getForObject(speciesUrl,Species.class);
        return Speci.getName();
    }

    private String getVehicle(String vehicleUrl) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        Vehicle vehicle = restTemplate.getForObject(vehicleUrl,Vehicle.class);
        return vehicle.getName();
    }

    private String getStarship(String starshipUrl) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        Starship starship = restTemplate.getForObject(starshipUrl,Starship.class);
        return starship.getName();
    }

}
