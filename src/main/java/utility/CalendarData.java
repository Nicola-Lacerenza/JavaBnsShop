package utility;

import models.Oggetti;
import org.json.JSONObject;
import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

public final class CalendarData implements Cloneable,Serializable,Comparable<CalendarData>, Oggetti<CalendarData> {

    @Serial
    private static final long serialVersionUID = 1L;
    private final int day;
    private final int month;
    private final int year;
    private final int hour;
    private final int minute;
    private final int second;
    private final int millisecond;
    public CalendarData(Calendar calendar){
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND);
        millisecond = calendar.get(Calendar.MILLISECOND);
    }

    public CalendarData(CalendarData original){
        this.day = original.day;
        this.month = original.month;
        this.year = original.year;
        this.hour = original.hour;
        this.minute = original.minute;
        this.second = original.second;
        this.millisecond = original.millisecond;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        super.clone();
        return new CalendarData(this);
    }

    @Override
    public String toString(){
        return printJSONObject().toString(4);
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null){
            return false;
        }
        if(this == obj){
            return true;
        }
        if(!(obj instanceof CalendarData)){
            return false;
        }
        CalendarData other = (CalendarData)(obj);
        return day == other.day &&
                month == other.month &&
                year == other.year &&
                hour == other.hour &&
                minute == other.minute &&
                second == other.second &&
                millisecond == other.millisecond;
    }

    @Override
    public int hashCode(){
        long hash = day + month + year + hour + minute + second + millisecond;
        if(hash >= Integer.MAX_VALUE){
            return Integer.MAX_VALUE;
        }else if(hash <= Integer.MIN_VALUE){
            return Integer.MIN_VALUE;
        }
        return (int)(hash);
    }

    @Override
    public int compareTo(CalendarData o){
        if(o == null){
            return -1;
        }
        if(this.equals(o)){
            return 0;
        }
        Calendar thisCalendar = getCalendar(day,month,year,hour,minute,second,millisecond);
        Calendar newCalendar = getCalendar(o.day,o.month,o.year,o.hour,o.minute,o.second,o.millisecond);
        return thisCalendar.compareTo(newCalendar);
    }

    public CalendarData copy(){
        return new CalendarData(this);
    }


    public JSONObject printJSONObject(){
        JSONObject output = new JSONObject();
        output.put("day",day);
        output.put("month",month);
        output.put("year",year);
        output.put("hour",hour);
        output.put("minute",minute);
        output.put("second",second);
        output.put("millisecond",millisecond);
        return output;
    }

    @Override
    public Optional<CalendarData> convertDBToJava(ResultSet row){
        return Optional.empty();
    }

    public Calendar getCalendar(){
        return getCalendar(day,month,year,hour,minute,second,millisecond);
    }
    private Calendar getCalendar(int day,int month,int year,int hour,int minute,int second,int millisecond){
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_MONTH,day);
        switch(month){
            case 1:
                calendar.set(Calendar.MONTH,Calendar.JANUARY);
                break;
            case 2:
                calendar.set(Calendar.MONTH,Calendar.FEBRUARY);
                break;
            case 3:
                calendar.set(Calendar.MONTH,Calendar.MARCH);
                break;
            case 4:
                calendar.set(Calendar.MONTH,Calendar.APRIL);
                break;
            case 5:
                calendar.set(Calendar.MONTH,Calendar.MAY);
                break;
            case 6:
                calendar.set(Calendar.MONTH,Calendar.JUNE);
                break;
            case 7:
                calendar.set(Calendar.MONTH,Calendar.JULY);
                break;
            case 8:
                calendar.set(Calendar.MONTH,Calendar.AUGUST);
                break;
            case 9:
                calendar.set(Calendar.MONTH,Calendar.SEPTEMBER);
                break;
            case 10:
                calendar.set(Calendar.MONTH,Calendar.OCTOBER);
                break;
            case 11:
                calendar.set(Calendar.MONTH,Calendar.NOVEMBER);
                break;
            case 12:
                calendar.set(Calendar.MONTH,Calendar.DECEMBER);
                break;
        }
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,second);
        calendar.set(Calendar.MILLISECOND,millisecond);
        return calendar;
    }
}