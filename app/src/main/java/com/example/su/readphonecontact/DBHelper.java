package com.example.su.readphonecontact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by su on 30/10/17.
 */

public class DBHelper extends SQLiteOpenHelper {




    private static final int DATABASE_VERSION=3;
    private static final String DATABASE_NAME="contactsDetail";
    private static final String TABLE_CONTACTS = "contacts";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";
    private static final String KEY_IMAGES="image";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " TEXT ," + KEY_NAME + " TEXT," + KEY_IMAGES + " TEXT,"
                + KEY_PH_NO + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        Log.d("onupgrade","onupgrade");
        onCreate(db);
        /*if(newVersion>oldVersion){

            String ALTER_TABLE="ALTER TABLE " + TABLE_CONTACTS + " ADD COLUMN KEY_IMAGES TEXT";

            db.execSQL(ALTER_TABLE );
        }*/

    }
    // Adding new contact
    void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID,contact.get_id());
        values.put(KEY_NAME, contact.get_name()); // Contact Name
        values.put(KEY_PH_NO, contact.get_phone_number()); // Contact Phone
        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }


    public Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                        KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(cursor.getString(0),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return contact;
    }





    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.set_id(cursor.getString(0));
                contact.set_name(cursor.getString(1));
                contact.set_phone_number(cursor.getString(3));
                contact.setImageuri(cursor.getString(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }




    // Updating single contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.get_name());
        values.put(KEY_PH_NO, contact.get_phone_number());

        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.get_id()) });
    }


    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.get_id()) });
        db.close();
    }



    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public void delete(){

        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("delete from "+TABLE_CONTACTS);

    }


    public void InsertData(String id,String Name,String phonenum){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put(KEY_ID,id);
        cv.put(KEY_NAME,Name);
        cv.put(KEY_PH_NO,phonenum);
        db.insert(TABLE_CONTACTS,null,cv);

    }

    public void checkRow(String id,String Name,String phonenum,String image){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
       // String sql ="SELECT KEY_ID FROM "+TABLE_CONTACTS+" WHERE KEY_ID="+id;
        String checkQuery = "SELECT " + KEY_ID + " FROM " + TABLE_CONTACTS + " WHERE " + KEY_ID + "= '"+id + "'";
        cursor= db.rawQuery(checkQuery,null);
        Log.v("Cursor Count : ", String.valueOf(cursor.getCount()));
        ContentValues cv=new ContentValues();

        if(cursor.getCount()>0){

        }else{

            cv.put(KEY_ID,id);
            cv.put(KEY_NAME,Name);
            cv.put(KEY_IMAGES,image);

            cv.put(KEY_PH_NO,phonenum);
            db.insert(TABLE_CONTACTS,null,cv);

        }
        cursor.close();

    }

    public void deleteRow(String id){
      SQLiteDatabase db=this.getWritableDatabase();

          db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { id });
    }

    public void updateRow(String id,String name,String phone,String image){


        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(KEY_ID,id);
        cv.put(KEY_NAME,name);
        cv.put(KEY_PH_NO,phone);
        cv.put(KEY_IMAGES,image);


        db.update(TABLE_CONTACTS, cv, "id = ? ", new String[] { id } );

    }


}
