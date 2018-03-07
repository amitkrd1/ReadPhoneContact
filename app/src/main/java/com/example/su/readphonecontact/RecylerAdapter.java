package com.example.su.readphonecontact;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by su on 1/11/17.
 */

public class RecylerAdapter extends RecyclerView.Adapter<RecylerAdapter.MyViewHolder> {

    Context context;
    List<Contact> arrayList;
    public static String json;
    char lastchar;
    String finalString;
    byte[] outimage;
  public static   ArrayList <Contact> selectedContactsList=new ArrayList<Contact>();

    public RecylerAdapter(Context context, List<Contact> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.contact,parent,false);
        MyViewHolder vhold=new MyViewHolder(v);

        return vhold;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        String contactname=arrayList.get(position).get_name();
        String firstchar=arrayList.get(position).get_name().substring(0,1);
        int val=contactname.lastIndexOf(" ");

        if(val==-1){

        }

        else {
            lastchar = contactname.charAt(val + 1);
            finalString = firstchar.concat(String.valueOf(lastchar));
        }

        final Contact setget=arrayList.get(position);
        holder.contactname.setText(arrayList.get(position).get_name());
        if(arrayList.get(position).get_phone_number()==null){
            holder.contactphnum.setText(" ");
        }
        else {
        holder.contactphnum.setText(arrayList.get(position).get_phone_number());
        }

        if(setget.getImageuri()==null){

            holder.tv_name.setVisibility(View.VISIBLE);
            if(val==-1){
                holder.tv_name.setText(firstchar);
            }
            else {
            holder.tv_name.setText(finalString);
            }
            holder.tv_name.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tv_name.setTypeface(Typeface.DEFAULT_BOLD);
            holder.tv_name.setTextSize(20);
            holder.ivContact.setVisibility(View.GONE);

        }
        else{

            holder.ivContact.setVisibility(View.VISIBLE);
            holder.ivContact.setImageURI(Uri.parse(setget.getImageuri()));
            holder.tv_name.setVisibility(View.GONE);
        }
        holder.contactchck.setOnCheckedChangeListener(null);
        holder.contactchck.setChecked(setget.isselected());

        holder.contactchck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {



                if(isChecked==true){
                    setget.setIsselected(true);
                    selectedContactsList.add(arrayList.get(position));
                    System.out.println("con--"+ selectedContactsList.size());
                }
                if(isChecked==false){
                     setget.setIsselected(false);
                    selectedContactsList.remove(arrayList.get(position));
                    System.out.println("con--"+ selectedContactsList.size());
                }
                holder.contactchck.setChecked(setget.isselected());

                for (Contact cn : selectedContactsList) {
                    String log = "Id: " + cn.get_id() + " ,Name: " + cn.get_name() + " ,Phone: " + cn.get_phone_number();
                    // Writing Contacts to log
                    Log.d("Name: ", log);


                }






            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{


        TextView contactname,contactphnum;
        ImageView ivContact;
        TextView tv_name;
         CheckBox contactchck;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name=(TextView)itemView.findViewById(R.id.tv_name);
            contactname=(TextView)itemView.findViewById(R.id.tvname);
            contactphnum=(TextView)itemView.findViewById(R.id.phnnum);
            ivContact=(ImageView)itemView.findViewById(R.id.iv);
            contactchck=(CheckBox)itemView.findViewById(R.id.contactchk);


        }
    }


}
