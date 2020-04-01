package ba.unsa.etf.rma.spirala.models;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class TransactionModel {

    private static ArrayList<Transaction> transactions = new ArrayList<>(
            Arrays.asList(new Transaction(getDate(2020,1,26),100, "Stipendija", Transaction.Type.REGULARINCOME, "Potrebno",30, getDate(2020,6,30)),
                    new Transaction(getDate(2019,1,27),-500, "Uplata za stanarinu", Transaction.Type.REGULARPAYMENT, "Prvo tromjesecje",90, getDate(2021,1,26)),
                    new Transaction(getDate(2020,3,17),200, "Takmicenje", Transaction.Type.INDIVIDUALINCOME, null,0, null),
                    new Transaction(getDate(2019,3,29),300, "Poklon", Transaction.Type.INDIVIDUALINCOME, null,0, null),
                    new Transaction(getDate(2020,1,29),-100, "Nabavka", Transaction.Type.INDIVIDUALPAYMENT, "Mjesecna",0, null),
                    new Transaction(getDate(2019,4,21),-100, "Poklon", Transaction.Type.REGULARPAYMENT, "Opis proizvoda",366, getDate(2020,4,21)),
                    new Transaction(getDate(2020,4,20),50, "Džeparac", Transaction.Type.REGULARINCOME, "Sweets",90, getDate(2020,8,4)),
                    new Transaction(getDate(2019,3,29),-80, "Kozmetika", Transaction.Type.PURCHASE, "-",0,null),
                    new Transaction(getDate(2020,1,17),-35, "Preparati", Transaction.Type.PURCHASE,"-",0,null),
                    new Transaction(getDate(2019,5,30),350, "Praksa", Transaction.Type.INDIVIDUALINCOME, null, 0, null),
                    new Transaction(getDate(2020,3,20),-120, "Pohod", Transaction.Type.INDIVIDUALPAYMENT, null,0, null),
                    new Transaction(getDate(2019,1,29),60, "Farmasi", Transaction.Type.REGULARINCOME, "Do isteka ugovora",20, getDate(2020,5,3)),
                    new Transaction(getDate(2020,5,29),-240, "Biciklo", Transaction.Type.INDIVIDUALPAYMENT, "-",0,null ),
                    new Transaction(getDate(2019,1,29),-70, "Režije", Transaction.Type.REGULARPAYMENT, "Struja, grijanje",30, getDate(2021,1,27)),
                    new Transaction(getDate(2020,1,29),30, "Ušteđevina", Transaction.Type.REGULARINCOME, "Za odmor",20, getDate(2020,7,26)),
                    new Transaction(getDate(2019,8,24),150, "Bajram", Transaction.Type.INDIVIDUALINCOME, null,0,null),
                    new Transaction(getDate(2020,1,13),300, "Pohod", Transaction.Type.INDIVIDUALINCOME, null,0,null),
                    new Transaction(getDate(2019,12,13),-70, "Odjeca", Transaction.Type.PURCHASE, "-",0,null),
                    new Transaction(getDate(2020,1,17),20, "Pohod", Transaction.Type.INDIVIDUALINCOME, null,0,null),
                    new Transaction(getDate(2019,2,27),20, "Pohod", Transaction.Type.INDIVIDUALINCOME, null,0,null))
    );

    private static Date getDate(int year, int month, int day) {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    public static ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public static void deleteTransaction(Transaction transaction) {
        transactions.remove(transaction);
    }
}
