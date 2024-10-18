package com.gdschongik.gdsc.domain.test.api;

import com.gdschongik.gdsc.domain.test.application.TestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "[Test]", description = "Admin")
@RestController
@RequestMapping("/tests")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping("/reset")
    public ResponseEntity<Void> reset() {
        testService.reset();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/complete")
    public ResponseEntity<Void> completeOrder() {
        testService.confirm();
        return ResponseEntity.noContent().build();
    }
}
