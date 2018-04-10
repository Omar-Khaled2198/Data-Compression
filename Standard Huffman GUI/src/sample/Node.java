package sample;

public class Node
{
    public Character symbol;
    public StringBuilder code;
    public int counter;
    public int type;

    public Node left,right;

    Node()
    {
        this.left=this.right=null;
    }

    public void symbolNode(char symbol,int freq)
    {
        this.symbol=symbol;
        type=1;
        code=new StringBuilder();
        left=right=null;
        counter=freq;

    }

    public void sumNode()
    {
        this.symbol='.';
        counter=0;
        code=new StringBuilder();
        left=right=null;
        type=0;
    }
}
