package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * Created by omar on 10/8/17.
 */
public class Decompress implements Initializable {

    public static ScalarQuantizers scalarQuantizers=new ScalarQuantizers();

    public static File file=new File("");

    @FXML
    private Button browse;

    @FXML
    private TextArea content;

    @FXML
    private Button decompress;

    @FXML
    private TextField directoryField;
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        browse.setOnAction(e->{

            Stage stage=new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            file=fileChooser.showOpenDialog(stage);
            directoryField.setText(file.getPath());
        });

        decompress.setOnAction(e->{
            content.clear();
            readQQ();
            scalarQuantizers.decompress(format(file));
            DirectoryChooser directoryChooser=new DirectoryChooser();
            directoryChooser.setTitle("Save to");
            File save;
            save=directoryChooser.showDialog(new Stage());
            try {
                FileOutputStream fout= new FileOutputStream (save.getPath()+"/Decompressed.txt");
                ObjectOutputStream oos = new ObjectOutputStream(fout);
                oos.writeObject(scalarQuantizers.compressed);
                fout.close();
            }
            catch (IOException ex)
            {
                System.out.println("Failed to write ShortCode");
            }
            for(int i=0;i<scalarQuantizers.decompressed.size();i++)
            {
                content.appendText(Integer.toString(scalarQuantizers.decompressed.get(i))+" ");
            }
        });
    }

    public static ArrayList<Integer> format(File file){
        ArrayList<Integer>data=new ArrayList<>();
        try {
            FileInputStream fin= new FileInputStream (file.getPath());
            ObjectInputStream oos = new ObjectInputStream(fin);
            try {
                data=(ArrayList<Integer>)oos.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            fin.close();
        }
        catch (IOException io)
        {
            io.printStackTrace();
        }
        return data;
    }
    public static void readQQ()
    {
        try {
            File QQ=new File("QQ.txt");
            FileInputStream fis=new FileInputStream(QQ);
            ObjectInputStream ois=new ObjectInputStream(fis);
            scalarQuantizers.QQ= (HashMap<Integer, Integer>) ois.readObject();     //read map from file
            ois.close();
            fis.close();
        }
        catch (IOException e)
        {
            System.out.println("Failed to read QQ");
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to read QQ");
        }
    }

}
