package z01dakar.lets_play.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import z01dakar.lets_play.models.Role;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // Excludes `null` fields from the response
public class UserDto {

    @NotBlank(message = "name is mandatory")
    @Size(min = 2, max = 100)
    private String name;

    @NotBlank(message = "mail is mandatory")
    @Email(message = "The E-mail format isn't valid")
    private String email;

    @NotBlank(message = "password is mandatory")
    @Size(min = 6, max = 12)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  // Accepted on entry but excluded on exit
    private String password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Role role;
}
