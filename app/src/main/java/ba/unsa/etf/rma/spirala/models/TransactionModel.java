package ba.unsa.etf.rma.spirala.models;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;



public class TransactionModel {
    private static ArrayList<Transaction> transactions = new ArrayList<>(
            Arrays.asList(new Transaction(new Date(2019,1,26),100, "Stipendija", Transaction.Type.REGULARINCOME, "Potrebno",30, new Date(2020,6,30)), new Transaction(new Date(2018,1,27),-500, "Uplata za stanarinu", Transaction.Type.REGULARPAYMENT, "Prvo tromjesecje",90, new Date(2019,1,26)),
                    new Transaction(new Date(2017,3,17),200, "Takmicenje", Transaction.Type.INDIVIDUALINCOME, null,0, null),
                    new Transaction(new Date(2018,3,29),300, "Poklon", Transaction.Type.INDIVIDUALINCOME, null,0, null),
                    new Transaction(new Date(2019,1,29),-100, "Nabavka", Transaction.Type.INDIVIDUALPAYMENT, "Mjesecna",0, null),
                    new Transaction(new Date(2017,4,21),-100, "Poklon", Transaction.Type.REGULARPAYMENT, "Opis proizvoda",366, new Date(2020,4,21)),
                    new Transaction(new Date(2018,11,20),50, "Džeparac", Transaction.Type.REGULARINCOME, "Sweets",90, new Date(2015,8,4)),
                    new Transaction(new Date(2015,1,29),-80, "Kozmetika", Transaction.Type.PURCHASE, "-",0,null),
                    new Transaction(new Date(2016,1,17),-35, "Preparati", Transaction.Type.PURCHASE,"-",0,null),
                    new Transaction(new Date(2018,5,30),350, "Praksa", Transaction.Type.INDIVIDUALINCOME, null, 0, null),
                    new Transaction(new Date(2017,12,20),-120, "Pohod", Transaction.Type.INDIVIDUALPAYMENT, null,0, null),
                    new Transaction(new Date(2019,1,29),60, "Farmasi", Transaction.Type.REGULARINCOME, "Do isteka ugovora",20, new Date(2020,5,3)),
                    new Transaction(new Date(2019,5,29),-240, "Biciklo", Transaction.Type.INDIVIDUALPAYMENT, "-",0,null ),
                    new Transaction(new Date(2015,1,29),-70, "Režije", Transaction.Type.REGULARPAYMENT, "Struja, grijanje",30, new Date(2021,1,26)),
                    new Transaction(new Date(2018,1,29),30, "Ušteđevina", Transaction.Type.REGULARINCOME, "Za odmor",20, new Date(2020,7,30)),
                    new Transaction(new Date(2019,8,24),150, "Bajram", Transaction.Type.INDIVIDUALINCOME, null,0,null),
                    new Transaction(new Date(2020,1,13),300, "pohod", Transaction.Type.INDIVIDUALINCOME, null,0,null),
                    new Transaction(new Date(2019,12,13),-70, "Odjeca", Transaction.Type.PURCHASE, "-",0,null),
                    new Transaction(new Date(2020,1,17),20, "pohod", Transaction.Type.INDIVIDUALINCOME, null,0,null),
                    new Transaction(new Date(2020,1,27),20, "pohod", Transaction.Type.INDIVIDUALINCOME, null,0,null))
    );


    public static ArrayList<Transaction> getTransactions() {
        return transactions;
    }
}
