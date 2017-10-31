package com.example.android.inventory.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.inventory.data.ProductContract.ItemEnery;

public class Itemprovider extends ContentProvider {
    public static final int alldata = 1;
    public static final int single = 2;
    public static final UriMatcher smatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final String LOG_TAG = Itemprovider.class.getSimpleName();

    static {
        smatcher.addURI(ProductContract.Authority, ProductContract.path, alldata);
        smatcher.addURI(ProductContract.Authority, ProductContract.path + "/#", single);

    }

    public SQLiteOpenHelper mhelper;

    @Override
    public boolean onCreate() {
        mhelper = new ProductdbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArg, String sortorder) {
        Cursor cursor;
        SQLiteDatabase database = mhelper.getReadableDatabase();
        int match = smatcher.match(uri);
        switch (match) {
            case alldata:
                cursor = database.query(ProductContract.ItemEnery.TableName, projection, selection, selectionArg, null, null, sortorder);
                break;
            case single:
                selection = ProductContract.ItemEnery._ID + "=?";
                selectionArg = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(ProductContract.ItemEnery.TableName, projection, selection, selectionArg, null, null, sortorder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    @Nullable
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionargs) {
        SQLiteDatabase database = mhelper.getWritableDatabase();
        int match = smatcher.match(uri);
        int sid;
        switch (match) {
            case alldata:
                sid = database.update(ItemEnery.TableName, contentValues, null, null);
                if (sid != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;
            case single:
                selection = ItemEnery._ID + "=?";
                selectionargs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                sid = database.update(ItemEnery.TableName, contentValues, selection, selectionargs);
                if (sid != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;
            default:

                throw new IllegalArgumentException("Cannot query unknown URI " + uri);

        }
        return sid;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = smatcher.match(uri);
        switch (match) {
            case alldata:
                return ItemEnery.contenttype1;
            case single:
                return ItemEnery.contenttype2;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase database = mhelper.getWritableDatabase();
        final int match = smatcher.match(uri);
        long ids;
        switch (match) {
            case alldata:
                ids = database.insert(ItemEnery.TableName, null, contentValues);
                if (ids == -1) {
                    Log.e(LOG_TAG, "error inseritng uri " + uri);

                    return null;
                }


                getContext().getContentResolver().notifyChange(uri, null);

                return ContentUris.withAppendedId(uri, ids);

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);


        }
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase database = mhelper.getWritableDatabase();
        int match = smatcher.match(uri);
        int did;
        switch (match) {
            case alldata:
                did = database.delete(ItemEnery.TableName, s, strings);
                if (did != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;

            case single:
                s = ItemEnery._ID + "=?";
                strings = new String[]{String.valueOf(ContentUris.parseId(uri))};
                did = database.delete(ItemEnery.TableName, s, strings);
                if (did != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;
            default:


                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return did;
    }
}

