package be.ehb.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegistratieDto(
        @NotBlank String gebruikersnaam,
        @Email @NotBlank String email,
        @NotBlank String wachtwoord
) {
}
