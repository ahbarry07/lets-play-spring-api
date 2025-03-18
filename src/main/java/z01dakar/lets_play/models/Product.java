package z01dakar.lets_play.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    @Field("product_name")
    private String name;

    private String description;
    private Double price;
    private String userId;
}
