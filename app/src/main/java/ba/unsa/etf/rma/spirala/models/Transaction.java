package ba.unsa.etf.rma.spirala.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;

public class Transaction implements Serializable, Parcelable {


    protected Transaction(Parcel in) {
        amount = in.readDouble();
        title = in.readString();
        itemDescription = in.readString();
        transactionInterval = in.readInt();
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(amount);
        dest.writeString(title);
        dest.writeString(itemDescription);
        dest.writeInt(transactionInterval);
    }

    public enum Type {
        INDIVIDUALPAYMENT("Individual payment"),
        REGULARPAYMENT("Regular payment"),
        PURCHASE("Purchase"),
        INDIVIDUALINCOME("Individual income"),
        REGULARINCOME("Regular income");

        private final String opis;

        private Type(String opis) {
            this.opis = opis;
        }

        @NonNull
        @Override
        public String toString() {
            return opis;
        }
    }

    private Date date;
    private double amount;
    private String title;
    private Type type;
    private String itemDescription;
    private int transactionInterval;
    private Date endDate = null;

    public Transaction() {
    }

    public Transaction(Date date, double amount, String title, Type type, String itemDescription, int transactionInterval, Date endDate) {
        this.date = date;
        this.amount = amount;
        setTitle(title);
        this.type = type;
        setItemDescription(itemDescription);
        setTransactionInterval(transactionInterval);
        setEndDate(endDate);
    }

    public Date getDate() {
        return date;
    }

    public String getDate1(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd. MMMM, yyyy");
        String d = dateFormat.format(date);
        return d;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title.length() <= 3 || title.length() >= 15)
            System.out.println("G R E S K A");//throw new IllegalArgumentException();
        this.title = title;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        if (type == Type.INDIVIDUALINCOME || type == Type.REGULARINCOME)
            this.itemDescription = null;
        else
            this.itemDescription = itemDescription;
    }

    public int getTransactionInterval() {
        return transactionInterval;
    }

    public void setTransactionInterval(int transactionInterval) {
        this.transactionInterval = 0;
        if (type == Type.REGULARINCOME || type == Type.REGULARPAYMENT)
            this.transactionInterval = transactionInterval;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        if (type == Type.REGULARPAYMENT || type == Type.REGULARINCOME)
            this.endDate = endDate;
        else this.endDate = null;
    }
}
