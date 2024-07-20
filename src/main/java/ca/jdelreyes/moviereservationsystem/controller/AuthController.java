package ca.jdelreyes.moviereservationsystem.controller;

import ca.jdelreyes.moviereservationsystem.dto.auth.AuthRequest;
import ca.jdelreyes.moviereservationsystem.exception.NotFoundException;
import ca.jdelreyes.moviereservationsystem.service.impl.AuthServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServiceImpl authService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @Valid @RequestBody AuthRequest authRequest, HttpServletResponse response
    ) throws NotFoundException {
        String token = authService.authenticate(authRequest);

        ResponseCookie cookieToken = ResponseCookie.from("token", token)
                .maxAge(Duration.ofDays(1))
                .path("/")
                .httpOnly(true)
                .sameSite("LAX")
                .build();

        ResponseCookie usernameCookie = ResponseCookie.from("username", authRequest.username())
                .maxAge(Duration.ofDays(1))
                .path("/")
                .httpOnly(false)
                .sameSite("LAX")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookieToken.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, usernameCookie.toString());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody AuthRequest authRequest) {
        authService.register(authRequest);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .maxAge(Duration.ofSeconds(-1))
                .path("/")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.noContent().build();
    }
}
