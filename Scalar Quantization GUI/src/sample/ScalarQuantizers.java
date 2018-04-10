package sample;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ScalarQuantizers
{
    public ArrayList<Integer>compressed;
    public ArrayList<Integer>decompressed;
    public HashMap<Integer,Integer>QQ;

    public ScalarQuantizers()
    {
        compressed=new ArrayList<>();
        decompressed=new ArrayList<>();
        QQ=new HashMap<>();
    }

    public int calcAverage(ArrayList<Integer>numbers,int beg,int end)
    {
        int average=0;
        for(int i=beg;i<=end;i++)
           average+=numbers.get(i);
        average=(int)Math.ceil(((double) average/(double) ((end-beg)+1)));
        return average;
    }

    public void compress(ArrayList<Integer>data,int bitsNum)
    {
        ArrayList<Integer>numbers=new ArrayList<>(data);
        Collections.sort(numbers);
        ArrayList<Index>indexs=new ArrayList<>();
        indexs.add(new Index(0,numbers.size()-1));
        int count,average;
        while (indexs.size()<Math.pow(2,bitsNum))
        {
            average=calcAverage(numbers,indexs.get(0).beg,indexs.get(0).end);
            for(int x=0;x<numbers.size();x++)
            {
                if(numbers.get(x)>=average)
                {

                    indexs.add(new Index(indexs.get(0).beg,x-1));
                    indexs.add(new Index(x,indexs.get(0).end));
                    break;
                }
            }
            indexs.remove(0);
        }/*
        for(int i=0;i<indexs.size();i++)
        {
            for(int x=indexs.get(i).beg;x<=indexs.get(i).end;x++)
                System.out.print(numbers.get(x)+" ");
            System.out.println();
        }*/
        HashMap<Integer,Range> Quantizer=new HashMap<>();
        ArrayList<Integer>averages=new ArrayList<>();
        for(int i=0;i<indexs.size();i++)
        {
            average=calcAverage(numbers,indexs.get(i).beg,indexs.get(i).end);
            averages.add(average);
        }
        int temp=0,Q=0;
        for(int i=0;i<averages.size()-1;i++)
        {
            Quantizer.put(Q,new Range(temp,(averages.get(i+1)+averages.get(i))/2));
            temp=((averages.get(i+1)+averages.get(i))/2)+1;
            QQ.put(Q,averages.get(i));
            Q++;
        }
        Quantizer.put(Q,new Range(temp,numbers.get(numbers.size()-1)));
        QQ.put(Q,averages.get(averages.size()-1));

        for(int i=0;i<data.size();i++)
        {
            for(Integer r:Quantizer.keySet())
            {
                if(Quantizer.get(r).lower<=data.get(i)&&data.get(i)<=Quantizer.get(r).upper)
                {
                    compressed.add(r);
                    break;
                }
            }
        }
        System.out.println("Range Q");
        for(Integer i:Quantizer.keySet())
        {
            System.out.println(Quantizer.get(i).lower+" "+Quantizer.get(i).upper+" "+i);
        }
        System.out.println("Q Q^-1");
        for(Integer i:QQ.keySet())
        {
            System.out.println(i+" "+QQ.get(i));
        }
    }
    public void decompress(ArrayList<Integer>compressedData)
    {
        for(int i=0;i<compressedData.size();i++)
        {
            decompressed.add(QQ.get(compressedData.get(i)));
        }
    }
}
