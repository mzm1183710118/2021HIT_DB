package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * created by meizhimin on 2021/4/17
 */
public class Apartment {
    private final IntegerProperty apartment_id;
    private final StringProperty apartment_name;

    public Apartment(Integer apartment_id,String apartment_name){
        this.apartment_id=new SimpleIntegerProperty(apartment_id);
        this.apartment_name = new SimpleStringProperty(apartment_name);
    }

    // id的三个method
    public int getApartment_id() {
        return apartment_id.get();
    }

    public void setApartment_id(int apartment_id) {
        this.apartment_id.set(apartment_id);
    }

    public IntegerProperty apartment_idProperty() {
        return apartment_id;
    }

    // name的三个method
    public String getApartment_name() {
        return apartment_name.get();
    }

    public void setApartment_name(String apartment_name) {
        this.apartment_name.set(apartment_name);
    }

    public StringProperty apartment_nameProperty() {
        return apartment_name;
    }
}
