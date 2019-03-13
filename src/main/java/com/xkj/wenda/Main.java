package com.xkj.wenda;

import java.util.Scanner;



public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        s.toLowerCase();
        int l=s.length();
        for(int i=1;i<l;i++){
           String b = s.substring(i);
           if((s.substring(i+1,l)).contains(b)){
               if(b.compareTo(s.substring(i+1))<=0){

                   System.out.println(s.substring(1,2));

               }else{
                   s = s.substring(i+1,l);
               }

           }
           l=s.length();
        }
        s.toLowerCase();
        //System.out.println(s.substring(1,2));

    }
}