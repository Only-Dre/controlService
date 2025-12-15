package app.domains.appointment.dtos;

import java.time.LocalDateTime;

public record AppointmentDTO(
        Long patientId,
        Long physiotherapistId,
        String technique,
        LocalDateTime date,
        Integer duration
) {
}
