package be.ehb.backend.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO voor een gemaakte reservatie met overizcht.
 */
public record ReservatieResponseDto(
        int reservatieId,
        String gebruikersnaam,
        List<String> gereserveerdeIsbns,
        List<String> gereserveerdeTitels,
        LocalDate reservatieDatum,
        LocalDate afhaalDatum,
        String bevestigingsBericht
) {
}
