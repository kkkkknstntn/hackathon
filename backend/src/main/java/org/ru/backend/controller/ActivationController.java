package org.ru.backend.controller;

import org.ru.backend.service.ActivationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activation")
public class ActivationController {

    private final ActivationService activationService;

    public ActivationController(ActivationService activationService) {
        this.activationService = activationService;
    }

    @GetMapping("/activate")
    @ResponseStatus(HttpStatus.OK)
    public void activateAccount(
            @RequestParam String token,
            @RequestParam String email
    ) {
        activationService.activateAccount(token, email);
    }
}