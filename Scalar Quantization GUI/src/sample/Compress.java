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

    public static ScalarQuantizers scalarQuantizers=new ScalarQuantizers();

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
            ArrayList<Integer>temp=formatContent(file);
            for(int i=0;i<temp.size();i++)
            {
                originalContent.appendText(Integer.toString(temp.get(i)));
            }
        });

        compress.setOnAction((ActionEvent e) ->{
            compressedContent.clear();
            if(file.exists())
            {
                ArrayList<Integer>data=formatContent(file);
                int bitsNum=data.get(data.size()-1);
                data.remove(data.size()-1);
                scalarQuantizers.compress(data,bitsNum);
            }
            else
            {
                String[] splitted=originalContent.getText().split(" ");
                ArrayList<Integer>data=new ArrayList<>();
                for(int i=0;i<splitted.length-1;i++)
                {
                    data.add(Integer.parseInt(splitted[i]));
                }
                scalarQuantizers.compress(data,Integer.parseInt(splitted[splitted.length-1]));
            }
            writeQQ();
            DirectoryChooser directoryChooser=new DirectoryChooser();
            directoryChooser.setTitle("Save to");
            File save;
            Stage stage=new Stage();
            save=directoryChooser.showDialog(stage);
            try {
                FileOutputStream fout= new FileOutputStream (save.getPath()+"/Compressed.txt");
                ObjectOutputStream oos = new ObjectOutputStream(fout);
                oos.writeObject(scalarQuantizers.compressed);
                fout.close();
                for(int i=0;i<scalarQuantizers.compressed.size();i++)
                {
                    compressedContent.appendText(Integer.toString(scalarQuantizers.compressed.get(i))+" ");
                }
            }
            catch (IOException io)
            {
                io.printStackTrace();
            }

        });
    }


    public static ArrayList<Integer> formatContent(File file)
    {
        Scanner scanner= null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {                  //read string from file
            e.printStackTrace();
        }
        String content=scanner.useDelimiter("\\Z").next();
        String[] splitted=content.split(" ");
        ArrayList<Integer>data=new ArrayList<>();
        for(int i=0;i<splitted.length;i++)
        {
            data.add(Integer.parseInt(splitted[i]));
        }
        return data;
    }

    public static void writeQQ()
    {
        try {
            File fileOne=new File("QQ.txt");
            FileOutputStream fos=new FileOutputStream(fileOne);
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(scalarQuantizers.QQ);        //write map in file
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
