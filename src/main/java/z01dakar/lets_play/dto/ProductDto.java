package z01dakar.lets_play.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductDto {

    private String id;

    @NotNull
    @NotBlank(message = "The product should have a name")
    private String name;

    @NotNull
    @NotBlank(message = "Give a description to the product")
    private String description;

    @NotNull
    @Min(value = 0, message = "0 is the min price")
    private Double price;
}
