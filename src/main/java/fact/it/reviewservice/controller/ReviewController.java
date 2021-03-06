package fact.it.reviewservice.controller;

import fact.it.reviewservice.model.Review;
import fact.it.reviewservice.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ReviewController {

    @PostConstruct
    public void fillDB(){
        if(reviewRepository.count()==0){
            reviewRepository.save(new Review("Nemo Cominotto", "Leopoldsburg - hasselt", "een goede rit", "123"));
            reviewRepository.save(new Review("Luca Cominotto", "Heppen - Leopodsburg centrum", "een goede rit maar ver", "123"));
            reviewRepository.save(new Review("Fira Bausmans", "Hasselt - Stevoort", "een toffe rit en niet te ver", "456"));
        }
//        System.out.println("Review test" + reviewRepository.findAllByAuteur("Nemo Cominotto").size());
    }

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping("/reviews")
    public List<Review> all() {
        return reviewRepository.findAll();
    }


    @GetMapping("/reviews/auteur/{auteur}")
    public List<Review> getAllByAuteur(@PathVariable String auteur){
        return reviewRepository.findAllByAuteur(auteur);
    }

    @GetMapping("/reviews/code/{routeCode}")
    public List<Review> getAllByRouteCode(@PathVariable String routeCode){
        return reviewRepository.findAllByRouteId(routeCode);
    }

    @GetMapping("/reviews/auteurs/{auteur}/routeCode/{routeCode}")
    public List<Review> getAllReviewsByCodeAndAuteur(@PathVariable String auteur, @PathVariable String routeCode){
        return reviewRepository.findAllByAuteurAndRouteId(auteur, routeCode);
    }

    @PostMapping("/reviews")
    public Review addReview(@RequestBody Review review){
        reviewRepository.save(review);
        return review;
    }

    @PutMapping("/reviews")
    public Review updateReview(@RequestBody Review updateReview){
        Review newReview = reviewRepository.findReviewById(updateReview.getId());

        newReview.setContent(updateReview.getContent());
        newReview.setRouteId(updateReview.getRouteId());
        newReview.setTitel(updateReview.getTitel());
        newReview.setAuteur(updateReview.getAuteur());

        reviewRepository.save(newReview);

        return newReview;
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity deleteReview(@PathVariable String id){
        Review review = reviewRepository.findReviewById(id);
        if(review!=null){
            reviewRepository.delete(review);
        }

        return ResponseEntity.ok().build();
    }
}
