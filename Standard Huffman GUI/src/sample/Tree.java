package sample;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

class CustomComparator implements Comparator<Node> {

    @Override
    public int compare(Node n1, Node n2) {
        if(n1.counter<n2.counter)
            return -1;
        else
            return 1;
    }
}

public class Tree
{
    public  Node root;

    public  HashMap<Character,String>shortCode;

    public String compressed;

    Tree()
    {
        root=null;
        shortCode=new HashMap<>();
        compressed="";
    }

    public  void coding(Node node)
    {
        if(node==null)
            return;
        if(node==root)
        {
            if(node.left!=null)
            {
                node.left.code.append("1");
                if(node.left.type==1)
                    shortCode.put(node.left.symbol,node.left.code.toString());

            }
            if(node.right!=null)
            {
                node.right.code.append("0");
                if(node.right.type==1)
                    shortCode.put(node.right.symbol,node.right.code.toString());
            }
        }
        else
        {
            if(node.left!=null&&node.left.code.length()==0)
            {
                node.left.code.append(node.code.toString()+"0");
                if(node.left.type==1)
                    shortCode.put(node.left.symbol,node.left.code.toString());

            }
            if(node.right!=null&&node.right.code.length()==0)
            {
                node.right.code.append(node.code.toString()+"1");
                if(node.right.type==1)
                    shortCode.put(node.right.symbol,node.right.code.toString());
            }
        }

        coding(node.left);
        coding(node.right);
    }


    public  void compress(String content)
    {
        HashMap<Character,Integer>freq=new HashMap<>();
        for(int i=0;i<content.length();i++)
        {
            if (freq.containsKey(content.charAt(i)))
                freq.put(content.charAt(i),freq.get(content.charAt(i))+1);
            else
                freq.put(content.charAt(i),1);

        }

        ArrayList<Node>nodes=new ArrayList<>();
        for(Character k:freq.keySet())
        {
            Node newNode=new Node();
            newNode.symbolNode(k,freq.get(k));
            nodes.add(newNode);
        }
        Collections.sort(nodes,new CustomComparator());

        boolean check=false;
        while (true)
        {
            if(nodes.size()==1)
            {
                Node newNode=new Node();
                newNode.sumNode();
                newNode.right=nodes.get(0);
                root=newNode;
                break;
            }
            for(int i=0;i<nodes.size();i++)
            {
                if(i+1<nodes.size()){
                    Node newNode=new Node();
                    newNode.sumNode();
                    newNode.left=nodes.get(i);
                    newNode.right=nodes.get(i+1);
                    newNode.counter=nodes.get(i).counter+nodes.get(i+1).counter;
                    root=newNode;
                    nodes.add(newNode);
                    nodes.remove(i+1);
                    nodes.remove(i);
                    check=true;
                    break;
                }
            }
            Collections.sort(nodes,new CustomComparator());
            //for(int i=0;i<arrayList.size();i++)
            //   System.out.print(arrayList.get(i).counter+" ");
            if(!check)
                break;
            check=false;

        }
        coding(root);
        for(int i=0;i<content.length();i++)
            compressed+=shortCode.get(content.charAt(i));
    }

    public String decompress(String compressedContent)
    {
        String original="";
        for(int i=0;i<compressedContent.length();i++)
        {
            for(Character K:shortCode.keySet())
            {
                if(i+shortCode.get(K).length()<=compressedContent.length())
                {
                    String subString=compressedContent.substring(i,i+shortCode.get(K).length());
                    if(subString.equals(shortCode.get(K)))
                    {
                        original+=K;
                        i+=subString.length()-1;
                        break;
                    }
                }
            }
        }
        return original;
    }

    public  void printPostorder(Node node)
    {
        if (node == null)
            return;
        printPostorder(node.left);
        printPostorder(node.right);
        System.out.println(node.symbol+" "+node.code);
    }
}
