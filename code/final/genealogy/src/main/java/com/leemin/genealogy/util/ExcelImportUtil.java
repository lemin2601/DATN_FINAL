package com.leemin.genealogy.util;
import com.leemin.genealogy.config.ConfigFormat;
import com.leemin.genealogy.data.GioiTinh;
import com.leemin.genealogy.data.QuanHe;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExcelImportUtil {
    public  static SimpleDateFormat simpleDateFormatYear = new SimpleDateFormat("yyyy");
    public  static SimpleDateFormat simpleDateFormatMonthYear = new SimpleDateFormat("MM/yyyy");
    public  static SimpleDateFormat simpleDateFormatMonthDayYear = new SimpleDateFormat("MM/dd/yyyy");

    public static QuanHe getQuanHe(Cell cell) {
        CellType cellType = cell.getCellTypeEnum();
        QuanHe result = QuanHe.CHA;
        switch (cellType) {
            case _NONE:
                break;
            case NUMERIC:
                break;
            case STRING:
                String value = cell.getStringCellValue()
                                   .toLowerCase();
                if (value.equalsIgnoreCase("cha")) {
                    result = QuanHe.CHA;
                }
                else if (value.equalsIgnoreCase("me") || value.equalsIgnoreCase("mẹ")) {
                    result = QuanHe.ME;
                }
                else if (
                        value.equalsIgnoreCase("chong") ||
                                value.equalsIgnoreCase("chồng") ||
                                value.equalsIgnoreCase("chông")
                        ) {
                    result = QuanHe.CHONG;
                }
                else if (
                        value.equalsIgnoreCase("vo") ||
                                value.equalsIgnoreCase("vợ") ||
                                value.equalsIgnoreCase("vơ")
                        ) {
                    result = QuanHe.VO;
                }
                break;
            case FORMULA:
                break;
            case BLANK:
                break;
            case BOOLEAN:
                break;
            case ERROR:
                break;
        }
        return result;
    }

    public  static String getImg(Cell cell){
        CellType cellType = cell.getCellTypeEnum();
        String result = "/img/avatar-default-unknown.png";
        switch (cellType) {
            case _NONE:
                break;
            case NUMERIC:
                break;
            case STRING:
                result = cell.getStringCellValue();
                break;
            case FORMULA:
                break;
            case BLANK:
                break;
            case BOOLEAN:
                break;
            case ERROR:
                break;
        }
        return result;
    }

    public static String getImgDefault(GioiTinh gioiTinh){
        switch (gioiTinh){
            case NAM:
                return "/img/avatar-default-nam.png";
            case NU:
                return "/img/avatar-default-nu.png";
            case KHONG_RO:
                return "/img/avatar-default-unknown.png";
        }
        return "/img/avatar-default-unknown.png";
    }

    public static String getImgDefault(int gioiTinh){
        return getImgDefault(GioiTinh.values()[gioiTinh]);
    }

    public static GioiTinh getGender(Cell cell){
        CellType cellType = cell.getCellTypeEnum();
        GioiTinh result = GioiTinh.KHONG_RO;
        switch (cellType) {
            case _NONE:
                break;
            case NUMERIC:
                break;
            case STRING:
                String gender = cell.getStringCellValue();
                gender = gender.trim().toLowerCase();
                if(gender.equalsIgnoreCase("nam") || gender.equalsIgnoreCase("male")){
                    result = GioiTinh.NAM;
                }else if(
                        gender.equalsIgnoreCase("nữ") ||
                        gender.equalsIgnoreCase("nu") ||
                        gender.equalsIgnoreCase("nư") ||
                                gender.equalsIgnoreCase("female")
                        ){
                    result = GioiTinh.NU;
                }else{
                    result = GioiTinh.KHONG_RO;
                }
                break;
            case FORMULA:
                break;
            case BLANK:
                break;
            case BOOLEAN:
                break;
            case ERROR:
                break;
        }
        return result;
    }
    public static Date getDate(String value){
        Date result = null;
        if(value.equals("?") ) return null;
        try {
            result = simpleDateFormatMonthDayYear.parse(value);
        } catch (ParseException e) {
            try {
                result = simpleDateFormatMonthYear.parse(value);
            } catch (ParseException e3) {
                try {
                    result = simpleDateFormatYear.parse(value);
                } catch (ParseException ignored) {
                }
            }
        }
        return result;
    }
    public static Date getDate(Cell cell){
        CellType cellType = cell.getCellTypeEnum();
        Date result = null;
        switch (cellType) {
            case _NONE:
                break;
            case NUMERIC:
                long value = (long) cell.getNumericCellValue();
                if(value>0 && value < 5000){
                    try {
                        result = simpleDateFormatYear.parse(value +"");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case STRING:
                result = getDate(cell.getStringCellValue());
                break;
            case FORMULA:
                break;
            case BLANK:
                break;
            case BOOLEAN:
                break;
            case ERROR:
                break;
        }
        return result;
    }
}
