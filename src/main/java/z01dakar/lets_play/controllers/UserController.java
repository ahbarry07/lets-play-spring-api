package z01dakar.lets_play.controllers;


import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import z01dakar.lets_play.MyHttpResponse;
import z01dakar.lets_play.dto.RoleUpdateRequest;
import z01dakar.lets_play.dto.UserDto;
import z01dakar.lets_play.models.User;
import z01dakar.lets_play.services.UserService;

import java.util.List;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/api/users")
@CrossOrigin(
        origins = {"http://localhost:3000", "https://z01dakar.lets-play.com"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT}
)
public class UserController extends MyHttpResponse {

    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;


    private String getCurrentUserEmail() {
        return ((UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername();
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        List<UserDto> users = this.userService.allUsers()
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());

        return response(HttpStatus.OK, "OK", users);
    }

    @GetMapping("/me")
    public ResponseEntity<Object> getCurrentUser() {
        // Get the username connected using context security
        String userEmail = getCurrentUserEmail();

        // Get the userinfo using userService
        User user = userService.showUserByEmail(userEmail);
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return response(HttpStatus.OK, "OK", userDto);
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDto userDto){
        // Get the username info before updating
        String userEmail = getCurrentUserEmail();

        User user = this.userService.bringUpToDateUser(modelMapper.map(userDto, User.class), userEmail);
        UserDto updatedUserDto = this.modelMapper.map(user, UserDto.class);
        return response(HttpStatus.OK, "User has been modified", updatedUserDto);
    }

    @PutMapping("/role/{id}")
    @PostAuthorize("hasRole('ADMIN')")// Restriction au rôle ADMIN
    public ResponseEntity<Object> updateUserRole(@PathVariable("id") String userId,
                                                 @RequestBody RoleUpdateRequest roleUpdateRequest) {

        User updatedUser = userService.bringUpToDateUserRole(userId, roleUpdateRequest.getRole());
        UserDto userDto = modelMapper.map(updatedUser, UserDto.class);
        return response(HttpStatus.OK, "The user role has been updated", userDto);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Object> deleteAccount(){
        String userEmail = getCurrentUserEmail();
        String message = this.userService.deleteMyAccount(userEmail);
        return response(HttpStatus.OK, message, null);
    }

    @DeleteMapping("/delete/{id}")
    @PostAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") String userId){
        String message = this.userService.removeUser(userId);
        return response(HttpStatus.OK, message, null);
    }
}
