package app.domains.physiotherapist.enums;

import com.google.gson.annotations.SerializedName;

public enum TechniquesENUM {

    @SerializedName(value = "QUIROPRAXIA", alternate = {"quiropraxia", "Quiropraxia"})
    QUIROPRAXIA(200.0),


    @SerializedName(value = "PILATES", alternate = {"pilates", "Pilates"})
    PILATES(150.0),

    @SerializedName(value = "MASSAGEM", alternate = {"massagem", "Massagem"})
    MASSAGEM(100.0);

    private final Double value;

    TechniquesENUM(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }
}