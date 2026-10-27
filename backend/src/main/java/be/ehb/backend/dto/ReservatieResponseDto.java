package be.ehb.backend.dto;

import java.time.LocalDate;
import java.util.List;

public record ReservatieResponseDto(
        int reservatieId,
        String gebruikersnaam,
        List<String> gereserveerdeTitels,
        List<String> gereserveerdeIsbns,
        LocalDate reservatieDatum,
        LocalDate afhaalDatum,
        String bevestigingsBericht
) {
}
