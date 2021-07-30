package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * created by meizhimin on 2021/4/18
 */
public class Course {
    private final IntegerProperty course_id;
    private final StringProperty course_name;
    private final IntegerProperty teacher_id;
    private final StringProperty teacher_name;
    private final IntegerProperty credit;

    public Course(Integer course_id,String course_name,Integer teacher_id,String teacher_name,Integer credit){
        this.course_id=new SimpleIntegerProperty(course_id);
        this.course_name = new SimpleStringProperty(course_name);
        this.teacher_id=new SimpleIntegerProperty(teacher_id);
        this.teacher_name = new SimpleStringProperty(teacher_name);
        this.credit=new SimpleIntegerProperty(credit);
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

    // id的三个method
    public int getTeacher_id() {
        return teacher_id.get();
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id.set(teacher_id);
    }

    public IntegerProperty teacher_idProperty() {
        return teacher_id;
    }

    // name的三个method
    public String getTeacher_name() {
        return teacher_name.get();
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name.set(teacher_name);
    }

    public StringProperty teacher_nameProperty() {
        return teacher_name;
    }

    // id的三个method
    public int getCredit() {
        return credit.get();
    }

    public void setCredit(int credit) {
        this.credit.set(credit);
    }

    public IntegerProperty creditProperty() {
        return credit;
    }
}
