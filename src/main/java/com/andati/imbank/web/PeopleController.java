package com.andati.imbank.web;

import com.andati.imbank.domain.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PeopleController {

    String uri = "https://swapi.co/api/people/";

    @GetMapping(path= {"/", "/{page}"})
    public String homePage(@PathVariable(required = false) String page, Model model, HttpSession session) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        String url = (page == null) ? uri : uri+"?page="+page;
        if(page!=null && !StringUtils.isNumeric(page)) {
            return "index";
        }
        People starWarsCharacters = restTemplate.getForObject(url,People.class);

        String next = starWarsCharacters.getNext();
        String nextPage;
        String currentPage;
        String previousPage;
        if (next != null) {
            nextPage = next.substring(next.lastIndexOf('=')+1);
            currentPage = String.valueOf(Integer.parseInt(nextPage)-1);
            previousPage = String.valueOf(Integer.parseInt(nextPage)-2);
        }
        else {
            nextPage = "0";
            String previous = starWarsCharacters.getPrevious();
            previousPage = previous.substring(previous.lastIndexOf('=')+1);
            currentPage = String.valueOf(Integer.parseInt(previousPage)+1);
        }

        model.addAttribute("previousPage", previousPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("starWarsCharacters", starWarsCharacters.getResults());

        List<String> favoriteCharacters = getFavCharacters(session);
        model.addAttribute("favoriteCharacters", favoriteCharacters);

        return "index";
    }

    @GetMapping(path= {"/profile", "/profile/{id}"})
    public String profilePage(@PathVariable(required = false) String id, Model model, HttpSession session) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        String url = uri+id+"/";
        if(id!=null && !StringUtils.isNumeric(id)) {
            return "profile";
        }
        if(id==null) {
            //return 404
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile Not Found");
        }
        Person characterProfile = restTemplate.getForObject(url,Person.class);
        model.addAttribute("characterProfile", characterProfile);
        model.addAttribute("name", characterProfile.getName());
        model.addAttribute("height", characterProfile.getHeight());
        model.addAttribute("mass", characterProfile.getMass());
        model.addAttribute("hair_color", characterProfile.getHairColor());
        model.addAttribute("skin_color", characterProfile.getSkinColor());
        model.addAttribute("eye_color", characterProfile.getEyeColor());
        model.addAttribute("birth_year", characterProfile.getBirthYear());
        model.addAttribute("gender", characterProfile.getGender());

        model.addAttribute("profile_id", id);

        List<String> favoriteCharacters = getFavCharacters(session);
        model.addAttribute("favoriteCharacters", favoriteCharacters);
        return "profile";
    }

    @PostMapping("/add_favourite")
    public String addFavourite (@RequestParam("name") String name, HttpServletRequest request, Model model) {
        List<String> favoriteCharacters = getFavCharacters(request.getSession());

        if(favoriteCharacters.size() >=5) {
            model.addAttribute("comment", "You can only have up to 5 favourite characters.");
            return "fav";
        }
        if (!StringUtils.isEmpty(name)) {
            favoriteCharacters.add(name);
            request.getSession().setAttribute("favoriteCharacters", favoriteCharacters);
        }
        model.addAttribute("comment", "Character Added to Favourites Successfully.");
        return "fav";
    }

    @PostMapping("/remove_favourite")
    public String removeFavourite (@RequestParam("name") String name, HttpServletRequest request, Model model) {
        List<String> favoriteCharacters = getFavCharacters(request.getSession());
        if (!StringUtils.isEmpty(name)) {
            favoriteCharacters.remove(name);
            request.getSession().setAttribute("favoriteCharacters", favoriteCharacters);
        }
        model.addAttribute("comment", "Character Removed from Favourites Successfully.");
        return "fav";
    }

    private List<String> getFavCharacters(HttpSession session) {
        List<String> favoriteCharacters = (List<String>) session.getAttribute("favoriteCharacters");
        if (favoriteCharacters == null) {
            favoriteCharacters = new ArrayList<>();
        }
        return favoriteCharacters;
    }
}
