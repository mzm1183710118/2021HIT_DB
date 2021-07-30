package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Apartment;
import model.ApartmentStudent;
import model.Student;

import java.sql.*;

/**
 * created by meizhimin on 2021/4/18
 */
public class ApartmentQuery {
    @FXML
    private TextField textField;

    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student,Integer> student_id;
    @FXML
    private TableColumn<Student, String> student_name;

    @FXML
    private TableView<ApartmentStudent> apartmentStudentTable;
    @FXML
    private TableColumn<ApartmentStudent,String> apartment_name;
    @FXML
    private TableColumn<ApartmentStudent, Integer> student_number;

    // 维持数据库连接的对象
    private Connection conn;

    private ObservableList<Student> studentData = FXCollections.observableArrayList();
    private ObservableList<ApartmentStudent> apartmentStudentData = FXCollections.observableArrayList();

    public void setConnection(Connection conn){
        this.conn = conn;
    }
    public void showApartmentStudent(){
        try{
            Statement stmt  = conn.createStatement();
            String sql = "select apartment_name, count(*) " +
                    "from Apartment, Student "+
                    "where Apartment.apartment_id = Student.apartment_id "+
                    "group by apartment_name";
            ResultSet rs = stmt.executeQuery(sql);
            //遍历查询的结果集
            while (rs.next()) {
                apartmentStudentData.add(new ApartmentStudent(Integer.valueOf(rs.getString(2)),rs.getString(1)));
            }
            student_number.setCellValueFactory(
                    new PropertyValueFactory<>("student_number"));
            apartment_name.setCellValueFactory(
                    new PropertyValueFactory<>("apartment_name"));
            apartmentStudentTable.setItems(apartmentStudentData);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOkClicked(){
        String name = textField.getText();
        try{
            Statement stmt  = conn.createStatement();
            String sql = "select student_id, student_name " +
                    "from Apartment, Student "+
                    "where Student.apartment_id = Apartment.apartment_id AND Student.apartment_id in ( "+
                    "select  apartment_id "+
                    "from Apartment "+
                    "where apartment_name = \""+name+"\")";
            ResultSet rs = stmt.executeQuery(sql);
            studentData.clear();
            //遍历查询的结果集
            while (rs.next()) {
                studentData.add(new Student(Integer.valueOf(rs.getString(1)),rs.getString(2)));
            }
            student_id.setCellValueFactory(
                    new PropertyValueFactory<>("student_id"));
            student_name.setCellValueFactory(
                    new PropertyValueFactory<>("student_name"));
            studentTable.setItems(studentData);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
