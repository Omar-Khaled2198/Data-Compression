package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Created by omar on 10/8/17.
 */
public class Decompress implements Initializable {

    File file=new File("");

    @FXML
    private Button browse;

    @FXML
    private TextFlow content;;

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
            directoryField.setText(file.getPath().toString());
            ArrayList<Tag>tags=format(file);
            for(int i=0;i<tags.size();i++)
                System.out.println(tags.get(i).position+" "+tags.get(i).length+tags.get(i).nextChar);
        });
    }

    public static ArrayList<Tag> format(File file)
    {
        ArrayList<Tag>tags=new ArrayList<Tag>();
        int pos=0,len=0;String next="";
        try {
            Scanner s = new Scanner(file);
            s.useDelimiter(""); // reads one char a time
            while(s.hasNext())
            {
                if (s.hasNextInt())
                    pos=s.nextInt();
                if(s.hasNextInt())
                    len=s.nextInt();
                else
                    next=s.next();
                System.out.println(pos+" "+len+" "+next);
            }
        }
        catch (IOException io)
        {
            io.printStackTrace();
        }
        return tags;
    }

    public static ArrayList<Character> decompression(ArrayList<Tag> tags)
    {
        ArrayList<Character>orignalContent=new ArrayList<Character>();
        for(int i=0;i<tags.size();i++)
        {
            if(tags.get(i).position==0)
                orignalContent.add(tags.get(i).nextChar);
            else {
                int counter = tags.get(i).length, x =orignalContent.size()-tags.get(i).position;
                while (counter > 0)
                {
                    orignalContent.add(orignalContent.get(x));
                    counter--;
                    x++;
                }
                orignalContent.add(tags.get(i).nextChar);

            }
        }
        return orignalContent;
    }
}
