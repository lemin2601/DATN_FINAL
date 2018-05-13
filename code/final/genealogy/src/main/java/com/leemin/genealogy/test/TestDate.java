package com.leemin.genealogy.test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestDate {
    public static void main(String[] args) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM/dd/yyyy");
        Date date;
        try {
             date = simpleDateFormat.parse("1995");
            System.out.println(simpleDateFormat1.format(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
