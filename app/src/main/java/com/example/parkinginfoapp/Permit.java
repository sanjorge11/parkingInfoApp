package com.example.parkinginfoapp;

public class Permit {
    String permit_type;

    public Permit() {

    }

    public Permit(String permit_type) {
        this.permit_type = permit_type;
    }

    public String getPermit_type() {
        return permit_type;
    }

    public void setPermit_type(String permit_type) {
        this.permit_type = permit_type;
    }

}
