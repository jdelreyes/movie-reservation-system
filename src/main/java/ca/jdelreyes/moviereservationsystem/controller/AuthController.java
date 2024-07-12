package ca.jdelreyes.moviereservationsystem.controller;

import ca.jdelreyes.moviereservationsystem.dto.auth.AuthRequest;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import ca.jdelreyes.moviereservationsystem.service.impl.AuthServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServiceImpl authService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @Valid @RequestBody AuthRequest authRequest, HttpServletResponse response
    ) throws NotFoundException {
        final int ONE_DAY = 60 * 60 * 24;

        String token = authService.authenticate(authRequest);

        Cookie cookie = new Cookie("token", token);

        cookie.setMaxAge(ONE_DAY);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody AuthRequest authRequest) {
        authService.register(authRequest);

        return ResponseEntity.noContent().build();
    }
}
