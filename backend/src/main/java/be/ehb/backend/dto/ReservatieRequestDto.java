package be.ehb.backend.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * DTO om een nieuwe reservatie op basis van ID gebruiker en ISBN-nummer vna het boek aan te vragen.
 */
public record ReservatieRequestDto(
        int gebruikerId,
        @NotEmpty(message = "Kies minstens één boek om te reserveren!") List<String> boekIsbns
) {
}
