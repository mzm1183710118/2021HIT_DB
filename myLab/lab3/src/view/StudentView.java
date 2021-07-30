package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Apartment;
import model.StudentDetail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * created by meizhimin on 2021/4/18
 */
public class StudentView {
    @FXML
    private TableView<StudentDetail> tableView;
    @FXML
    private TableColumn<StudentDetail,Integer> student_id;
    @FXML
    private TableColumn<StudentDetail, String> student_name;
    @FXML
    private TableColumn<StudentDetail,Integer> apartment_id;
    @FXML
    private TableColumn<StudentDetail, String> apartment_name;
    @FXML
    private TableColumn<StudentDetail,Integer> class_id;
    @FXML
    private TableColumn<StudentDetail, String> class_name;
    // 维持数据库连接的对象
    private Connection conn;

    private ObservableList<StudentDetail> studentData = FXCollections.observableArrayList();

    public void setConnection(Connection conn){
        this.conn = conn;
    }

    public void showStudentDetail(){
        try{
            Statement stmt  = conn.createStatement();
            String sql = "select * " +
                    "from view_student_apartment_class";
            ResultSet rs = stmt.executeQuery(sql);
            //遍历查询的结果集
            while (rs.next()) {
                studentData.add(new StudentDetail(Integer.valueOf(rs.getString(1)),rs.getString(2),Integer.valueOf(rs.getString(3)),rs.getString(4),Integer.valueOf(rs.getString(5)),rs.getString(6)));
            }
            student_id.setCellValueFactory(
                    new PropertyValueFactory<>("student_id"));
            student_name.setCellValueFactory(
                    new PropertyValueFactory<>("student_name"));
            apartment_id.setCellValueFactory(
                    new PropertyValueFactory<>("apartment_id"));
            apartment_name.setCellValueFactory(
                    new PropertyValueFactory<>("apartment_name"));
            class_id.setCellValueFactory(
                    new PropertyValueFactory<>("class_id"));
            class_name.setCellValueFactory(
                    new PropertyValueFactory<>("class_name"));
            tableView.setItems(studentData);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
