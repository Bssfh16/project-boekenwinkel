package be.ehb.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * DTO om boekgegevens te ontvangen.
 */
public record BoekDto(
        @NotBlank String isbn,
        @NotBlank String titel,
        @NotBlank String auteurs,
        String descriptie,
        @Positive double prijs,
        @Min(0) int stock,
        String genre
) {
}
