package fact.it.reviewservice;

import fact.it.reviewservice.model.Review;
import fact.it.reviewservice.repository.ReviewRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReviewRepository reviewRepository;


    private Review reviewroute1 = new Review("Nemo Cominotto", "Leopoldsburg - hasselt", "een goede rit", "123");
    private Review reviewroute2 = new Review("Luca Cominotto", "Heppen - Leopodsburg centrum", "een goede rit maar ver", "123");
    private Review reviewroute3 = new Review("Fira Bausmans", "Hasselt - Stevoort", "een toffe rit en niet te ver", "456");
    private Review reviewToBeDeleted = new Review("Nemo Cominotto", "Leopoldsburg - hasselt", "een goede rit", "456");

    @BeforeEach
    public void beforeAllTests() {
        reviewRepository.deleteAll();
        reviewRepository.save(reviewroute1);
        reviewRepository.save(reviewroute2);
        reviewRepository.save(reviewroute3);
        reviewRepository.save(reviewToBeDeleted);
    }


//    @AfterEach
//    public void afterAllTests() {
//        reviewRepository.deleteAll();
//    }


    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void getreviewByAuteurAndRouteID() throws Exception {
        mockMvc.perform(get("/reviews/auteurs/{auteur}/routeCode/{routeCode}", "Luca Cominotto", "123"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].auteur", is("Luca Cominotto")))
                .andExpect(jsonPath("$[0].titel", is("Heppen - Leopodsburg centrum")))
                .andExpect(jsonPath("$[0].content", is("een goede rit maar ver")))
                .andExpect(jsonPath("$[0].routeId", is("123")));


    }

    @Test
    public void getReviewWithRouteCode() throws Exception {

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(reviewroute1);
        reviewList.add(reviewroute2);

        mockMvc.perform(get("/reviews/code/{routecode}", "123"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].auteur", is("Luca Cominotto")))
                .andExpect(jsonPath("$[1].titel", is("Heppen - Leopodsburg centrum")))
                .andExpect(jsonPath("$[1].content", is("een goede rit maar ver")))
                .andExpect(jsonPath("$[1].routeId", is("123")))
                .andExpect(jsonPath("$[0].auteur", is("Nemo Cominotto")))
                .andExpect(jsonPath("$[0].titel", is("Leopoldsburg - hasselt")))
                .andExpect(jsonPath("$[0].content", is("een goede rit")))
                .andExpect(jsonPath("$[0].routeId", is("123")));

    }

    @Test
    public void getReviewByAuteur() throws Exception {

        mockMvc.perform(get("/reviews/auteur/{auteur}", "Luca Cominotto"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].auteur", is("Luca Cominotto")))
                .andExpect(jsonPath("$[0].titel", is("Heppen - Leopodsburg centrum")))
                .andExpect(jsonPath("$[0].content", is("een goede rit maar ver")))
                .andExpect(jsonPath("$[0].routeId", is("123")));
    }

    @Test
    public void PostTest() throws Exception {
        Review review4 = new Review("Jetze Janssens", "Ham", "Toffe rit", "789");

        mockMvc.perform(post("/reviews")
                .content(mapper.writeValueAsString(review4))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.auteur", is("Jetze Janssens")))
                .andExpect(jsonPath("$.titel", is("Ham")))
                .andExpect(jsonPath("$.content", is("Toffe rit")))
                .andExpect(jsonPath("$.routeId", is("789")));
    }
        //Put Test, rare error
//    @Test
//    public void PutTest() throws Exception {
//
//        Review nieuweReview = new Review("Jetze Janssens", "Ham", "Toffe rit met lekker ijsje", "789");
//
//        mockMvc.perform(put("/reviews")
//                .content(mapper.writeValueAsString(nieuweReview))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.auteur", is("Jetze Janssens")))
//                .andExpect(jsonPath("$.titel", is("Ham")))
//                .andExpect(jsonPath("$.content", is("Toffe rit met lekker ijsje")))
//                .andExpect(jsonPath("$.routeId", is("789")));
//
//}
        //Delete test
//    @Test
//    public void DeleteTest() throws Exception {
//
//        mockMvc.perform(delete("/reviews/id/{id}", "61d759420d22e5566c9de14f")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }


    @Test
    public void givenNoReviewGeenStatus() throws Exception {

        mockMvc.perform(delete("/reviews/id/{id}", "61d759420d22e5566c9de14f")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    }
