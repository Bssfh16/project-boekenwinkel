package be.ehb.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record BoekDto(
        @NotBlank String isbn,
        @NotBlank String titel,
        @NotBlank String auteurs,
        String descriptie,
        @Positive double price,
        @Min(0) int stock,
        String genre
) {
}
