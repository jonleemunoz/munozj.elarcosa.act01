package com.munozj.elarcosac.act01;

import java.util.ArrayList;
import java.util.List;

import com.androidhive.androidsqlite.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class ListAdapter extends ArrayAdapter{
	List<Contact> con = null;
	Context cont=null;
	DatabaseHandler dbhelp;
   
	private ArrayList<DataSetObserver> observers = new ArrayList<DataSetObserver>();

	public ListAdapter(Context context, List<Contact> contact) {
		super(context, R.layout.rowlayout, contact);
		con=contact;
		cont=context;
	}
	public View getView(final int position, View convertView, ViewGroup parent) {
		dbhelp=new DatabaseHandler(cont);
		LayoutInflater inflater = (LayoutInflater) cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
	
		TextView name=(TextView) rowView.findViewById(R.id.name);
		TextView num=(TextView) rowView.findViewById(R.id.number);
		Button edit=(Button) rowView.findViewById(R.id.edit);
		Button del=(Button) rowView.findViewById(R.id.delete);
		name.setText(con.get(position).getName());
		System.out.println(con.get(position).getName());
		num.setText(con.get(position).getPhoneNumber());
		
		del.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int res=dbhelp.deleteContact(con.get(position));
			if(res==1){
				Toast.makeText(cont, "Successfully deleted "+con.get(position).getName(), Toast.LENGTH_SHORT).show();
				remove(con.get(position));
				dbhelp.close();
				notifyDataSetChanged();
				
				
			}
			}
		});
		
		edit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				final Dialog dialog = new Dialog(cont);
				dialog.setContentView(R.layout.edit_contact);
				dialog.setTitle("Update Record");

				Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
				Button dialogCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);
				final EditText name=(EditText) dialog.findViewById(R.id.editname);
				final EditText num=(EditText) dialog.findViewById(R.id.editnum);
				
				name.setHint(con.get(position).getName());
				num.setHint(con.get(position).getPhoneNumber());
				

				dialogButton.setOnClickListener(new View.OnClickListener() {
					String newname=name.getHint().toString();
					String newnum=num.getHint().toString();
					@Override
					public void onClick(View v) {
						 newname=name.getText().toString();
					     newnum=num.getText().toString();
					
						
					   int res= dbhelp.updateContact(con.get(position), new Contact(newname, newnum));
					    
						if(res==1){
							dbhelp.close();
							Contact newcon=dbhelp.getContact(con.get(position).getID());
							con.get(position).setName(newcon.getName());
							con.get(position).setPhoneNumber(newcon.getPhoneNumber());
							notifyDataSetChanged();
						
						}
							
						System.out.println("new name:" +con.get(position).getName());
						dialog.dismiss();
						Toast.makeText(cont, "Successfully updated  contact.", Toast.LENGTH_SHORT).show();
					}
				});
				
				dialogCancel.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
	 
				dialog.show();
			}
		});
		
	
		return rowView;
	}
	
	

}
