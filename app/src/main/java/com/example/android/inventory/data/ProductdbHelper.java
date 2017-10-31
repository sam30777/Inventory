package com.example.android.inventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.inventory.data.ProductContract.ItemEnery;

/**
 * Created by Santosh on 16-04-2017.
 */

public class ProductdbHelper extends SQLiteOpenHelper {
    public static final String databseName = "item.db";
    public static final int databaseId = 1;
    public static final String sqliteTable = "CREATE TABLE " + ItemEnery.TableName + "("
            + ItemEnery._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ItemEnery.Price_Column + " INTEGER NOT NULL, "
            + ItemEnery.Quantity_Column + " INTEGER NOT NULL, "
            + ItemEnery.Supllier + " TEXT, "
            + ItemEnery.emailColumn + " Text,"
            + ItemEnery.productColumn + " TEXT,"
            + ItemEnery.ImageColumn + " TEXT);";

    ProductdbHelper(Context context) {
        super(context, databseName, null, databaseId);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ItemEnery.TableName);
        onCreate(sqLiteDatabase);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sqliteTable);

    }
}
