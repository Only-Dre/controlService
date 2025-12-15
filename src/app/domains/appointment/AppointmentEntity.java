package app.domains.appointment;

import app.domains.patient.PatientEntity;
import app.domains.physiotherapist.PhysiotherapistEntity;
import app.domains.physiotherapist.enums.TechniquesENUM;

import java.time.LocalDateTime;


public class AppointmentEntity {

    private Long id;
    private PatientEntity patient;
    private PhysiotherapistEntity physiotherapist;
    private TechniquesENUM technique;
    private LocalDateTime date;
    private Integer duration;
    private Double value;
    private Double netValue;

    public AppointmentEntity(Long id, PatientEntity patient, PhysiotherapistEntity physiotherapist, TechniquesENUM technique, LocalDateTime date, Integer duration) {
        this.id = id;
        this.patient = patient;
        this.physiotherapist = physiotherapist;
        this.technique = technique;
        this.date = date;
        this.duration = duration;
        this.value = this.technique.getValue();
        this.netValue = (this.value * physiotherapist.getCommission()) / 100;
    }

    public AppointmentEntity(PatientEntity patient, PhysiotherapistEntity physiotherapist, TechniquesENUM technique, LocalDateTime date, Integer duration) {
        this.patient = patient;
        this.physiotherapist = physiotherapist;
        this.technique = technique;
        this.date = date;
        this.duration = duration;
        this.value = this.technique.getValue();
        this.netValue = (this.value * physiotherapist.getCommission()) / 100;
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
        return patient;
    }

    public void setPatient(PatientEntity patientEntity) {
        this.patient = patientEntity;
    }

    public PhysiotherapistEntity getPhysiotherapist() {
        return physiotherapist;
    }

    public void setPhysiotherapist(PhysiotherapistEntity physiotherapistEntity) {this.physiotherapist = physiotherapistEntity;}

    public TechniquesENUM getTechnique() {
        return technique;
    }

    public void setTechnique(TechniquesENUM technique) {
        this.technique = technique;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getNetValue() {
        return netValue;
    }

    public void setNetValue(Double netValue) {
        this.netValue = netValue;
    }
}
