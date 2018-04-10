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


    File file=new File("");

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
            directoryField.setText(file.getPath().toString());
            originalContent.setText(formatContent(file));
        });

        compress.setOnAction((ActionEvent e) ->{
            compressedContent.clear();
            ArrayList<Integer>tags;
            if(file.exists())
                tags= (ArrayList<Integer>) compression(formatContent(file));
            else
                tags= (ArrayList<Integer>) compression(originalContent.getText().toString());
            DirectoryChooser directoryChooser=new DirectoryChooser();
            directoryChooser.setTitle("Save to");
            File save=new File("");
            Stage stage=new Stage();
            save=directoryChooser.showDialog(stage);
            try {
                DataOutputStream dataInputStream=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(save.getPath() + "/Compressed "+file.getName())));
                for (int i = 0; i < tags.size(); i++) {
                    dataInputStream.writeInt(tags.get(i));
                    compressedContent.appendText(tags.get(i).toString()+"\n");
                }
                dataInputStream.close();
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


    public static ArrayList<Integer> compression(String content)
    {
        ArrayList<Integer> tags = new ArrayList<Integer>();
        boolean found = false;
        LinkedHashMap<String,Integer> dictionary=new LinkedHashMap<String,Integer>();
        int last=128;
        for (int i = 0; i <= content.length(); i++)
        {
            for (int x = i + 1; x <= content.length(); x++)
            {
                String subContent =content.substring(i,x);

                if(subContent.length()>1&&!dictionary.containsKey(subContent))
                {
                    if(subContent.substring(0,subContent.length()-1).length()>1)
                        tags.add(dictionary.get(subContent.substring(0,subContent.length()-1)));
                    else
                        tags.add((int)subContent.charAt(0));
                    dictionary.put(subContent,last);
                    last++;
                    i+=subContent.length()-2;
                    found=true;
                    break;
                }

            }
            if (!found&&content.substring(i).length()>1) {
                tags.add(dictionary.get(content.substring(i)));
                i+=content.substring(i).length()-1;
            }
            else if(!found&&content.substring(i).length()==1)
            {
                tags.add((int)content.substring(i).charAt(0));
                i++;
            }
            found = false;
        }
        return tags;
    }
}
