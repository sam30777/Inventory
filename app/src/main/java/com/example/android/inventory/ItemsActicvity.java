package com.example.android.inventory;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.android.inventory.data.ProductContract.ItemEnery;

public class ItemsActicvity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int loaderid = 1;
    FloatingActionButton fb;
    ItemAdapter itemAdapter;
    GridView gridView;
    Uri uri;
   TextView defaultTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_acticvity);
        defaultTextView=(TextView)findViewById(R.id.DefaultTextview);
        defaultTextView.setVisibility(View.VISIBLE);
        itemAdapter = new ItemAdapter(this, null);
        gridView = (GridView) findViewById(R.id.inventorydata);
        gridView.setEmptyView(defaultTextView);
        gridView.setAdapter(itemAdapter);


        fb = (FloatingActionButton) findViewById(R.id.fbutton);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemsActicvity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(ItemsActicvity.this, EditorActivity.class);
                uri = ContentUris.withAppendedId(ItemEnery.Content, l);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(loaderid, null,this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.itemmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_all_entries:
                showDeleteConfirmationDialog();
            default:
        }
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection =
                {
                        ItemEnery._ID,
                        ItemEnery.Quantity_Column,
                        ItemEnery.Price_Column,
                        ItemEnery.productColumn,
                        ItemEnery.ImageColumn,


                };


        return new CursorLoader(this, ItemEnery.Content, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {


        itemAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        itemAdapter.swapCursor(null);


    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.all_item_wil_be_added);
        builder.setPositiveButton(R.string.deletdata, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                getContentResolver().delete(ItemEnery.Content, null, null);
            }
        });
        builder.setNegativeButton(R.string.canceldelet, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
