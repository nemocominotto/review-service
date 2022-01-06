package fact.it.reviewservice.repository;

import fact.it.reviewservice.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findAllByAuteur(String auteur);
    List<Review> findAllByRouteId(String routeCode);
    List<Review> findAllByAuteurAndRouteId(String auteur, String routeCode);
    Review findReviewById(String id);
}
