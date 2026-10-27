package be.ehb.backend.dto;

import java.time.LocalDate;

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