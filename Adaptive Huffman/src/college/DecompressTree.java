package college;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.HashMap;

public class DecompressTree
{
    public Node root;
    public boolean isFound;
    public Node Parent;
    public StringBuilder NYT;

    public HashMap<String, Character> table = new HashMap<>();
    public StringBuilder decompressed;

    DecompressTree()
    {
        root = new Node();
        root.nytNode();
        isFound = false;
        for(int i=0;i<=127;i++)
          table.put("0"+Integer.toBinaryString(i),(char)i);
        decompressed = new StringBuilder();
        Parent=null;
        NYT=new StringBuilder();
    }

    public void search(String code, Node node)
    {
        if (node == null)
            return;

        search(code, node.left);

        search(code, node.right);

        if (node.type==1&&node.code.toString().equals(code))
        {
            isFound = true;
            return;
        }
    }

    public void updateCode(Node node)
    {
        if(node==null)
            return;

        if(node.left!=null&&node.right!=null&&node!=root)
        {
                char c;
                if(!node.code.toString().equals(node.left.code.substring(0,node.code.length()-1)))
                {
                    c=node.left.code.charAt(node.left.code.length()-1);
                    node.left.code=new StringBuilder();
                    node.left.code.append(node.code.toString());
                    node.left.code.append(c);
                }
                if(!node.code.toString().equals(node.right.code.substring(0,node.code.length()-1)))
                {
                    c=node.right.code.charAt(node.right.code.length()-1);
                    node.right.code=new StringBuilder();
                    node.right.code.append(node.code.toString());
                    node.right.code.append(c);
                }
        }

        updateCode(node.left);
        updateCode(node.right);
    }

    public  void updateNode(String code,Node node)
    {
        if (node == null)
            return;

        updateNode(code,node.left);
        updateNode(code,node.right);

        if(node.type==1&&node.code.toString().equals(code.toString()))
        {
            decompressed.append(node.symbol);
            Parent=node;
            return;
        }
    }

    public void Update() {
        swaping1(root);
        Parent.counter++;
        getParent(Parent, root);
        while (Parent != null) {
            swaping2(root);
            incCounter(Parent,root);
            if(Parent==root)
                break;
            getParent(Parent, root);
        }
        updateNYT(root);
        updateCode(root);
        Parent = null;
    }

    public  <T> T getItself(T itself, T dummy)
    {
        return itself;
    }

    public void swaping1(Node node)
    {
        if (node == null)
            return;

        if(node.left!=null&&node.right!=null)
        {
            Node temp=node.right;
            if(temp.right!=Parent&&temp.left!=Parent&&Parent.number<temp.number&&Parent.counter>=temp.counter)
            {
                Parent.symbol = getItself(temp.symbol, temp.symbol = Parent.symbol);
                Parent.type = getItself(temp.type, temp.type = Parent.type);
                Parent.right = getItself(temp.right, temp.right = Parent.right);
                Parent.left = getItself(temp.left, temp.left = Parent.left);
                Parent=temp;
                return;
            }
            temp=node.left;
            if(temp.right!=Parent&&temp.left!=Parent&&Parent.number<temp.number&&Parent.counter>=temp.counter)
            {
                Parent.symbol = getItself(temp.symbol, temp.symbol = Parent.symbol);
                Parent.type = getItself(temp.type, temp.type = Parent.type);
                Parent.right = getItself(temp.right, temp.right = Parent.right);
                Parent.left = getItself(temp.left, temp.left = Parent.left);
                Parent=temp;
                return;
            }
        }
        if(node.left!=null&&node.left.type==0)
            swaping1(node.left);
        else if(node.right!=null&&node.right.type==0)
            swaping1(node.right);

    }

    public  void getParent(Node specific,Node node)
    {
        if (node == null)
            return;

        if(node.left==specific||node.right==specific)
        {
            Parent=node;
            return;
        }

        getParent(specific,node.left);
        getParent(specific,node.right);

    }

    public  void swaping2(Node node)
    {
        if (node == null)
            return;

        if(node.left!=null&&node.right!=null)
        {
            Node temp;
            if(node.left==Parent) {
                temp = node.right;
                if (Parent.counter >= temp.counter && Parent.number < temp.number) {
                    Parent.symbol = getItself(temp.symbol, temp.symbol = Parent.symbol);
                    Parent.type = getItself(temp.type, temp.type = Parent.type);
                    Parent.right = getItself(temp.right, temp.right = Parent.right);
                    Parent.left = getItself(temp.left, temp.left = Parent.left);
                    Parent=temp;
                    return;
                }
            }
            else if(node.right==Parent) {
                temp = node.left;
                if (Parent.counter >= temp.counter && Parent.number < temp.number) {
                    Parent.symbol = getItself(temp.symbol, temp.symbol = Parent.symbol);
                    Parent.type = getItself(temp.type, temp.type = Parent.type);
                    Parent.right = getItself(temp.right, temp.right = Parent.right);
                    Parent.left = getItself(temp.left, temp.left = Parent.left);
                    Parent=temp;
                    return;
                }
            }
        }
        if(node.left!=null&&node.left.type==0)
            swaping2(node.left);
        else if(node.right!=null&&node.right.type==0)
            swaping2(node.right);

    }


    public  void addNode(char symbol)
    {
        Node curr=root;
        while (true)
        {
            if(curr!=null)
            {
                if((curr.left==null&&curr.right==null&&curr.type==0))
                {
                    Node right=new Node();
                    right.symbolNode(symbol);
                    Node left=new Node();
                    left.nytNode();
                    if(curr==this.root)
                    {
                        right.code.append("1");
                        left.code.append("0");
                    }
                    else
                    {
                        right.code.append(curr.code+"1");
                        left.code.append(curr.code+"0");
                    }
                    //System.out.println(left.code);
                    curr.right=right;
                    curr.left=left;
                    curr.counter++;
                    getParent(curr,root);
                    while(Parent!=null)
                    {
                        swaping2(root);
                        incCounter(Parent,root);
                        if(Parent==root)
                            break;
                        getParent(Parent,root);
                    }
                    updateNYT(root);
                    updateCode(root);
                    NYT=new StringBuilder();
                    NYT.append(left.code.toString());
                    Parent=null;
                    break;
                }
                else
                {
                    if(curr.left!=null&&curr.left.type==0)
                        curr=curr.left;
                    else if(curr.right!=null&&curr.right.type==0)
                        curr=curr.right;
                }
            }
        }
    }


    public void incCounter(Node specfic,Node node)
    {
        if (node == null)
            return;

        incCounter(specfic,node.left);
        incCounter(specfic,node.right);

        if(node.left==specfic||node.right==specfic)
        {
            specfic.counter++;
            return;
        }
    }

    public  void updateNYT(Node node)
    {
        if (node == null)
            return;
        updateNYT(node.left);
        updateNYT(node.right);

        if(node.left!=null&&node.right!=null&&node.type==0)
            node.counter=node.left.counter+node.right.counter;
    }
}