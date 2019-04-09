package com.example.parkinginfoapp;

public class Lot {
    Double latitude;
    Double longitude;
    String lot_name;
    String lot_num;
    Integer object_id;
    String permit_type;
    String time;

    public Lot() {

    }

    public Lot(Double latitude, Double longitude, String lot_name, String lot_num, Integer object_id, String permit_type, String time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.lot_name = lot_name;
        this.lot_num = lot_num;
        this.object_id = object_id;
        this.permit_type = permit_type;
        this.time = time;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLotName() {
        return lot_name;
    }

    public void setLotName(String lot_name) {
        this.lot_name = lot_name;
    }

    public String getLotNumber() {
        return lot_num;
    }

    public void setLotNumber(String lot_num) {
        this.lot_num = lot_num;
    }

    public Integer getObjectId() {
        return object_id;
    }

    public void setObjectId(Integer object_id) {
        this.object_id = object_id;
    }

    public String getPermitType() {
        return permit_type;
    }

    public void setPermitType(String permit_type) {
        this.permit_type = permit_type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
