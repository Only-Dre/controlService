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

    public Appointment(Long id, Patient patient, Physiotherapist physiotherapist, String procedures, String date, String startHour, String endHour, String duration, String procedureValue) {
        this.id = id;
        this.patient = patient;
        this.physiotherapist = physiotherapist;
        this.procedures = procedures;
        this.date = date;
        this.startHour = startHour;
        this.endHour = endHour;
        this.duration = duration;
        this.procedureValue = procedureValue;
    }

    public Appointment() {
    }


    /*Getters and Setters*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Physiotherapist getPhysiotherapist() {
        return physiotherapist;
    }

    public void setPhysiotherapist(Physiotherapist physiotherapist) {
        this.physiotherapist = physiotherapist;
    }

    public String getProcedures() {
        return procedures;
    }

    public void setProcedures(String procedures) {
        this.procedures = procedures;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getProcedureValue() {
        return procedureValue;
    }

    public void setProcedureValue(String procedureValue) {
        this.procedureValue = procedureValue;
    }
}
