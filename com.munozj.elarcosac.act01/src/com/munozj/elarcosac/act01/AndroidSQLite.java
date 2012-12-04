package com.munozj.elarcosac.act01;

import java.util.List;

import com.androidhive.androidsqlite.R;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;


public class AndroidSQLite extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        Button editbtn=(Button) findViewById(R.id.edit);
        final DatabaseHandler db = new DatabaseHandler(this);
        ListAdapter lv=new ListAdapter(this, db.getAllContacts()); 
        View header=getLayoutInflater().inflate(R.layout.header, null);
        ListView listview=getListView();
        listview.addHeaderView(header);
       
       setMyAdapter(db.getAllContacts());
        
       final TextView comment=(TextView) findViewById(R.id.comment);
       
      if(db.getAllContacts().size()<=0)
       	comment.setText("no available contacts. try to sync from phone or add phone contacts."); 
      else
    	  comment.setText("");
       
      Button sync=(Button) findViewById(R.id.sync);
      sync.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			db.deleteAllContact();
			 Cursor cursorgetcontact = getCursorContacts();

		        while (cursorgetcontact.moveToNext()) {
		        	String displayName = cursorgetcontact.getString(cursorgetcontact
		              .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
		            String displayNum = cursorgetcontact.getString(cursorgetcontact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		            db.addContact(new Contact(displayName, displayNum));
		        }
		
		     
		        Log.d("Reading: ", "Reading all contacts.."); 
		        List<Contact> contacts = db.getAllContacts();       
		       
		        
		        for (Contact cn : contacts) {
		            String log = "Id: "+cn.getID()+" ,Name: " + cn.getName() + " ,Phone: " + cn.getPhoneNumber();
		              
		        Log.d("Name: ", log);
		        
		        
		        } 
		        if(db.getAllContacts().size()<=0)
		           	comment.setText("no available contacts. try to sync from phone or add phone contacts.");
		          else
		        	  comment.setText("");
		        setMyAdapter(contacts);
		        
		        }
	});
        
      
    }
    
    protected void setMyAdapter(List<Contact> c){
    	ListAdapter lv=new ListAdapter(this, c);
    	setListAdapter(lv);
    }

    private Cursor getCursorContacts() {
        // Run query
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
        		ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '"
            + ("1") + "'";
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
            + " COLLATE LOCALIZED ASC";

        
        return managedQuery(uri, projection, selection, selectionArgs,
            sortOrder);
      }
}