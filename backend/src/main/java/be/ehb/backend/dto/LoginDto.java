package be.ehb.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDto(
        @NotBlank String gebruikersnaam,
        @NotBlank String wachtwoord
) {
}
