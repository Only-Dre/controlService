package app.domains.physiotherapist.dtos;

public record PhysiotherapistStatisticsDTO (
        Long physiotherapistId,
        String physiotherapistName,
        Integer totalAppointments,
        Double totalValue,
        Double totalNetValue
) {
}
