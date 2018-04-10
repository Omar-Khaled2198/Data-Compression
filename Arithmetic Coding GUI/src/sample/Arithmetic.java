package sample;


import java.util.Arrays;
import java.util.HashMap;


public class Arithmetic
{
    public HashMap<Character,Range>orginalRanges;
    public String compressed;
    public String decompressed;
    public int size;

    Arithmetic()
    {
        compressed="";
        decompressed="";
        orginalRanges=new HashMap<>();
    }


    public void compress(String str)
    {
        size=str.length();
        int[] freq=new int[256];
        Arrays.fill(freq,0);
        for(int i=0;i<str.length();i++)
            freq[str.charAt(i)]++;

        double cumulative=0;
        orginalRanges=new HashMap<>();
        for(int i=0;i<256;i++)
        {
            if(freq[i]>0)
            {
                orginalRanges.put((char)i,new Range(cumulative,((double)freq[i]/str.length())+cumulative));
                System.out.println((char)i+" "+cumulative+" "+(((double)freq[i]/str.length())+cumulative));
                cumulative+=(double)freq[i]/str.length();
            }
        }
        System.out.println("================================================================");
        double lower=0,upper=1,range=1;
        for(int i=0;i<str.length();i++)
        {
            upper=lower+range*orginalRanges.get(str.charAt(i)).upper;
            lower=lower+range*orginalRanges.get(str.charAt(i)).lower;
            System.out.println(str.charAt(i)+" "+lower+" "+upper);
            range=upper-lower;
        }
        double value = Math.random() * (upper - lower) + lower;
        compressed=Double.toString(value);

    }

    public void decompress(String str)
    {
        double value=Double.parseDouble(str);
        char isSymbol='.';
        int counter=0;
        double lower=0,upper=1,range=1;
        do
        {
            for(Character K:orginalRanges.keySet())
            {

                if(orginalRanges.get(K).lower<=value&&orginalRanges.get(K).upper>=value)
                {
                    decompressed+=K;
                    upper=orginalRanges.get(K).upper;
                    lower=orginalRanges.get(K).lower;
                    range=upper-lower;
                    value=(value-lower)/range;
                    counter++;
                    break;
                }
            }
        }while (counter<size);
    }

}
