package com.example.su.readphonecontact;

import android.Manifest;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS=100;
    ArrayList<Contact> contactList=new ArrayList<Contact>();
    RecyclerView rv;
    List<Contact> contacts=new ArrayList<Contact>();
    String contact_id;
    String name;
    String phoneNumber = null;
    String photouri= ContactsContract.CommonDataKinds.Phone.PHOTO_URI;
    String _ID = ContactsContract.Contacts._ID;
    String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
    String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
    String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
    String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
    Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    Bitmap  contactimage;
    String image_uri;

    ArrayList<String> updatedID=new ArrayList<String>();
    ArrayList<String> databaseID =new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       rv=(RecyclerView)findViewById(R.id.rview);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(itemDecoration);
        getLoaderManager().initLoader(0,null,this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        //contactList.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_contact,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_save){

         RecylerAdapter adapter=new RecylerAdapter(this,contactList);

            ArrayList<Contact> selcontact=adapter.selectedContactsList;
            Log.v("size of list",String.valueOf(selcontact.size()));

            Gson gson=new Gson();
            String json=gson.toJson(selcontact);
            Log.v("final contact=---",json);

            Intent i=new Intent(MainActivity.this,SelectedContacts.class);
            i.putExtra("contacts",json);
            startActivity(i);

        }
        return true;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.READ_CONTACTS },
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }

            return new CursorLoader(this, CONTENT_URI, null,null, null, null);


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            DBHelper myhelper=new DBHelper(this);
            data.moveToFirst();
            while (!data.isAfterLast()) {
                contact_id = data.getString(data.getColumnIndex( _ID ));
            updatedID.add(contact_id);
            name=data.getString(data.getColumnIndex(DISPLAY_NAME));
                Log.v("name",name);

            int hasPhoneNumber = Integer.parseInt(data.getString(data.getColumnIndex( HAS_PHONE_NUMBER )));
            System.out.println(""+hasPhoneNumber);

            if(hasPhoneNumber>0) {
                Log.v("id", contact_id);
                Cursor PhoneCursor = getContentResolver().query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);
                while (PhoneCursor.moveToNext()){
                    phoneNumber = PhoneCursor.getString(PhoneCursor.getColumnIndex(NUMBER));}
            }
            else{
                phoneNumber=null;
            }


            Cursor imageCursor=getContentResolver().query(PhoneCONTENT_URI,null,Phone_CONTACT_ID + " = ?",new String[]{contact_id},null);
                while (imageCursor.moveToNext()){
                    image_uri=imageCursor.getString(imageCursor.getColumnIndex(photouri));
                    System.out.println("imageuri"+image_uri);

                }



/*
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contact_id)));
             contactimage = BitmapFactory.decodeStream(inputStream);*/



            myhelper.checkRow(contact_id,name,phoneNumber,image_uri);
            myhelper.updateRow(contact_id,name,phoneNumber,image_uri);
            data.moveToNext();

            Contact mycontact=new Contact();
            mycontact.set_id(contact_id);
            if(name==null){
                mycontact.set_name("");
            }
            else {
            mycontact.set_name(name);}
            if(hasPhoneNumber>0){
            mycontact.set_phone_number(phoneNumber);
            }
            else{
                mycontact.set_phone_number(" ");
            }
            mycontact.setIsselected(false);

                mycontact.setImageuri(image_uri);
           // mycontact.setImage(contactimage);
              //  mycontact.setImg(image);

            contactList.add(mycontact);

        }
        for(int i=0;i<updatedID.size();i++){
            Log.v("updated id ", String.valueOf(updatedID.size()));}
           contacts=myhelper.getAllContacts();
         Log.v("test",String.valueOf(contacts.size()));
         for(int i=0;i<contacts.size();i++){
             databaseID.add(contacts.get(i).get_id());
          Log.v("val----", String.valueOf(databaseID.size()));
        }
        databaseID.removeAll(updatedID);
        System.out.println(databaseID);

        for(int i = 0; i< databaseID.size(); i++){
            myhelper.deleteRow(databaseID.get(i));
            Log.v("deleted","deleted");
        }


        for (Contact cn : contacts) {
            String log = "Id: " + cn.get_id() + " ,Name: " + cn.get_name() + " ,Phone: " + cn.get_phone_number()+" image- "+cn.getImageuri()+" check "+cn.isselected() ;
            // Writing Contacts to log
            Log.d("Name: ", log);

        }
        RecylerAdapter adapter=new RecylerAdapter(this,contactList);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    @Override
    public void onLoaderReset(Loader loader) {
       // mAdapter.swapCursor(null);
    }





/*
    public void openPhoto(long contactId) {

        InputStream is;
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = getContentResolver().query(photoUri,
                new String[] {ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
        if (cursor == null) {

        }
        try {
            if (cursor.moveToFirst()) {
                 image = cursor.getBlob(0);
                if (image != null) {
                    is= new ByteArrayInputStream(image);
                }
            }
        } finally {
            cursor.close();
        }
    }*/


   /* private void getContactsDetails() {

        Cursor phones = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
                null, null);
        while (phones.moveToNext()) {
            String Name = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String Number = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            String image_uri = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            System.out.println("Contact1 : " + Name + ", Number " + Number
                    + ", image_uri " + image_uri);


            if (image_uri != null) {
                image.setImageURI(Uri.parse(image_uri));
            }


        }
    }
*/








}
