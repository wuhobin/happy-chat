package com.wuhobin.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Date工具类
 * @author 刘博
 */
@Slf4j
public class DateUtil {


    /**
     * 当前时间字符串
     */
    public static String getSimpleDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(new Date());
    }

    public static String getSimpleTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmmss");
        return dateFormat.format(new Date());
    }

    /**
     * 获取昨天的时间字符串
     */
    public static String getLastSimpleDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        return format.format(calendar.getTime());
    }
    /**
     * 获取前天的时间字符串
     */
    public static String getBeforeSimpleDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH,-2);
        return format.format(calendar.getTime());
    }

    /**
     * 获取昨天的时间字符串
     */
    public static String getLastSimpleHour(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHH");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY,-1);
        return format.format(calendar.getTime());
    }

    /**
     * 获取当前月字符串
     */
    public static String getMonthDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        String dateStr = format.format(date);
        return dateStr;
    }


    /**
     * 获取日期字符串
     */
    public static String getDateStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 获取时间字符串
     */
    public static String getDateTimeStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    /**
     * 获取小时格式
     * @return
     */
    public static String getSimpleHour(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH");
        return dateFormat.format(new Date());
    }

    /**
     * 获取导出日期格式
     * @return
     */
    public static String getExportDateStr(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmm");
        return dateFormat.format(new Date());
    }

    /**
     * 获取前天的时间字符串
     */
    public static String getLastSimpleDate(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date dateStr = null;
        try {
            dateStr = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateStr);
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        return format.format(calendar.getTime());
    }

    /**
     * 日期格式转换 将yyyyMMdd 转为yyyy/MM/dd
     */
    public static String changeDate(String date){
        String result=null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
            Date parse = format.parse(date);
            result = format2.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取明天的字符串
     * @return
     */
    public static String getTomSimpleDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date dateStr = null;
        try {
            dateStr = format.parse(getSimpleDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateStr);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        return format.format(calendar.getTime());
    }


    /**
     * 获取上周五的时间字符串
     */
    public static String getLastFridayDate(String dateStr){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH,-3);
        return format.format(calendar.getTime());
    }

    public static String getSimpleDate(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(date);
    }

    /**
     * 上个月的时间
     */
    public static String getLastMonthSimpleDate(){
        Date date = DateUtils.addDays(new Date(),-30);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(date);
    }

    public static Date getDate(String date,String pattern){
        try {
            SimpleDateFormat df= new SimpleDateFormat(pattern);
            return  df.parse(date);
        }
        catch (ParseException e) {
            log.error("转换日期类型出错!");
        }
        return null;
    }

    /**
     * 得到几天后的时间
     */
    public static Date getDateAfter(Date d,int day){
        Calendar now =Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE,now.get(Calendar.DATE)+day);
        return now.getTime();
    }

    /**
     * 获取几天前的时间
     */
    public static Date getDateBefore(Date d,int day){
        Calendar now =Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE,now.get(Calendar.DATE)-day);
        return now.getTime();
    }

    public static String getLastDateStr(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR,-1);
        return format.format(calendar.getTime());
    }

    public static String getLastDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR,-1);
        return format.format(calendar.getTime());
    }

    public static Date parseDateFormat(String date){
//        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        Date parse = null;
        try {
             parse = format1.parse(date);
        } catch (ParseException e) {
            return null;
        }
        return parse;
    }

    public static List<String> getPerDay(Date startTime, Date endTime) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //定义一个接受时间的集合
        List<String> lDate = new ArrayList<>();
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(startTime);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(endTime);
        // 测试此日期是否在指定日期之后
        while (endTime.after(calBegin.getTime()))  {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            lDate.add(format.format(calBegin.getTime()));
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
        }
        return lDate;
    }


    /**
     * 获取日期格式（月）
     * @return
     */
    public static String getSimpleMonth(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        return dateFormat.format(date);
    }





    /**
     * 根据指定时间获取周标识
     *
     * @param date
     * @return 示例  20200406-20200412
     */
    public static String getWeekTagOfCurday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int index = calendar.get(Calendar.DAY_OF_WEEK);

        Date monday = null;
        Date weekday = null;
        //判断今天是否周日
        if (index == 1) {
            monday = nextDate(date, -6);
            weekday = date;
        } else {
            Date weekStartTime = getWeekStartTime(date);
            monday = nextDate(weekStartTime, 1);
            weekday = nextDate(weekStartTime, 7);
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(monday) + "-" + df.format(weekday);
    }


    /**
     * 获取几天后的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date nextDate(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * 获取当周开始时间，精确到秒
     *
     * @param date
     * @return 当周开始的时间
     */
    public static Date getWeekStartTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(Calendar.DAY_OF_WEEK, 1);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    /**
     * 获取这周开始时间（周一开始）
     * @return
     */
    public static String getWeekBeginTime(Date date){
        DateTime dateTime = cn.hutool.core.date.DateUtil.beginOfWeek(date);
        String beginDate = dateTime.toString("yyyy-MM-dd");
        return beginDate;
    }

    /**
     * 获取这周结束时间（周天结束）
     * @param date
     * @return
     */
    public static String getWeekEndTime(Date date){
        DateTime dateTime = cn.hutool.core.date.DateUtil.endOfWeek(date);
        String endDate = dateTime.toString("yyyy-MM-dd");
        return endDate;
    }

    /**
     * 获取下一周的开始时间
     * @param date
     * @return
     */
    public static String getNextWeekBeginTime(Date date){
        DateTime dateTime = cn.hutool.core.date.DateUtil.beginOfWeek(date);
        DateTime nextDate = cn.hutool.core.date.DateUtil.offset(dateTime, DateField.DAY_OF_MONTH, 7);
        String nextBeginDate = nextDate.toString("yyyy-MM-dd");
        return nextBeginDate;
    }

    /**
     * 获取下一周的结束时间
     * @param date
     * @return
     */
    public static String getNextWeekEndTime(Date date){
        DateTime dateTime = cn.hutool.core.date.DateUtil.endOfWeek(date);
        DateTime nextDate = cn.hutool.core.date.DateUtil.offset(dateTime, DateField.DAY_OF_MONTH, 7);
        String nextEndDate = nextDate.toString("yyyy-MM-dd");
        return nextEndDate;
    }


    /**
     * 字符串转时间
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate){
        Date date = cn.hutool.core.date.DateUtil.parse(strDate, "yyyy-MM-dd HH:mm:ss");
        return date;
    }


    /**
     * 获取当前是这一周的第几天，从星期一开始计算
     * @param date
     * @return
     */
    public static int getWeekDay(Date date){
        Calendar calendar = Calendar.getInstance();
        //把当前时间赋给日历
        calendar.setTime(date);
        //获取到当前时间是本周的第几天
        int value = calendar.get(Calendar.DAY_OF_WEEK);
        //是星期天时,获取的value是1，星期六是7。
        if(value == 1){
            value = 8;
        }
        //此处按星期一是第一天计,所以value的值需要减1
        return value -1;
    }



    /**
     * 判断一个时间是否在一个时间段内
     * @param nowTime
     * @param beginTime
     * @param endTime
     * @return
     */
    public static int belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        if (date.before(begin)){
            //红包雨未开始
            return -1;
        }else if (date.after(begin) && date.before(end)){
            //红包雨正在进行
            return 0;
        }else {
            //红包雨已结束
            return 1;
        }
    }

    /**
     * 获取今天的时间
     *
     * @return
     */
    public static String getTodayStr() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(new Date());
    }




//    public static void main(String[] args) {
////        int status = belongCalendar(new Date(), new Date(), cn.hutool.core.date.DateUtil.parseDate("2021-09-30 11:00:00"));
////        long between = cn.hutool.core.date.DateUtil.between(new Date(), cn.hutool.core.date.DateUtil.parseDate("2021-09-30 11:00:00"), DateUnit.);
////        System.out.println(status);
//        String lastSimpleHour = getLastSimpleHour();
//        System.out.println(lastSimpleHour);
//    }


}
