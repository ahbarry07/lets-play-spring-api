package z01dakar.lets_play.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import z01dakar.lets_play.MyHttpResponse;
import z01dakar.lets_play.dto.UserDto;
import z01dakar.lets_play.models.User;
import z01dakar.lets_play.security.JwtUtil;
import z01dakar.lets_play.services.CustomUserDetailsService;
import z01dakar.lets_play.services.UserService;



@RestController
@RequestMapping("/api/auth")
@CrossOrigin(
        origins = {"http://localhost:3000", "https://z01dakar.lets-play.com"},
        methods = {RequestMethod.GET, RequestMethod.POST}
)
public class AuthController extends MyHttpResponse {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, CustomUserDetailsService userDetailsService, ModelMapper modelMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthRequest authRequest) {
        try {
            // Authentication attempt
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            // If authentication successful, generate token
            UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
            String token = jwtUtil.generateToken(userDetails.getUsername(),
                    userDetails.getAuthorities().iterator().next().getAuthority());

            return ResponseEntity.ok(token);
        } catch (UsernameNotFoundException e) {
            return  response(HttpStatus.UNAUTHORIZED, e.getMessage(), null);
        } catch (BadCredentialsException e) {
            return  response(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> addUser(@Valid @RequestBody UserDto userDto){

        //convert UserDto to User
        User user = this.userService.createUser(modelMapper.map(userDto, User.class));
        UserDto userCreatedDto = this.modelMapper.map(user, UserDto.class);

        return response(HttpStatus.CREATED, "CREATED", userCreatedDto);
    }
}

@Data
class AuthRequest {

    @Email
    @NotBlank(message = "Enter your mail")
    private String email;

    @NotBlank(message = "Enter your password")
    private String password;
}
