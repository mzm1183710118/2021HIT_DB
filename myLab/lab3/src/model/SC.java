package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * created by meizhimin on 2021/4/18
 */
public class SC {
    private final IntegerProperty student_id;
    private final StringProperty student_name;
    private final IntegerProperty course_id;
    private final StringProperty course_name;
    private final IntegerProperty grade;

    public SC(Integer student_id,String student_name,Integer course_id,String course_name,Integer grade){
        this.student_id=new SimpleIntegerProperty(student_id);
        this.student_name = new SimpleStringProperty(student_name);
        this.course_id=new SimpleIntegerProperty(course_id);
        this.course_name = new SimpleStringProperty(course_name);
        this.grade=new SimpleIntegerProperty(grade);
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

    // id的三个method
    public int getCourse_id() {
        return course_id.get();
    }

    public void setCourse_id(int course_id) {
        this.course_id.set(course_id);
    }

    public IntegerProperty course_idProperty() {
        return course_id;
    }

    // name的三个method
    public String getCourse_name() {
        return course_name.get();
    }

    public void setCourse_name(String course_name) {
        this.course_name.set(course_name);
    }

    public StringProperty course_nameProperty() {
        return course_name;
    }

    // grade的三个method
    public int getGrade() {
        return grade.get();
    }

    public void setGrade(int grade) {
        this.grade.set(grade);
    }

    public IntegerProperty gradeProperty() {
        return grade;
    }
}
