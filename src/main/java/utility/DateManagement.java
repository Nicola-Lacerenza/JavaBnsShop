package utility;

import org.json.JSONException;
import org.json.JSONObject;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;
public final class DateManagement{
    private DateManagement(){}

    public static Calendar copyCalendar(Calendar calendar){
        if(calendar == null){
            calendar = new GregorianCalendar();
        }
        CalendarData calendarData = new CalendarData(calendar);
        return calendarData.copy().getCalendar();
    }

    public static JSONObject printJSONCalendar(Calendar calendar){
        if(calendar == null){
            calendar = new GregorianCalendar();
        }
        CalendarData calendarData = new CalendarData(calendar);
        return calendarData.printJSONObject();
    }

    public static Calendar fromDatabaseToCalendar(Timestamp timestamp){
        if(timestamp == null){
            return new GregorianCalendar();
        }
        Calendar calendar = new GregorianCalendar();
        long milliseconds = timestamp.getTime();
        calendar.setTimeInMillis(milliseconds);
        return calendar;
    }

    public static Optional<Timestamp> fromStringToTimestamp(JSONObject data,
                                                            String keyToExtract,
                                                            String pattern,
                                                            boolean keepTime) throws SQLException{

        if(data == null || keyToExtract == null || pattern == null){
            return Optional.empty();
        }
        if(keyToExtract.isEmpty() || pattern.isEmpty()){
            return Optional.empty();
        }
        if(!data.keySet().contains(keyToExtract)){
            return Optional.empty();
        }
        String descriptionDate = extractFieldToJSONObject(data,keyToExtract);
        SimpleDateFormat parser = new SimpleDateFormat(pattern);
        Calendar date = parseCalendar(parser,descriptionDate);
        if(!keepTime){
            date.set(Calendar.HOUR_OF_DAY,0);
            date.set(Calendar.MINUTE,0);
            date.set(Calendar.SECOND,0);
            date.set(Calendar.MILLISECOND,0);
        }
        Timestamp timestamp = new Timestamp(date.getTimeInMillis());
        return Optional.of(timestamp);
    }

    private static String extractFieldToJSONObject(JSONObject data,String keyToExtract) throws SQLException{
        try{
            return data.getString(keyToExtract);
        }catch(JSONException exception){
            exception.printStackTrace();
            throw new SQLException(exception.getMessage(),exception);
        }
    }

    private static Calendar parseCalendar(SimpleDateFormat parser,String descriptionDate) throws SQLException{
        Calendar date = new GregorianCalendar();
        try{
            date.setTime(parser.parse(descriptionDate));
        }catch(ParseException exception){
            exception.printStackTrace();
            throw new SQLException(exception.getMessage(),exception);
        }
        return date;
    }

    public static Timestamp fromCalendarToDatabase(Calendar calendar){
        return new Timestamp(calendar.getTimeInMillis());
    }
}