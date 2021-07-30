package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * created by meizhimin on 2021/4/18
 */
public class ApartmentStudent {
    private final IntegerProperty student_number;
    private final StringProperty apartment_name;

    public ApartmentStudent(Integer student_number,String apartment_name){
        this.student_number=new SimpleIntegerProperty(student_number);
        this.apartment_name = new SimpleStringProperty(apartment_name);
    }

    // id的三个method
    public int getStudent_number() {
        return student_number.get();
    }

    public void setStudent_number(int student_number) {
        this.student_number.set(student_number);
    }

    public IntegerProperty student_numberProperty() {
        return student_number;
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
