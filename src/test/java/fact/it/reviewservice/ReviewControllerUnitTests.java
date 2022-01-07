package fact.it.reviewservice;

import fact.it.reviewservice.model.Review;
import fact.it.reviewservice.repository.ReviewRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewRepository reviewRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void GetAllReviews() throws Exception {
        Review reviewroute1 = new Review("Nemo Cominotto", "Leopoldsburg - hasselt", "een goede rit", "123");
        Review reviewroute2 = new Review("Luca Cominotto", "Heppen - Leopodsburg centrum", "een goede rit maar ver", "123");

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(reviewroute1);
        reviewList.add(reviewroute2);

        given(reviewRepository.findAll()).willReturn(reviewList);

        mockMvc.perform(get("/reviews"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].auteur", is("Nemo Cominotto")))
                .andExpect(jsonPath("$[0].titel", is("Leopoldsburg - hasselt")))
                .andExpect(jsonPath("$[0].content", is("een goede rit")))
                .andExpect(jsonPath("$[0].routeId", is("123")))

                .andExpect(jsonPath("$[1].auteur", is("Luca Cominotto")))
                .andExpect(jsonPath("$[1].titel", is("Heppen - Leopodsburg centrum")))
                .andExpect(jsonPath("$[1].content", is("een goede rit maar ver")))
                .andExpect(jsonPath("$[1].routeId", is("123")));
    }

    @Test
    public void GetAllReviewByAuteur() throws Exception {
        Review reviewroute1 = new Review("Nemo Cominotto", "Leopoldsburg - hasselt", "een goede rit", "123");

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(reviewroute1);

        given(reviewRepository.findAllByAuteur("Nemo Cominotto")).willReturn(reviewList);

        mockMvc.perform(get("/reviews/auteur/{auteur}", "Nemo Cominotto"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].auteur", is("Nemo Cominotto")))
                .andExpect(jsonPath("$[0].titel", is("Leopoldsburg - hasselt")))
                .andExpect(jsonPath("$[0].content", is("een goede rit")))
                .andExpect(jsonPath("$[0].routeId", is("123")));

    }

    @Test
    public void GetAllReviewByRouteCode() throws Exception {
        Review reviewroute1 = new Review("Nemo Cominotto", "Leopoldsburg - hasselt", "een goede rit", "123");
        Review reviewroute2 = new Review("Luca Cominotto", "Heppen - Leopodsburg centrum", "een goede rit maar ver", "123");

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(reviewroute1);
        reviewList.add(reviewroute2);

        given(reviewRepository.findAllByRouteId("123")).willReturn(reviewList);

        mockMvc.perform(get("/reviews/code/{routecode}", "123"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].auteur", is("Nemo Cominotto")))
                .andExpect(jsonPath("$[0].titel", is("Leopoldsburg - hasselt")))
                .andExpect(jsonPath("$[0].content", is("een goede rit")))
                .andExpect(jsonPath("$[0].routeId", is("123")))

                .andExpect(jsonPath("$[1].auteur", is("Luca Cominotto")))
                .andExpect(jsonPath("$[1].titel", is("Heppen - Leopodsburg centrum")))
                .andExpect(jsonPath("$[1].content", is("een goede rit maar ver")))
                .andExpect(jsonPath("$[1].routeId", is("123")));

    }

    @Test
    public void GetAllReviewByRouteCodeandAuteur() throws Exception {
        Review reviewroute1 = new Review("Nemo Cominotto", "Leopoldsburg - hasselt", "een goede rit", "123");
        Review reviewroute2 = new Review("Luca Cominotto", "Heppen - Leopodsburg centrum", "een goede rit maar ver", "123");

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(reviewroute1);
        reviewList.add(reviewroute2);

        given(reviewRepository.findAllByAuteurAndRouteId("Nemo Cominotto", "123")).willReturn(reviewList);

        mockMvc.perform(get("/reviews/auteurs/{auteur}/routeCode/{routeCode}", "Nemo Cominotto", "123"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].auteur", is("Nemo Cominotto")))
                .andExpect(jsonPath("$[0].titel", is("Leopoldsburg - hasselt")))
                .andExpect(jsonPath("$[0].content", is("een goede rit")))
                .andExpect(jsonPath("$[0].routeId", is("123")));

    }

    @Test
    public void whenPostReview_thenretunJsonReview() throws Exception{

        Review reviewroute1 = new Review("Jetze Janssens", "Ham", "Toffe rit", "789");

        mockMvc.perform(post("/reviews")
                .content(mapper.writeValueAsString(reviewroute1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.auteur", is("Jetze Janssens")))
                .andExpect(jsonPath("$.titel", is("Ham")))
                .andExpect(jsonPath("$.content", is("Toffe rit")))
                .andExpect(jsonPath("$.routeId", is("789")));

    }
    

    }



