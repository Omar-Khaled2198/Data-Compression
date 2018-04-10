package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable
{


    @FXML
    private Button compress;

    @FXML
    private Button decompress;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        compress.setOnAction(e->{

            Stage stage=new Stage();
            Parent root=null;
            try {
                root = FXMLLoader.load(getClass().getResource("compress.fxml"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Scene scene=new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Compress");
            stage.show();
        });

        decompress.setOnAction(e->{

            Stage stage=new Stage();
            Parent root=null;
            try {
                root = FXMLLoader.load(getClass().getResource("decompress.fxml"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Scene scene=new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Decompress");
            stage.show();
        });


    }
}
