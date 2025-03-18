package z01dakar.lets_play.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import z01dakar.lets_play.models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<List<Product>> findAllByUserId(String userId);
}
