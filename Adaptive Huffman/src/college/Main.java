package college;


import java.util.Scanner;

public class Main {

    public static void printPostorder(Node node)
    {
        if (node == null)
            return;
        printPostorder(node.left);
        printPostorder(node.right);
        System.out.println(node.symbol+" "+node.number+" "+node.counter+" "+node.code);
    }



    public static void main(String[] args)
    {
	    CompressTree cTree=new CompressTree();
        System.out.print("Input : ");
	    String s=new Scanner(System.in).next();
	    for(int i=0;i<s.length();i++)
        {
            cTree.search(s.charAt(i),cTree.root);
            if(cTree.isFound)
            {
                cTree.updateNode(s.charAt(i),cTree.root);
                cTree.Update();
            }
            else
                cTree.addNode(s.charAt(i));
            cTree.isFound=false;

        }
        //printPostorder(cTree.root);
	    //System.out.println(cTree.compressed);
	    String str=cTree.compressed.toString();
	    Node.temp=100;
	    Node.count=0;
        DecompressTree dTree=new DecompressTree();
        boolean check=false,NYT=false;
        for(int i=0;i<str.length();i++)
        {
            //System.out.println(dTree.NYT);
            if(dTree.NYT.toString().length()!=0&&i+dTree.NYT.toString().length()<=str.length())
            {

                String subString=str.substring(i,i+dTree.NYT.length());
                if(subString.equals(dTree.NYT.toString()))
                {
                    i+=dTree.NYT.length()-1;
                    check=true;
                    NYT=true;
                    dTree.NYT=new StringBuilder();
                }
            }
            if(check==false)
            {
                if(i+8<=str.length())
                {
                    String subString=str.substring(i,i+8);
                    if(dTree.table.containsKey(subString))
                    {
                        if(i==0||(i!=0&&NYT==true)){
                        dTree.decompressed.append(dTree.table.get(subString));
                        dTree.addNode(dTree.table.get(subString));
                        dTree.table.remove(subString);
                        i+=7;
                        NYT=false;
                        check=true;}

                    }

                }
            }
            if(check==false)
            {
                for(int x=i+1;x<=str.length();x++)
                {
                    String subString=str.substring(i,x);
                    dTree.search(subString,dTree.root);
                    if(dTree.isFound)
                    {
                        //System.out.println(subString);
                        dTree.updateNode(subString,dTree.root);
                        dTree.Update();
                        i+=subString.length()-1;
                        break;
                    }
                }
            }
            dTree.isFound=false;
            check=false;



        }
        System.out.println("Compressed : "+cTree.compressed);
        System.out.println("Decompressed : "+dTree.decompressed);
        System.out.println("Correct ? "+dTree.decompressed.toString().equals(s));
        //printPostorder(dTree.root);




    }
}
