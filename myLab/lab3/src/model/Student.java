package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * created by meizhimin on 2021/4/18
 */
public class Student {
    private final IntegerProperty student_id;
    private final StringProperty student_name;

    public Student(Integer student_id,String student_name){
        this.student_id=new SimpleIntegerProperty(student_id);
        this.student_name = new SimpleStringProperty(student_name);
    }

    // id的三个method
    public int getStudent_id() {
        return student_id.get();
    }

    public void setStudent_id(int student_id) {
        this.student_id.set(student_id);
    }

    public IntegerProperty student_idProperty() {
        return student_id;
    }

    // name的三个method
    public String getStudent_name() {
        return student_name.get();
    }

    public void setStudent_name(String student_name) {
        this.student_name.set(student_name);
    }

    public StringProperty student_nameProperty() {
        return student_name;
    }
}
