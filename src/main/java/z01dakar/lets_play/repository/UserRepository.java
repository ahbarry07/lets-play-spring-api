package z01dakar.lets_play.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import z01dakar.lets_play.models.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}
