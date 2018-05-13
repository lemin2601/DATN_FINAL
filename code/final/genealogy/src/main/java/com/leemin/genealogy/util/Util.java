package com.leemin.genealogy.util;
import com.leemin.genealogy.data.GioiTinh;
import com.leemin.genealogy.data.QuanHe;
import com.leemin.genealogy.model.PeopleModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

public class Util {
    public  static SimpleDateFormat simpleDateFormatMonthDayYear = new SimpleDateFormat("MM/dd/yyyy");
//    public  static QuanHe getQuanHeChildFromParent(PeopleModel peopleModel){
//        QuanHe quanHe;
//        GioiTinh gioiTinh = GioiTinh.values()[peopleModel.getGender()];
//        switch (gioiTinh){
//
//        }
//    };

    public static String dateToString(Date date){
        if(date == null) return "?";
        return simpleDateFormatMonthDayYear.format(date);
    }
}
