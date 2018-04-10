package college;

/**
 * Created by omar on 10/18/17.
 */
public class Node
{
    public Character symbol;
    public StringBuilder code;
    public int number;
    public int counter;
    public int type;
    public static int count=0;
    public static int temp=100;

    public Node left,right;

    Node()
    {
        this.left=this.right=null;
    }

    public void symbolNode(char symbol)
    {
        this.symbol=symbol;
        type=1;
        code=new StringBuilder();
        count++;
        number=temp;
        temp--;
        left=right=null;
        counter=1;

    }

    public void nytNode()
    {
        this.symbol='.';
        count++;
        counter=0;
        code=new StringBuilder();
        number=temp;
        temp--;
        left=right=null;
        type=0;
    }
}
