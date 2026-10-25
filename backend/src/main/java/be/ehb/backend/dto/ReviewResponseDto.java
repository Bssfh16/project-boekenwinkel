package be.ehb.backend.dto;

public record ReviewResponseDto(
        int id,
        String boekIsbn,
        String gebruikersnaam,
        int score,
        String tekst
) {
}
