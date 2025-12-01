package app.appointment.domain;

import app.patient.domain.Patient;
import app.physiotherapist.domain.Physiotherapist;

import java.util.List;

// Classe de Atendimento
public class Appointment {

    private Long id;
    private Patient patient; // Paciente
    private Physiotherapist physiotherapist; // Fisioterapeuta
    private String procedures; // Procedimentos
    private String date; // Data
    private String startHour; // Horário de Início do Atendimento
    private String endHour; // Horário do Fim do Atendimento
    private String duration; // Duração do Atendimento
    private String procedureValue; // Valor do rocedimento
}
