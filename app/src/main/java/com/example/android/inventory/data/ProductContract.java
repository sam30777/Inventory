package com.example.android.inventory.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Santosh on 16-04-2017.
 */

public class ProductContract {
    public static final String Authority = "com.example.android.inventory";
    public static final String path = "inventory";
    public static Uri baseUri = Uri.parse("content://" + Authority);

    ProductContract() {
    }
    public static final class ItemEnery implements BaseColumns {
        public static final Uri Content = Uri.withAppendedPath(baseUri, path);
        public static final String contenttype1 =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Authority + "/" + path;
        public static final String contenttype2 =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Authority + "/" + path + "/#";
        public static final String TableName = "inventory";
        public static final String Id = BaseColumns._ID;
        public static final String Quantity_Column = "Quantity";
        public static final String Price_Column = "Price";
        public static final String Supllier = "sName";
        public static final String ImageColumn = "image";
        public static final String productColumn = "pName";
        public static final String emailColumn = "email";


    }


}
