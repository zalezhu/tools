package com.zale.shortlink.util;

import org.apache.commons.lang.math.RandomUtils;

import java.util.Random;

/**
 * Created by Zale on 16/8/22.
 */
public class SystemUtil {

    private static char[] digits = { '!',
            '0' , '1' , '2' , '3' , '4' , '5' , '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '@'
    };

    private static char[] random_digits = {
            '0' , '1' , '2' , '3' , '4' , '5' ,
            '6' , '7' , '8' , '9' , 'a' , 'b' ,
            'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
            'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
            'o' , 'p' , 'q' , 'r' , 's' , 't' ,
            'u' , 'v' , 'w' , 'x' , 'y' , 'z' ,
            'A' , 'B' , 'C' , 'D' , 'E' , 'F' ,
            'G' , 'H' , 'I' , 'J' , 'K' , 'L' ,
            'M' , 'N' , 'O' , 'P' , 'Q' , 'R' ,
            'S' , 'T' , 'U' , 'V' , 'W' , 'X' ,
            'Y' , 'Z' ,'0','1'
    };
    public static String DecimalToSystem64(long i){
        return toUnsignedString(i,6,digits);
    }


    private static String toUnsignedString(long i, int shift,char[] di) {
        char[] buf = new char[64];
        int charPos = 64;
        int radix = 1 << shift;
        long mask = radix - 1;
        do {
            buf[--charPos] = di[(int)(i & mask)];
            i >>>= shift;
        } while (i != 0);
        return new String(buf, charPos, (64 - charPos));
    }



    public static String genRandomCode(int length){
        Double minNo = Math.pow(64,length-1);
        Double maxNo = Math.pow(64, length);
        Random r = new Random();
        long rVal = Math.abs(r.nextLong());
        while(rVal<minNo){
            System.out.println(rVal);
            rVal = rVal<<6;
        }
        while(rVal>maxNo){
            rVal = rVal>>3;
        }
        return toUnsignedString(rVal,6,random_digits);
    }


}
