package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Apartment;
import model.ApartmentStudent;
import model.SC;
import model.Student;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * created by meizhimin on 2021/4/18
 */
public class SCView {
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
            String sql = "select Student.student_id,Student.student_name,Course.course_id,Course.course_name,SC.grade " +
                    "from Student,Course,SC "+
                    "where SC.student_id = Student.student_id AND SC.course_id = Course.course_id";
            ResultSet rs = stmt.executeQuery(sql);
            SCData.clear();
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
    @FXML
    public void handleAddSC(){
        try{
            List<Object> objects = this.showAddSCView();
            boolean okClicked = (Boolean)objects.get(0);
            String studentName = (String)objects.get(1);
            String courseName = (String)objects.get(2);
            int scGrade = (Integer)objects.get(3);
            if (okClicked) {
                // 给数据库添加
                Statement stmt  = conn.createStatement();
                conn.setAutoCommit(false);  //将自动提交设置为false
                // 先找到学生id和课程id
                String sql = "select  student_id "+
                        "from student "+
                        "where student_name = \""+studentName+"\"";
                ResultSet rs = stmt.executeQuery(sql);
                int studentID=0;
                //遍历查询的结果集
                while (rs.next()) {
                    studentID = Integer.parseInt(rs.getString(1));
                }

                String sq2 = "select course_id "+
                        "from course "+
                        "where course_name = \""+courseName+"\"";
                ResultSet rs2 = stmt.executeQuery(sq2);
                int courseID=0;
                //遍历查询的结果集
                while (rs2.next()) {
                    courseID = Integer.parseInt(rs2.getString(1));
                }
                String sq3 = "insert into SC(student_id,course_id,grade) "+
                        "values (\""+studentID+"\",\""+courseID+"\","+scGrade+")";
                stmt.executeUpdate(sq3);
                this.showSC();
                conn.commit();
            }
        }catch(SQLIntegrityConstraintViolationException e){
            try{
                conn.rollback();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("由于外键约束，部分域非法导致添加失败！");
                alert.setContentText("您的输入存在非法字段（学生名、课程名必须是真实存在的）！");
                alert.showAndWait();
            }catch(Exception e1){
                e1.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private List<Object> showAddSCView() throws Exception{
        // Load root layout from fxml file.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("AddSC.fxml"));
        AnchorPane rootLayout = (AnchorPane) loader.load();

        // Show the scene containing the root layout.
        Scene scene = new Scene(rootLayout);
        Stage stage = new Stage();
        stage.setScene(scene);
        // Give the controller access to the main app.
        AddSC controller = loader.getController();
        controller.setStage(stage);
        stage.showAndWait();

        // 返回ok状态以及用户填写的内容
        boolean flag =  controller.isOkClicked();
        String student_name = controller.getStudent_name();
        String course_name = controller.getCourse_name();
        Integer grade = controller.getGrade();
        List<Object> objects = new ArrayList<>();
        objects.add(flag);
        objects.add(student_name);
        objects.add(course_name);
        objects.add(grade);
        return objects;
    }



}
