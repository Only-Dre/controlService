package app.domains.physiotherapist;

import app.domains.physiotherapist.enums.TechniquesENUM;

public class PhysiotherapistEntity {

    private Long id;
    private String name;
    private String email;
    private String password;
    private TechniquesENUM technique;
    private Double commission;

    public PhysiotherapistEntity(Long id, String name, String email, String password, TechniquesENUM technique, Double commission) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.technique = technique;
        this.commission = commission;
    }

    public PhysiotherapistEntity() {

    }

    @Override
    public String toString() {
        return "PhysiotherapistEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", technique=" + technique +
                ", commission=" + commission +
                '}';
    }

    /*GETTERS AND SETTERS*/
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public TechniquesENUM getTechnique() {
        return technique;
    }

    public void setTechnique(TechniquesENUM technique) {
        this.technique = technique;
    }
}
