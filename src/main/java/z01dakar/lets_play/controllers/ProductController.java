package z01dakar.lets_play.controllers;


import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import z01dakar.lets_play.MyHttpResponse;
import z01dakar.lets_play.dto.ProductDto;
import z01dakar.lets_play.models.Product;
import z01dakar.lets_play.services.ProductService;

import java.util.List;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/api/products")
@CrossOrigin(
        origins = {"http://localhost:3000", "https://z01dakar.lets-play.com"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT}
)
public class ProductController extends MyHttpResponse {

    @Autowired
    ProductService productService;
    @Autowired
    ModelMapper modelMapper;

    private String getCurrentUserEmail() {
        return ((UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername();
    }

    @PermitAll
    @GetMapping
    public ResponseEntity<Object> getProducts() {

        List<ProductDto> products =  productService.getAllProducts()
                .stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
        return response(HttpStatus.OK, "OK", products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Object> getProductBy(@PathVariable("productId") String productId) {
        Product product = productService.showProductById(productId);
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        return response(HttpStatus.OK, "OK", productDto);
    }

    @GetMapping("/myproduct")
    public ResponseEntity<Object> getMyProducts() {
        String userEmail = getCurrentUserEmail();
        List<Product> products = productService.showMyProduct(userEmail);
        return response(HttpStatus.OK, "OK", products);
    }

    @PostMapping("/addproduct")
    public ResponseEntity<Object> addProduct(@Valid @RequestBody ProductDto productDto) {
        String userEmail = getCurrentUserEmail();
        Product newProduct = productService.createProduct(modelMapper.map(productDto, Product.class), userEmail);
        ProductDto productCreatedDto = modelMapper.map(newProduct, ProductDto.class);
        return response(HttpStatus.CREATED, "You're added a new product", productCreatedDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable("id") String productId,
            @Valid @RequestBody ProductDto productRequest) {

        String userEmail = getCurrentUserEmail();
        Product product = productService.bringUpToDateProduct(modelMapper.map(productRequest, Product.class), userEmail, productId);
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        return response(HttpStatus.OK, "Product has been modified", productDto);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Object> deleteProduct(@PathVariable("productId") String productId) {
        String userEmail = getCurrentUserEmail();
        String message = productService.deleteProductById(productId, userEmail);
        return response(HttpStatus.OK, message, null);
    }

}
