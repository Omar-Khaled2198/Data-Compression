package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Created by omar on 10/8/17.
 */
public class Compress implements Initializable {


    File file=new File("");

    @FXML
    private Button browse;

    @FXML
    private TextFlow originalContent;

    @FXML
    private TextFlow compressedContent;

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
            originalContent.getChildren().add(new Text(formatContent(file)));
        });

        compress.setOnAction((ActionEvent e) ->{

            ArrayList<Tag>tags= (ArrayList<Tag>) compression(formatContent(file));
            DirectoryChooser directoryChooser=new DirectoryChooser();
            directoryChooser.setTitle("Save to");
            File save=directoryChooser.showDialog(new Stage());
            for (int i = 0; i < tags.size(); i++) {
                String tag = Integer.toString(tags.get(i).position) + " " + Integer.toString(tags.get(i).length) + " " + Character.toString(tags.get(i).nextChar) + " ";
                compressedContent.getChildren().add(new Text(tag));
            }
            try (FileWriter file = new FileWriter(save.getPath())) {
                for(int i = 0 ; i < tags.size() ; ++i){
                    file.write((byte)tags.get(i).position);
                    file.write((byte)tags.get(i).length);
                    int x = (int)tags.get(i).nextChar;
                    file.write((byte)x);
                }
                file.close();
            }
            catch(IOException IO){
                IO.printStackTrace();
                System.out.print("Exception");
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

    static int ind=-1;

    public static boolean search(int begIndex,int endIndex,String content,String subContent)
    {
        if(begIndex+subContent.length()>content.length())
            return false;
        if(content.substring(begIndex,begIndex+subContent.length()).equals(subContent))
        {
            ind=begIndex;
            return true;
        }
        return search(begIndex+1,endIndex,content,subContent);
    }

    public static List<Tag> compression(String content)
    {
        List<Tag>tags=new ArrayList<Tag>();
        boolean found=false;
        for(int i=0;i<=content.length();i++)
        {
            String subContent = null;
            int tempIndex=i;
            for(int x=i+1;x<=content.length();x++)
            {
                String check=content.substring(i,x);
                tempIndex=i;
                if(tempIndex-check.length()>=0&&content.substring(tempIndex-check.length(),tempIndex).equals(check))
                {
                    while (true)
                    {
                        if(tempIndex+ check.length() <=content.length()&&content.substring(tempIndex,tempIndex+check.length()).equals(check))
                        {

                            tempIndex+=check.length();
                        }
                        else
                            break;
                    }
                    String tempString=content.substring(i,tempIndex);
                    char next;
                    if (tempIndex < content.length())
                        next = content.charAt(tempIndex );
                    else
                        next = '/';
                    tags.add(new Tag(check.length(), tempString.length(), next));
                    i += tempString.length();
                    found=true;
                    break;

                }
            }
            if(!found){
                for (int x = i + 1; x <= content.length(); x++) {
                    subContent = content.substring(i, x);
                    if (!search(0, content.substring(0, i).length(), content.substring(0, i), subContent)) {
                        if (ind == -1)
                            ind = 0;
                        else
                            ind = i - ind;
                        tags.add(new Tag(ind, subContent.length() - 1, subContent.charAt(subContent.length() - 1)));
                        i += subContent.length() - 1;
                        found = true;
                        break;
                    }
                }
                if (!found && subContent != null) {
                    if (ind == -1)
                        ind = 0;
                    else
                        ind = i - ind;
                    Tag tag = new Tag(ind, subContent.length(), '/');
                    tags.add(tag);
                    i += subContent.length() - 1;
                }
                found = false;
                ind = -1;
            }
            found = false;
        }
        return tags;
    }
}
