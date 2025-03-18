package z01dakar.lets_play.services;


//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import z01dakar.lets_play.exception.NotOwnResourceException;
import z01dakar.lets_play.exception.ResourceAlreadyExistException;
import z01dakar.lets_play.exception.ResourceNotFoundException;
import z01dakar.lets_play.exception.UnAuthorizedException;
import z01dakar.lets_play.models.Role;
import z01dakar.lets_play.repository.UserRepository;
import  z01dakar.lets_play.models.User;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository  userRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public List<User> allUsers(){
        return userRepository.findAll();
    }

    public User showUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("The user with " + email + " doesn't exist"));
    }

    public User createUser(User user){

        user.setId(null);
        user.setRole(Role.USER);

        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        if (optionalUser.isPresent()) // Check if a user has the same email as the one you want to add
            throw new ResourceAlreadyExistException("The " + user.getEmail() + " E-mail is already taken");

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public User bringUpToDateUser(User user, String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty())
            throw new ResourceNotFoundException("The user with " + email + " doesn't exist");

        // Check if the new mail is already taken
        User updatedUser = optionalUser.get();
        Optional<User> existingEmailUser = userRepository.findByEmail(user.getEmail());
        if (existingEmailUser.isPresent())
            throw new ResourceAlreadyExistException("The " + user.getEmail() + " mail is already used");

        //Updating information
        updatedUser.setEmail(user.getEmail());
        updatedUser.setName(user.getName());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(updatedUser);
    }

    public User bringUpToDateUserRole(String userId, Role role){
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            throw new ResourceNotFoundException("The user with " + userId + " doesn't exist");
        User updatedUser = optionalUser.get();
        updatedUser.setRole(role);
        return userRepository.save(updatedUser);
    }

    public String deleteMyAccount(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty())
            throw new ResourceNotFoundException("The user with " + email + " doesn't exist");
        User deletedUser = optionalUser.get();
        userRepository.delete(deletedUser);
        return "Your account has been deleted";
    }

    public String removeUser(String id){
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty())
            throw new ResourceNotFoundException("The user with " + id + " doesn't exist");
        User user = optionalUser.get();
        userRepository.delete(user);
        return "The user with " + id + " has been deleted";
    }
}
