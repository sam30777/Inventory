package com.example.android.inventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventory.data.ProductContract;

public class ItemAdapter extends CursorAdapter {
    ItemAdapter(Context context, Cursor cursor) {

        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.listitem, viewGroup, false);

    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        final TextView textView = (TextView) view.findViewById(R.id.qvalue);
        TextView textView1 = (TextView) view.findViewById(R.id.pValue);
        TextView textView2 = (TextView) view.findViewById(R.id.productname);
        Button button = (Button) view.findViewById(R.id.Buy);
        ImageView imageView = (ImageView) view.findViewById(R.id.itemimage);
        final int columnid = cursor.getColumnIndex(ProductContract.ItemEnery._ID);
        final int quanid = cursor.getColumnIndex(ProductContract.ItemEnery.Quantity_Column);
        final int priceid = cursor.getColumnIndex(ProductContract.ItemEnery.Price_Column);
        int pname = cursor.getColumnIndex(ProductContract.ItemEnery.productColumn);
        int imageid = cursor.getColumnIndex(ProductContract.ItemEnery.ImageColumn);
        final int id = cursor.getInt(columnid);
        int quantity = cursor.getInt(quanid);
        int price = cursor.getInt(priceid);
        String pnam = cursor.getString(pname);
        String image = cursor.getString(imageid);
        textView.setText(String.valueOf(quantity));
        textView1.setText(String.valueOf(price));
        textView2.setText(pnam);
        imageView.setImageURI(Uri.parse(image));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(textView.getText().toString()) > 0) {
                    Toast.makeText(context, R.string.Item_sold, Toast.LENGTH_SHORT).show();
                    int quantity = Integer.parseInt(textView.getText().toString());
                    quantity = quantity - 1;
                    textView.setText(String.valueOf(quantity));
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(ProductContract.ItemEnery.Quantity_Column, quantity);
                    Uri uri = ContentUris.withAppendedId(ProductContract.ItemEnery.Content, id);
                    context.getContentResolver().update(uri, contentValues, null, null);
                } else {
                    Toast.makeText(context, R.string.no_item_left, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

