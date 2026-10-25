package be.ehb.backend.dto;

import java.util.List;

public record ProfielDto(
        int id,
        String gebruikersnaam,
        String email,
        List<BoekDto> gekochteBoeken
) {
}
