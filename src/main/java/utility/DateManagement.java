package utility;

import java.util.Calendar;

public class DateManagement {

    public static String printDate(Calendar date){
        int giorno = date.get(Calendar.DAY_OF_MONTH);
        int mese = date.get(Calendar.MONTH)+1;
        int anno = date.get(Calendar.YEAR);
        int ora = date.get(Calendar.HOUR_OF_DAY);
        int minuti = date.get(Calendar.MINUTE);
        int secondi = date.get(Calendar.SECOND);
        int ms = date.get(Calendar.MILLISECOND);
        return String.format("%d-%d-%dT%d:%d:%d.%dZ",anno,mese,giorno,ora,minuti,secondi,ms);
    }
}
