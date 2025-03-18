package z01dakar.lets_play.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import z01dakar.lets_play.exception.NotOwnResourceException;
import z01dakar.lets_play.exception.ResourceNotFoundException;
import z01dakar.lets_play.exception.UnAuthorizedException;
import z01dakar.lets_play.models.Product;
import z01dakar.lets_play.models.Role;
import z01dakar.lets_play.models.User;
import z01dakar.lets_play.repository.ProductRepository;
import z01dakar.lets_play.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product showProductById(String id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty())
            throw new ResourceNotFoundException("Product with id: " + id + " not found");
        return product.get();
    }

    public List<Product> showMyProduct(String userEmail) {
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if (optionalUser.isEmpty())
            throw new UnAuthorizedException("Please sign in");
        User user = optionalUser.get();
        Optional<List<Product>> optionalProducts = productRepository.findAllByUserId(user.getId());
        return optionalProducts.orElseGet(List::of);
    }

    public Product createProduct(Product product, String emailCredential) {
        Optional<User> optionalUser = userRepository.findByEmail(emailCredential);
        if (optionalUser.isEmpty())
            throw new UnAuthorizedException("You can not create a product please to sign in");
        User user = optionalUser.get();
        product.setId(null);
        product.setUserId(user.getId());
        return productRepository.save(product);
    }

    public Product bringUpToDateProduct(Product product, String userEmail, String productIdToBringUp) {
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if (optionalUser.isEmpty())
            throw new UnAuthorizedException("You can not bring up to date");
        User user = optionalUser.get();

        Optional<Product> optionalProduct = productRepository.findById(productIdToBringUp);
        if (optionalProduct.isEmpty()) {
            throw new ResourceNotFoundException("Product with id " + productIdToBringUp + " not found");
        }
        Product updatedProduct = optionalProduct.get();

        if (!updatedProduct.getUserId().equals(user.getId()))
            throw new NotOwnResourceException("You are not owning this product");

        updatedProduct.setName(product.getName());
        updatedProduct.setPrice(product.getPrice());
        updatedProduct.setDescription(product.getDescription());
        return productRepository.save(updatedProduct);
    }

    public String deleteProductById(String productId, String userEmail) {
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if (optionalUser.isEmpty())
            throw new UnAuthorizedException("You can not delete a product please to sign in");
        User user = optionalUser.get();

        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty())
            throw new ResourceNotFoundException("Product with id " + productId + " not found");
        Product product = optionalProduct.get();

        boolean isOwner = product.getUserId().equals(user.getId());
        boolean isAdmin = Role.ADMIN.equals(user.getRole());
        if (!isOwner && !isAdmin)
            throw new NotOwnResourceException("You are not owning this product");

        productRepository.delete(product);
        return "Product with id: " + productId + " has been deleted";
    }
}
