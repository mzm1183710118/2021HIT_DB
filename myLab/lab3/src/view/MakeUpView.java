package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.SC;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * created by meizhimin on 2021/4/18
 */
public class MakeUpView {
    @FXML
    private TableView<SC> SCTable;
    @FXML
    private TableColumn<SC,Integer> student_id;
    @FXML
    private TableColumn<SC, String> student_name;
    @FXML
    private TableColumn<SC,Integer> course_id;
    @FXML
    private TableColumn<SC, String> course_name;
    @FXML
    private TableColumn<SC,Integer> grade;

    // 维持数据库连接的对象
    private Connection conn;

    private ObservableList<SC> SCData = FXCollections.observableArrayList();

    public void setConnection(Connection conn){
        this.conn = conn;
    }

    public void showSC(){
        try{
            Statement stmt  = conn.createStatement();
            String sql = "select v.student_id,v.student_name,v.course_id,v.course_name,v.grade " +
                    "from view_student_course_grade v,make_up "+
                    "where v.student_id = make_up.student_id AND v.course_id = make_up.course_id";
            ResultSet rs = stmt.executeQuery(sql);
            //遍历查询的结果集
            while (rs.next()) {
                SCData.add(new SC(Integer.valueOf(rs.getString(1)),rs.getString(2),Integer.valueOf(rs.getString(3)),rs.getString(4),Integer.valueOf(rs.getString(5))));
            }
            student_id.setCellValueFactory(
                    new PropertyValueFactory<>("student_id"));
            student_name.setCellValueFactory(
                    new PropertyValueFactory<>("student_name"));
            course_id.setCellValueFactory(
                    new PropertyValueFactory<>("course_id"));
            course_name.setCellValueFactory(
                    new PropertyValueFactory<>("course_name"));
            grade.setCellValueFactory(
                    new PropertyValueFactory<>("grade"));
            SCTable.setItems(SCData);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
