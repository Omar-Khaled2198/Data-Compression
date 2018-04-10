package sample;

import java.io.Serializable;

/**
 * Created by omar on 10/8/17.
 */
public class Tag implements Serializable
{
    public int position;
    public int length;
    public char nextChar;

    Tag(int pos,int len,char next)
    {
        position=pos;
        length=len;
        nextChar=next;
    }
}
