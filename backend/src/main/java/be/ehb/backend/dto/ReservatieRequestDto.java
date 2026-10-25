package be.ehb.backend.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ReservatieRequestDto(
        int gebruikerId,
        @NotEmpty(message = "Kies minstens één boek om te reserveren!") List<String> boekIsbns
) {
}
