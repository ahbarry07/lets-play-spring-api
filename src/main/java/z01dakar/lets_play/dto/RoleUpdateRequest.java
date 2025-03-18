package z01dakar.lets_play.dto;

import lombok.Data;
import z01dakar.lets_play.models.Role;

@Data
public class RoleUpdateRequest {
    private Role role;
}
