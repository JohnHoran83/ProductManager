package com.productmanager;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;
import android.os.Build;

public class AddToDBActivity extends ActionBarActivity implements OnClickListener { 

	String barcode;
    String description;
    String size;
    
    Button addBtn;
    
    DB_Helper db;
	private int quantity;
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_to_db);		
		db = new DB_Helper(this);
		
		getValuesFromIntent();
	    createTextView();	
	    initialiseButton();
	}

	public void createTextView() {
		// Create the text view
	    TextView textViewEan = (TextView)findViewById(R.id.product_details_ean_2);
	    textViewEan.setText(barcode);
	    // Create the text view
	    TextView textViewDesc = (TextView)findViewById(R.id.product_details_desc_2);
	    textViewDesc.setText(description);
	    // Create the text view
	    TextView textViewSize = (TextView)findViewById(R.id.product_details_size_2);
	    textViewSize.setText(size);
	}

	public void getValuesFromIntent() {
		Intent intent = getIntent();
	    barcode = intent.getStringExtra(ProductActivity.UPCDatabaseQuery.BARCODE);
	    description = intent.getStringExtra(ProductActivity.UPCDatabaseQuery.RESULT_DESC);
	    size = intent.getStringExtra(ProductActivity.UPCDatabaseQuery.RESULT_SIZE);
	}

	private void initialiseButton() {
		addBtn = (Button)findViewById(R.id.add_to_database);
		addBtn.setOnClickListener(this);
	}
	
	public void onClick(View v){
		if(v.getId()==R.id.add_to_database){
			Test test = new Test(this);
		    EditText editText = (EditText) findViewById(R.id.quantity);
		    quantity = Integer.parseInt(editText.getText().toString());
		    Toast toast;
		    
		    if (db.addToProductTable(new Product(description, 0, quantity, barcode, size)) == -1){
		    	toast = Toast.makeText(getApplicationContext(),
    		    "Product not added", Toast.LENGTH_SHORT);
    		    toast.show();
			}else{
				toast = Toast.makeText(getApplicationContext(),
    		    "Product added to database.", Toast.LENGTH_SHORT);
    		    toast.show();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_to_db, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
