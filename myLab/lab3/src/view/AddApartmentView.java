package view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * created by meizhimin on 2021/4/17
 */
public class AddApartmentView {

    @FXML
    private TextField apartment_name;

    private boolean okClicked = false;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public String getApartment_name() {return apartment_name.getText();}

    public boolean isOkClicked(){return okClicked;}

    @FXML
    private void handleOk() {
        if (!apartment_name.getText().equals("")) {
            okClicked = true;
            stage.close();
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText("公寓名字不可为空值");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCancel() {
        stage.close();
    }

}
