package sample;


import java.io.Serializable;

public class Range implements Serializable
{
   public double lower;
   public double upper;

   Range(double l,double u)
   {
       lower=l;
       upper=u;
   }
}
