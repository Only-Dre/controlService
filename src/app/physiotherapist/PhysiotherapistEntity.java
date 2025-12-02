package app.physiotherapist;

import java.util.List;

public class PhysiotherapistEntity {

    private Long id;
    private String name;
    private String email;
    private String password;
    private List<String> procedures;
    private Double commission;

    public PhysiotherapistEntity(Long id, String name, String email, String password, List<String> procedures, Double commission) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.procedures = procedures;
        this.commission = commission;
    }

    public PhysiotherapistEntity() {
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

    public String getProcedures() {
        return procedures;
    }

    public void setProcedures(String procedures) {
        this.procedures = procedures;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }
}
