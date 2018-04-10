package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Created by omar on 10/8/17.
 */
public class Compress implements Initializable {

    public static Tree tree=new Tree();

    public File file=new File("");

    @FXML
    private Button browse;

    @FXML
    private TextArea originalContent;

    @FXML
    private TextArea  compressedContent;

    @FXML
    private Button compress;


    @FXML
    private TextField directoryField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        browse.setOnAction(e->{

            Stage stage=new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            file=fileChooser.showOpenDialog(stage);
            directoryField.setText(file.getPath());
            originalContent.setText(formatContent(file));
        });

        compress.setOnAction((ActionEvent e) ->{
            compressedContent.clear();
            if(file.exists())
                tree.compress(formatContent(file));
            else
                tree.compress(originalContent.getText());
            WriteShortCode();
            DirectoryChooser directoryChooser=new DirectoryChooser();
            directoryChooser.setTitle("Save to");
            File save;
            Stage stage=new Stage();
            save=directoryChooser.showDialog(stage);
            for (Character K:tree.shortCode.keySet()) {
                compressedContent.appendText(K+" "+tree.shortCode.get(K)+"\n");
            }
            try {
                File f=new File(save.getPath()+"/Compressed.txt");
                FileWriter fileWriter=new FileWriter(f.getPath());
                fileWriter.write(tree.compressed);
                fileWriter.close();
            }
            catch (IOException io)
            {
                io.printStackTrace();
            }

        });
    }


    public static String formatContent(File file)
    {
        Scanner scanner= null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String content=scanner.useDelimiter("\\Z").next();
        return content;
    }

    public static void WriteShortCode()
    {
        try {
        File shortCode=new File("/home/omar/ShortCode.txt");
        FileOutputStream fos=new FileOutputStream(shortCode);
        ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(tree.shortCode);
            oos.flush();
            oos.close();
            fos.close();
        }
        catch (IOException e)
        {
            System.out.println("Failed to write ShortCode");
        }
    }

}
