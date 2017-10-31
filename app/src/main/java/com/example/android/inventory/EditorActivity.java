package com.example.android.inventory;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.inventory.data.ProductContract;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    EditText price, quantity, supplier, producTname, mail,Change_In_Quantity;
    ImageView uploadeimage;
    Button button, order,increase,decrease;
    Uri uridata;
    ItemAdapter itemAdapter;
    private boolean itemChanged = false;
    private Uri reciveduri;
    private int loaderid = 1;
    private int imageAdded = 1;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            itemChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        itemAdapter = new ItemAdapter(this, null);
        final Intent itent = getIntent();
        reciveduri = itent.getData();
        if (reciveduri == null) {
            setTitle(getString(R.string.Add_mode));
            invalidateOptionsMenu();

        } else {
            setTitle(getString(R.string.Edit_mode));
            getLoaderManager().initLoader(loaderid, null, this);
        }
        decrease=(Button)findViewById(R.id.Decrease_quantity) ;

        price = (EditText) findViewById(R.id.price);
        quantity = (EditText) findViewById(R.id.quantity);
        supplier = (EditText) findViewById(R.id.supplier);
        producTname = (EditText) findViewById(R.id.nameofitem);
        mail = (EditText) findViewById(R.id.smail);
        Change_In_Quantity=(EditText)findViewById(R.id.editeQuantity);
        price.setOnTouchListener(mTouchListener);
        quantity.setOnTouchListener(mTouchListener);
        supplier.setOnTouchListener(mTouchListener);
        producTname.setOnTouchListener(mTouchListener);
        mail.setOnTouchListener(mTouchListener);

        order = (Button) findViewById(R.id.Order);
        uploadeimage = (ImageView) findViewById(R.id.uploadImage);
        uploadeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadeimage.setVisibility(View.INVISIBLE);
                addimage();


            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String pricee = price.getText().toString().trim();
                String quantityy = quantity.getText().toString().trim();
                String supplierr = supplier.getText().toString().trim();
                String name = producTname.getText().toString().trim();
                String suplierId = mail.getText().toString().trim();
                if (TextUtils.isEmpty(pricee) || TextUtils.isEmpty(quantityy) || TextUtils.isEmpty(supplierr) || TextUtils.isEmpty(name) || TextUtils.isEmpty(suplierId)) {
                    showBeforeOrderWarning();
                } else {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{suplierId});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Required more quantity of " + name);
                    intent.setType("message/rfc822");
                    startActivity(intent.createChooser(intent, "Choose a email app"));
                    startActivity(intent);
                }
            }
        });
        button = (Button) findViewById(R.id.save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert();
            }
        });
        increase=(Button)findViewById(R.id.Increase_quantity);
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentQuantity=quantity.getText().toString();
                String change=Change_In_Quantity.getText().toString();
                if(TextUtils.isEmpty(currentQuantity)||TextUtils.isEmpty(change))
                {
                         IncreaseWaring();
                }
                else {
                    int currentInt=Integer.parseInt(currentQuantity);
                    int changeint=Integer.parseInt(change);
                    currentInt = currentInt + changeint;
                    quantity.setText(String.valueOf(currentInt));
                }
            }
        });
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentQuantity=quantity.getText().toString();
                String change=Change_In_Quantity.getText().toString();

                if(TextUtils.isEmpty(currentQuantity)||TextUtils.isEmpty(change))
                {
                    IncreaseWaring();
                }
                else{
                    int currentInt=Integer.parseInt(currentQuantity);
                    int changeint=Integer.parseInt(change);
                    if(currentInt>0)
                    { currentInt=currentInt-changeint;
                        if(currentInt>0)
                        { quantity.setText(String.valueOf(currentInt));}}}
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.itemsdelet, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (reciveduri == null) {
            MenuItem menuItem = menu.findItem(R.id.delet);
            menuItem.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delet:
                showDeleteConfirmationDialog();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!itemChanged) {
            super.onBackPressed();
            return;
        } else {
            showBackState();
        }


    }
    public void insert() {

        String pricee = price.getText().toString().trim();
        String quantityy = quantity.getText().toString().trim();
        String supplierr = supplier.getText().toString().trim();
        String name = producTname.getText().toString().trim();
        String mailid = mail.getText().toString().trim();

        if (TextUtils.isEmpty(pricee) || TextUtils.isEmpty(quantityy) || TextUtils.isEmpty(supplierr) || TextUtils.isEmpty(name) || TextUtils.isEmpty(mailid)) {

            showEmptyState();

        }
        else if(reciveduri==null&&uridata==null)
        {
            showEmptyState();
        }else
        {
            int qtity = Integer.parseInt(quantityy);
            int price = Integer.parseInt(pricee);

            ContentValues contentValues = new ContentValues();
            contentValues.put(ProductContract.ItemEnery.Quantity_Column, qtity);
            contentValues.put(ProductContract.ItemEnery.Price_Column, price);
            contentValues.put(ProductContract.ItemEnery.Supllier, supplierr);
            contentValues.put(ProductContract.ItemEnery.productColumn, name);
            contentValues.put(ProductContract.ItemEnery.emailColumn, mailid);

            if (uridata != null) {
                String imagestring = uridata.toString();
                contentValues.put(ProductContract.ItemEnery.ImageColumn, imagestring);
                uridata=null;
            }

            if (reciveduri == null) {
                Uri newuri = getContentResolver().insert(ProductContract.ItemEnery.Content, contentValues);
                if (newuri != null) {
                    Toast.makeText(this, R.string.dataadded, Toast.LENGTH_LONG).show();
                    Intent x = new Intent(EditorActivity.this, ItemsActicvity.class);
                    startActivity(x);
                } else {
                    Toast.makeText(this, R.string.notAdded, Toast.LENGTH_LONG);
                }
            } else {

                int reffected = getContentResolver().update(reciveduri, contentValues, null, null);
                if (reffected == 0) {
                    Toast.makeText(this, R.string.FailUpdate, Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, R.string.true_update, Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }

    private void addimage() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");

        startActivityForResult(intent, imageAdded);
        itemChanged = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == imageAdded && resultCode == RESULT_OK && data != null) {
            uridata = data.getData();
            uploadeimage.setImageURI(uridata);
            uploadeimage.setVisibility(View.VISIBLE);

        } else if (resultCode != RESULT_OK) {
            uploadeimage.setVisibility(View.VISIBLE);
        }}

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ProductContract.ItemEnery._ID,
                ProductContract.ItemEnery.ImageColumn,
                ProductContract.ItemEnery.Price_Column,
                ProductContract.ItemEnery.Quantity_Column,
                ProductContract.ItemEnery.Supllier,
                ProductContract.ItemEnery.productColumn,
                ProductContract.ItemEnery.emailColumn
        };
        return new CursorLoader(this, reciveduri, projection, null, null, null);
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            int quanid = cursor.getColumnIndex(ProductContract.ItemEnery.Quantity_Column);
            int priceid = cursor.getColumnIndex(ProductContract.ItemEnery.Price_Column);
            int suplid = cursor.getColumnIndex(ProductContract.ItemEnery.Supllier);
            int imageid = cursor.getColumnIndex(ProductContract.ItemEnery.ImageColumn);
            int nameid = cursor.getColumnIndex(ProductContract.ItemEnery.productColumn);
            int mailid = cursor.getColumnIndex(ProductContract.ItemEnery.emailColumn);
            int quan = cursor.getInt(quanid);
            int pri = cursor.getInt(priceid);
            String sup = cursor.getString(suplid);
            String pname = cursor.getString(nameid);
            String emailll = cursor.getString(mailid);
            String imageString = cursor.getString(imageid);
            price.setText(String.valueOf(pri));
            quantity.setText(String.valueOf(quan));
            supplier.setText(sup);
            producTname.setText(pname);
            mail.setText(emailll);
            uploadeimage.setImageURI(Uri.parse(imageString));
            cursor.close();
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        price.setText("");
        quantity.setText("");
        supplier.setText("");
        producTname.setText("");
        mail.setText("");
        uploadeimage.setImageResource(R.drawable.product);

    }

    private void showEmptyState() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.Confirmation_asked);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showBeforeOrderWarning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.orderWarning);
        builder.setPositiveButton(R.string.Eitediting, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                finish();
            }
        });
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void IncreaseWaring() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Can,t change the quantity if quantity or amount of change fields are empty");
        builder.setPositiveButton("quit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                finish();
            }
        });
        builder.setNegativeButton("Edit fields", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showBackState() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.quiting_confirmation_asked);
        builder.setPositiveButton(R.string.discard_changes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                finish();
            }
        });
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.Confirm_Delet);
        builder.setPositiveButton(R.string.delet, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                deletePet();
            }
        });
        builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deletePet() {
        if (reciveduri != null) {
            int rowsdeleted = getContentResolver().delete(reciveduri, null, null);
            if (rowsdeleted == 0) {
                Toast.makeText(this, R.string.failed_Delet, Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, R.string.success_delet, Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }
}
