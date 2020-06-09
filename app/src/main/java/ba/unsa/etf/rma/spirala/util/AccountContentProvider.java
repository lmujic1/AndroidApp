package ba.unsa.etf.rma.spirala.util;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AccountContentProvider extends ContentProvider {
    private static final int ACCOUNTID = 1;
    private static final UriMatcher um;

    static {
        um = new UriMatcher(UriMatcher.NO_MATCH);
        um.addURI("rma.provider.accounts", "elements/#", ACCOUNTID);
    }

    TransactionDBOpenHelper transactionDBOpenHelper;
    @Override
    public boolean onCreate() {
        transactionDBOpenHelper = new TransactionDBOpenHelper(getContext(), TransactionDBOpenHelper.DATABASE_NAME, null, TransactionDBOpenHelper.DATABASE_VERSION);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database;
        try {
            database = transactionDBOpenHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            database = transactionDBOpenHelper.getReadableDatabase();
        }
        String groupBy = null;
        String having = null;
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        switch (um.match(uri)) {
            case ACCOUNTID:
                String idRow = uri.getPathSegments().get(1);
                sqLiteQueryBuilder.appendWhere(TransactionDBOpenHelper.ACCOUNT_INTERNAL_ID + "=" + idRow);
            default:
                break;
        }
        sqLiteQueryBuilder.setTables(TransactionDBOpenHelper.ACCOUNT_TABLE);
        Cursor cursor = sqLiteQueryBuilder.query(database, projection, selection, selectionArgs, groupBy, having, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (um.match(uri)) {
            case ACCOUNTID:
                return "vnd.android.cursor.dir/vnd.rma.elemental";
            default:
                throw new IllegalArgumentException("Unsuported uri: " + uri.toString());
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase database;
        try {
            database = transactionDBOpenHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            database = transactionDBOpenHelper.getReadableDatabase();
        }
        long id = database.insert(TransactionDBOpenHelper.ACCOUNT_TABLE, null, values);

        return uri.buildUpon().appendPath(String.valueOf(id)).build();
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database;
        try {
            database = transactionDBOpenHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            database = transactionDBOpenHelper.getReadableDatabase();
        }
        int ud = database.update(TransactionDBOpenHelper.ACCOUNT_TABLE, values, selection, selectionArgs);
        return ud;
    }
}
