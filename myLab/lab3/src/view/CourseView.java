package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Course;
import model.StudentDetail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * created by meizhimin on 2021/4/18
 */
public class CourseView {
    @FXML
    private TableView<Course> tableView;
    @FXML
    private TableColumn<Course,Integer> course_id;
    @FXML
    private TableColumn<Course, String> course_name;
    @FXML
    private TableColumn<Course,Integer> credit;
    @FXML
    private TableColumn<Course,Integer> teacher_id;
    @FXML
    private TableColumn<Course, String> teacher_name;

    // 维持数据库连接的对象
    private Connection conn;

    private ObservableList<Course> courseData = FXCollections.observableArrayList();

    public void setConnection(Connection conn){
        this.conn = conn;
    }

    public void showCourse(){
        try{
            Statement stmt  = conn.createStatement();
            String sql = "select * " +
                    "from view_course_teacher";
            ResultSet rs = stmt.executeQuery(sql);
            //遍历查询的结果集
            while (rs.next()) {
                    courseData.add(new Course(Integer.valueOf(rs.getString(1)),rs.getString(2),Integer.valueOf(rs.getString(4)),rs.getString(5),Integer.valueOf(rs.getString(3))));
            }
            course_id.setCellValueFactory(
                    new PropertyValueFactory<>("course_id"));
            course_name.setCellValueFactory(
                    new PropertyValueFactory<>("course_name"));
            credit.setCellValueFactory(
                    new PropertyValueFactory<>("credit"));
            teacher_id.setCellValueFactory(
                    new PropertyValueFactory<>("teacher_id"));
            teacher_name.setCellValueFactory(
                    new PropertyValueFactory<>("teacher_name"));
            tableView.setItems(courseData);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
