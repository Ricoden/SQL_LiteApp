package com.example.roman.sql_liteapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAdd, btnRead, btnClear, btnUpd, btnDel;
    EditText etName, etEmail, etId;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        btnAdd = (Button) findViewById(R.id.buttonAdd);
        btnAdd.setOnClickListener(this);

        btnRead = (Button) findViewById(R.id.buttonRead);
        btnRead.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.buttonClear);
        btnClear.setOnClickListener(this);

        btnUpd = (Button) findViewById(R.id.buttonUpd);
        btnUpd.setOnClickListener(this);

        btnDel = (Button) findViewById(R.id.buttonDel);
        btnDel.setOnClickListener(this);

        etId =  (EditText) findViewById(R.id.editTextId);
        etName = (EditText) findViewById(R.id.editTextName);
        etEmail = (EditText) findViewById(R.id.editTextEmail);

        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View view) {
        String id = etId.getText().toString();
        String name = etName.getText().toString();
        String mail = etEmail.getText().toString();

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        switch (view.getId()){
            case R.id.buttonAdd:
                contentValues.put(DBHelper.KEY_NAME, name);
                contentValues.put(DBHelper.KEY_MAIL, mail);
                database.insert(DBHelper.TABLE_CONTACTS,null,contentValues);
                Log.d("mLog", name+" is added!");
                break;
            case R.id.buttonRead:
                Cursor cursor = database.query(DBHelper.TABLE_CONTACTS,null,null,null,null,null,null);
               if (cursor.moveToFirst())
               {
                   int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                   int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                   int mailIndex = cursor.getColumnIndex(DBHelper.KEY_MAIL);
                        do {
                            Log.d("mLog", "ID = "+cursor.getInt(idIndex) +
                                    " NAME = "+cursor.getString(nameIndex) +
                                    " EMAIL = "+cursor.getString(mailIndex));
                        } while (cursor.moveToNext());
               } else
                   Log.d("mLog", "Table is empty!");

               cursor.close();
                break;
            case R.id.buttonClear:
                database.delete(DBHelper.TABLE_CONTACTS,null,null);
                Log.d("mLog", "database is empty now");
                break;
            case R.id.buttonUpd:
                if (id.equalsIgnoreCase("")){
                    break;
                }

                contentValues.put(DBHelper.KEY_NAME, name);
                contentValues.put(DBHelper.KEY_MAIL, mail);

                int countUpd = database.update(DBHelper.TABLE_CONTACTS,contentValues, DBHelper.KEY_ID+" = ?", new String[]{id});
                Log.d("mLog", "udpate: "+countUpd+" rows");
                break;
            case R.id.buttonDel:
                if (id.equalsIgnoreCase("")){
                    break;
                }

                int countDel = database.delete(DBHelper.TABLE_CONTACTS, DBHelper.KEY_NAME+" = ?", new String[]{name});
                Log.d("mLog", "deleted: "+countDel+" rows");
                break;
        }
        dbHelper.close();
    }
}
