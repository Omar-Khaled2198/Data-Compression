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

    public static Arithmetic arithmetic=new Arithmetic();

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
            readFromFile();
            arithmetic.decompress(format(file));
            String originalContent=arithmetic.decompressed;
            DirectoryChooser directoryChooser=new DirectoryChooser();
            directoryChooser.setTitle("Save to");
            File save;
            save=directoryChooser.showDialog(new Stage());
            try {
                FileWriter fileWriter=new FileWriter(save.getPath() + "/Original " + file.getName());
                content.appendText(originalContent);
                fileWriter.write(originalContent);        //write string to file
                fileWriter.close();
                }
            catch (IOException io)
            {
                io.printStackTrace();
            }
        });
    }

    public static String format(File file)
    {
        String content=null;
        Scanner scanner= null;
        try {
            scanner = new Scanner(file);                    //read string from file
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        content=scanner.useDelimiter("\\Z").next();

        return content;
    }
    public static void readFromFile()
    {
        try {
            File toRead=new File("/home/omar/Ranges.txt");
            DataInputStream in = new DataInputStream(new FileInputStream("Ranges.txt"));
            arithmetic.size=in.readInt();        //read string size from file
            in.close();
            FileInputStream fis=new FileInputStream(toRead);
            ObjectInputStream ois=new ObjectInputStream(fis);
            arithmetic.orginalRanges=(HashMap<Character,Range>)ois.readObject();     //read map from file

            ois.close();
            fis.close();
        }
        catch (IOException e)
        {
            System.out.println("Failed to write Ranges");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
