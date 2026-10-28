package be.ehb.backend.dto;

import java.time.LocalDate;

/**
 * DTO om een gemaakte review in de frontend weer te geven.
 */
public record ReviewResponseDto(
        int id,
        String boekIsbn,
        String boekTitel,
        String gebruikersnaam,
        int score,
        String tekst,
        LocalDate datum
) {
}