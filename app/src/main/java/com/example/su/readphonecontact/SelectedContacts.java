package com.example.su.readphonecontact;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SelectedContacts extends AppCompatActivity {


    ArrayList<Contact> finalContacts=new ArrayList<Contact>();
    RecyclerView recyclerView;
    SelectedContactAdapter adapter;
    TextView nodata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_contacts);
        recyclerView=(RecyclerView)findViewById(R.id.rvcontacts);
        nodata=(TextView)findViewById(R.id.tv_nodata);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        Type type=new TypeToken<ArrayList<Contact>>(){}.getType();

        String val=getIntent().getExtras().getString("contacts");
        Gson gson=new Gson();
        finalContacts= gson.fromJson(val,type);
        Log.v("vv", String.valueOf(finalContacts.size()));

        for(int i=0;i<finalContacts.size();i++){
            Log.d("finalList", "--Name--" + finalContacts.get(i).get_name()+" price  "+finalContacts.get(i).get_phone_number());
        }

        if(finalContacts.size()==0){

            nodata.setText("nocontact selected");
            nodata.setVisibility(View.VISIBLE);


        }
else {
        adapter =new SelectedContactAdapter(SelectedContacts.this,finalContacts);
        recyclerView.setAdapter(adapter);

       adapter.notifyDataSetChanged();}

    }

    @Override
    protected void onResume() {

        Log.v("onresume","onresume");
        super.onResume();
        //finalContacts.clear();
        adapter =new SelectedContactAdapter(SelectedContacts.this,finalContacts);

        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onPause() {
        Log.v("onpause","onpause");
        super.onPause();
        //finalContacts.clear();
    }

    @Override
    protected void onStart() {
        Log.v("onstart","onstart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.v("onStop","onStop");
        super.onStop();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
    }
}
