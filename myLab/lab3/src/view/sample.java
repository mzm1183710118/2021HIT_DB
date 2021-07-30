package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

public class sample {

    private String url= "jdbc:mysql://localhost:3306/sms";//JDBC的URL，在此处标明所使用的数据库
    private String rootName = "root";
    private String pwd ="mzm12138mzm";
    private Connection conn;

    public  sample(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            //建立数据库连接
            conn = DriverManager.getConnection(url,rootName,pwd);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void ManageApartment(){
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("ApartmentView.fxml"));
            AnchorPane rootLayout = (AnchorPane) loader.load();
            // Give the controller access to the main app.
            ApartmentView controller = loader.getController();
            controller.setConnection(this.conn);
            controller.showApartment();
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ManageClass(){

    }

    @FXML
    private void ManageMajor(){

    }
    @FXML
    private void ManageCollege(){

    }
    @FXML
    private void ManageLeader(){

    }
    @FXML
    private void ManageTeacher(){

    }
    @FXML
    private void ManageStudent(){
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("StudentView.fxml"));
            AnchorPane rootLayout = (AnchorPane) loader.load();
            // Give the controller access to the main app.
            StudentView controller = loader.getController();
            controller.setConnection(this.conn);
            controller.showStudentDetail();
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    private void ManageCourse(){
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("CourseView.fxml"));
            AnchorPane rootLayout = (AnchorPane) loader.load();
            // Give the controller access to the main app.
            CourseView controller = loader.getController();
            controller.setConnection(this.conn);
            controller.showCourse();
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void ManageSC(){
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("SCView.fxml"));
            AnchorPane rootLayout = (AnchorPane) loader.load();
            // Give the controller access to the main app.
            SCView controller = loader.getController();
            controller.setConnection(this.conn);
            controller.showSC();
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    private void ManageMakeUp(){
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("MakeUpView.fxml"));
            AnchorPane rootLayout = (AnchorPane) loader.load();
            // Give the controller access to the main app.
            MakeUpView controller = loader.getController();
            controller.setConnection(this.conn);
            controller.showSC();
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
