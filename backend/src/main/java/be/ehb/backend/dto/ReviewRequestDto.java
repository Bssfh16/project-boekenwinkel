package be.ehb.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO om een nieuwe voor een bepaald boek door een bepaalde gebruiker aan te maken.
 */
public record ReviewRequestDto(
        int gebruikerId,
        @NotBlank String boekIsbn,
        @Min(1) @Max(5) int score,
        @NotBlank String tekst
) {
}
