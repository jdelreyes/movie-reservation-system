package ca.jdelreyes.moviereservationsystem.controller;

import ca.jdelreyes.moviereservationsystem.dto.user.UpdateOwnPasswordRequest;
import ca.jdelreyes.moviereservationsystem.dto.user.UpdateOwnProfileRequest;
import ca.jdelreyes.moviereservationsystem.dto.user.UpdateUserRequest;
import ca.jdelreyes.moviereservationsystem.dto.user.UserResponse;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import ca.jdelreyes.moviereservationsystem.model.User;
import ca.jdelreyes.moviereservationsystem.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") Long id) throws NotFoundException {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("id") Long id,
                                                   @Valid @RequestBody UpdateUserRequest updateUserRequest) throws NotFoundException {
        return ResponseEntity.ok(userService.updateUser(id, updateUserRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) throws NotFoundException {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update-profile")
    public ResponseEntity<UserResponse> updateOwnProfile(@AuthenticationPrincipal User user,
                                                         @Valid @RequestBody UpdateOwnProfileRequest updateProfileRequest) throws NotFoundException {
        return ResponseEntity.ok(userService.updateOwnProfile(user.getUsername(), updateProfileRequest));
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updateOwnPassword(@AuthenticationPrincipal User user,
                                               @Valid @RequestBody UpdateOwnPasswordRequest updateOwnPasswordRequest) throws NotFoundException {
        userService.updateOwnPassword(user.getUsername(), updateOwnPasswordRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<?> deleteOwnAccount(@AuthenticationPrincipal User user) throws NotFoundException {
        userService.deleteOwnAccount(user.getUsername());
        return ResponseEntity.noContent().build();
    }
}