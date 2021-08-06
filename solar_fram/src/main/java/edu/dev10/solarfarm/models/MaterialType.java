package edu.dev10.solarfarm.models;

public enum MaterialType {
    AMP_SI("Amp Si"),
    MULTI_SI("Multi Si"),
    MONO_SI("Mono Si"),
    CDTD("CdTe"),
    CIGS("CuInGaSe")
    ;

    private final String materialName;

    MaterialType(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialName() {
        return materialName;
    }
}
