package ba.unsa.etf.rma.spirala.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ba.unsa.etf.rma.spirala.models.Transaction;

public class TransactionDBOpenHelper extends SQLiteOpenHelper {

    public static int idTransaction = 0;
    public static final String DATABASE_NAME = "MyTransactionsDataBase.db";
    public static final int DATABASE_VERSION = 2;

    public TransactionDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public TransactionDBOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static final String TRANSACTION_TABLE = "transactions";
    public static final String TRANSACTION_INTERNAL_ID = "_id";
    public static final String TRANSACTION_ID = "idTransakcije";
    public static final String TRANSACTION_TITLE = "title";
    public static final String TRANSACTION_DATE = "date";
    public static final String TRANSACTION_AMOUNT = "amount";
    public static final String TRANSACTION_TYPE = "type";
    public static final String TRANSACTION_DESCRIPTION = "description";
    public static final String TRANSACTION_INTERVAL = "transactionInterval";
    public static final String TRANSACTION_ENDDATE = "endDate";
    public static final String TRANSACTION_OFFMODE = "offMode";

    public static final String TRANSACTION_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TRANSACTION_TABLE
                    + " ("
                    + TRANSACTION_INTERNAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + TRANSACTION_ID + " INTEGER UNIQUE, "
                    + TRANSACTION_TITLE + " TEXT NOT NULL, "
                    + TRANSACTION_DATE + " TEXT NOT NULL, "
                    + TRANSACTION_AMOUNT + " REAL NOT NULL, "
                    + TRANSACTION_TYPE + " INTEGER NOT NULL, "
                    + TRANSACTION_DESCRIPTION + " TEXT, "
                    + TRANSACTION_INTERVAL + " TEXT, "
                    + TRANSACTION_ENDDATE + " TEXT, "
                    + TRANSACTION_OFFMODE + " TEXT);";

    private static final String TRANSACTION_DROP = "DROP TABLE IF EXISTS " + TRANSACTION_TABLE;


    public static int accountID = 0;
    public static final String ACCOUNT_TABLE = "accounts";
    public static final String ACCOUNT_INTERNAL_ID = "_id";
    public static final String ACCOUNT_ID = "id";
    public static final String ACCOUNT_BUDGET = "budget";
    public static final String ACCOUNT_MONTH_LIMIT = "monthLimit";
    public static final String ACCOUNT_TOTAL_LIMIT = "totalLimit";

    public static final String ACCOUNT_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + ACCOUNT_TABLE
                    + " ("
                    + ACCOUNT_INTERNAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ACCOUNT_ID + " INTEGER UNIQUE,"
                    + ACCOUNT_BUDGET + " REAL,"
                    + ACCOUNT_MONTH_LIMIT + " REAL,"
                    + ACCOUNT_TOTAL_LIMIT + " REAL);";

    public static final String ACCOUNT_DROP = "DROP TABLE IF EXISTS " + ACCOUNT_TABLE;


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TRANSACTION_TABLE_CREATE);
        db.execSQL(ACCOUNT_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TRANSACTION_DROP);
        db.execSQL(ACCOUNT_DROP);
        onCreate(db);
    }

}
