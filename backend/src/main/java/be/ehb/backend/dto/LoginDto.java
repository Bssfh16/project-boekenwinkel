package be.ehb.backend.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO om inloggegevens te ontvangen.
 */
public record LoginDto(
        @NotBlank String gebruikersnaam,
        @NotBlank String wachtwoord
) {
}
