package be.ehb.backend.dto;

import java.util.List;

/**
 * DTO om profielgegevens van een gebruiker terug te sturen zonder wachtwoord.
 */
public record ProfielDto(
        int id,
        String gebruikersnaam,
        String email,
        List<BoekDto> gekochteBoeken
) {
}
