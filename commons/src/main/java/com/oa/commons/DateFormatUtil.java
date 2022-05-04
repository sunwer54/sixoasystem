package com.oa.commons;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
  封装一个更改日期的格式的工具类
 */
public class DateFormatUtil {
    private DateFormatUtil(){

    }
    public static String changeDateFormate(Date date){
		//创建日期格式转化对象，并指定要转化成的日期格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if(date !=null) {
			//传入Date对象，把Date日期对象转化成指定的日期格式，并返回该日期格式的字符串对象
            String hdate = simpleDateFormat.format(date);
            return hdate;
        }
        return null;
    }
}
