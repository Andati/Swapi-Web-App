package com.andati.imbank;

import com.andati.imbank.web.PeopleController;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(PeopleController.class)
public class SwapiTest {
    @Autowired
    private MockMvc mvc;

    @LocalServerPort
    private String port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Test
    public void indexPageTest() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void profilePageTest() throws Exception {
        mvc.perform(get("/profile"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void profilePageTest2() throws Exception {
        mvc.perform(get("/profile/1/"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"));
    }

    @Test
    public void addFavouritePageTest() throws Exception {
        mvc.perform(post("/add_favourite").param("name", "Rodgers"))
                .andExpect(status().isOk())
                .andExpect(view().name("fav"));
    }

    @Test
    public void removeFavouritePageTest() throws Exception {
        mvc.perform(post("/add_favourite").param("name","Rodgers"))
                .andExpect(status().isOk())
                .andExpect(view().name("fav"));
    }

    @Test
    public void testRetrieveMoreCharacterDetails() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/profiledetails/35/",
                HttpMethod.GET, entity, String.class);

        String expected = "{\"homeWorld\":\"Naboo\",\"films\":[\"Attack of the Clones\",\"The Phantom Menace\",\"Revenge of the Sith\"],\"species\":[\"Human\"],\"vehicles\":[],\"starships\":[\"H-type Nubian yacht\",\"Naboo star skiff\",\"Naboo fighter\"]}";

        try {
            JSONAssert.assertEquals(expected, response.getBody(), false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
