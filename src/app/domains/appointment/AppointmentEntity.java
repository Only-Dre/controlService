package app.domains.appointment;

import app.domains.patient.PatientEntity;
import app.domains.physiotherapist.PhysiotherapistEntity;

// Classe de Atendimento
public class AppointmentEntity {

    private Long id;
    private PatientEntity patientEntity; // Paciente
    private PhysiotherapistEntity physiotherapistEntity; // Fisioterapeuta
    private String procedures; // Procedimentos
    private String date; // Data
    private String startHour; // Horário de Início do Atendimento
    private String endHour; // Horário do Fim do Atendimento
    private String duration; // Duração do Atendimento
    private String procedureValue; // Valor do rocedimento

    public AppointmentEntity(Long id, PatientEntity patientEntity, PhysiotherapistEntity physiotherapistEntity, String procedures, String date, String startHour, String endHour, String duration, String procedureValue) {
        this.id = id;
        this.patientEntity = patientEntity;
        this.physiotherapistEntity = physiotherapistEntity;
        this.procedures = procedures;
        this.date = date;
        this.startHour = startHour;
        this.endHour = endHour;
        this.duration = duration;
        this.procedureValue = procedureValue;
    }

    public AppointmentEntity() {
    }


    /*Getters and Setters*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PatientEntity getPatient() {
        return patientEntity;
    }

    public void setPatient(PatientEntity patientEntity) {
        this.patientEntity = patientEntity;
    }

    public PhysiotherapistEntity getPhysiotherapist() {
        return physiotherapistEntity;
    }

    public void setPhysiotherapist(PhysiotherapistEntity physiotherapistEntity) {
        this.physiotherapistEntity = physiotherapistEntity;
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
