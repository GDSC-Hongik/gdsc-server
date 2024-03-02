package com.gdschongik.gdsc.domain.auth.api;

import com.gdschongik.gdsc.domain.auth.application.AuthService;
import com.gdschongik.gdsc.domain.auth.dto.request.LoginRequest;
import com.gdschongik.gdsc.domain.auth.dto.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "어드민 인증 API입니다.")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인", description = "로그인을 수행합니다. 어드민만 가능합니다.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.loginAdmin(request);
        return ResponseEntity.ok().body(response);
    }
}
