package sample;

import com.sun.corba.se.spi.presentation.rmi.IDLNameTranslator;
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
import jdk.nashorn.internal.runtime.regexp.joni.encoding.CharacterType;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
    private TextArea content;;

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
        });

        decompress.setOnAction(e->{
            content.clear();
            String originalContent=decompression(format(file)).toString();
            DirectoryChooser directoryChooser=new DirectoryChooser();
            directoryChooser.setTitle("Save to");
            File save=new File("");
            save=directoryChooser.showDialog(new Stage());
            try {
                FileWriter fileWriter=new FileWriter(save.getPath() + "/Original " + file.getName());
                content.appendText(originalContent);
                fileWriter.write(originalContent);
                fileWriter.close();
                }
            catch (IOException io)
            {
                io.printStackTrace();
            }
        });
    }

    public static ArrayList<Integer> format(File file)
    {
        ArrayList<Integer>tags=new ArrayList<Integer>();
        int tag;
        try {
            DataInputStream dataInputStream=new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            try {
                while (true) {
                    tag = dataInputStream.readInt();
                    tags.add(tag);
                }
            }
            catch (EOFException eof)
            {
                dataInputStream.close();
            }

        }
        catch (IOException io)
        {
            io.printStackTrace();
        }
        return tags;
    }

    public static StringBuilder decompression(ArrayList<Integer> tags)
    {
        LinkedHashMap<Integer,String> dictionary=new LinkedHashMap<>();
        StringBuilder orignalContent=new StringBuilder();
        int indexer=0,last=128;
        for(int i=0;i<tags.size();i++)
        {
            if(tags.get(i)<last)
            {
                if(tags.get(i)>127)
                    orignalContent.append(dictionary.get(tags.get(i)));
                else
                    orignalContent.append((char)tags.get(i).intValue());
                for(int x=indexer+1;x<=orignalContent.length();x++)
                {
                    String subContent =orignalContent.substring(indexer,x);
                    if(subContent.length()>1&&!dictionary.containsValue(subContent))
                    {
                        dictionary.put(last,subContent);
                        last++;
                        indexer+=subContent.length()-1;
                        break;
                    }
                }
            }
            else
            {
                String subContent;
                if(tags.get(i-1)>127)
                    subContent=dictionary.get(tags.get(i-1))+orignalContent.charAt(indexer);
                else
                    subContent=Character.toString((char)(tags.get(i-1).intValue()))+orignalContent.charAt(indexer);
                dictionary.put(last,subContent);
                last++;
                indexer+=subContent.length()-1;
                orignalContent.append(subContent);
            }
        }
        return orignalContent;
    }
}
