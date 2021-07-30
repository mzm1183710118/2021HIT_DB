package view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * created by meizhimin on 2021/4/18
 */
public class AddSC {
    @FXML
    private TextField student_name;
    @FXML
    private TextField course_name;
    @FXML
    private TextField grade;

    private boolean okClicked = false;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public String getCourse_name() {return course_name.getText();}
    public String getStudent_name() {return student_name.getText();}
    public int getGrade() {return Integer.parseInt(grade.getText());}
    public boolean isOkClicked(){return okClicked;}

    @FXML
    public void handleOkClicked(){
        if (!student_name.getText().equals("") && !course_name.getText().equals("")) {
            okClicked = true;
            stage.close();
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText("学生名字以及课程名字都不可为空值");
            alert.showAndWait();
        }
    }

    @FXML
    public void handleCancelClicked(){
        stage.close();
    }

}
