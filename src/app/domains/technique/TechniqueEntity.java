package app.domains.technique;

public class TechniqueEntity {

    private Long id;
    private String name;
    private Double value;

    public TechniqueEntity(Long id, String name, Double value) {
        this.name = name;
        this.value = value;
    }

    public TechniqueEntity() {
    }


    /*Getters and Setters*/
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

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
