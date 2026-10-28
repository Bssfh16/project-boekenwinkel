package be.ehb.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO om registratiegegevens te ontvangen.
 */
public record RegistratieDto(
        @NotBlank String gebruikersnaam,
        @Email @NotBlank String email,
        @NotBlank String wachtwoord
) {
}
