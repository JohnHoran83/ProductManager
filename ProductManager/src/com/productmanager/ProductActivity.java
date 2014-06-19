package com.productmanager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import de.timroes.axmlrpc.XMLRPCClient;
import de.timroes.axmlrpc.XMLRPCException;

public class ProductActivity extends Activity implements OnClickListener {
	
	private Button scanBtn;
	DB_Helper db;
	ProgressDialog dialog;
	private Button buttonSync;

	@Override
	protected void onStop() {
		super.onPause();
		if (dialog.isShowing()) {
            dialog.dismiss();
        }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product);
		
		buttonSync = (Button) findViewById(R.id.sync_button);
		dialog = new ProgressDialog(this);
		initialiseView();
		db = new DB_Helper( this );
		
		buttonSync.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				new Test(ProductActivity.this).queryAllTest();
				//Get the data
	//			DoSync mDoSync = new DoSync(ProductActivity.this, editTextSearchString.getText().toString());
	//			mDoSync.execute("");
			}
		});
	}

	private void initialiseView() {
		scanBtn = (Button)findViewById(R.id.scan_button);
		scanBtn.setOnClickListener(this);
	}
	
	public void onClick(View v){
		if(v.getId()==R.id.scan_button){
			initiateScan();
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		String scanResult = getScanResult(requestCode, resultCode, intent);
		if (scanResult != null)
			new UPCDatabaseQuery(scanResult).execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.product, menu);
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
	
	/************************************  Barcode methods  ***************************************************/
	
	public void initiateScan() {
		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
		scanIntegrator.initiateScan();
	}

	public String getScanResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		String scanContent = null;
		if (scanningResult != null) {
			scanContent = scanningResult.getContents();
			
		}else{
		    Toast toast = Toast.makeText(getApplicationContext(),
		    "No scan data received!", Toast.LENGTH_SHORT);
		    toast.show();
		}
		return scanContent;
	}	

	public class UPCDatabaseQuery extends AsyncTask<Void, Integer, Void> {

		public static final String BARCODE = "com.productmanager.ProductActivity.UPCDatabaseQuery.BARCODE";
		public static final String RESULT_SIZE = "com.productmanager.ProductActivity.UPCDatabaseQuery.RESULT_SIZE";
		public static final String RESULT_DESC = "com.productmanager.ProductActivity.UPCDatabaseQuery.RESULT_DESC";
		
		private String barcode;
		private String resultDesc;
		private String resultSize;

		public UPCDatabaseQuery(String barcode) {
			this.barcode = barcode;
		}
		
		@Override
		protected void onPreExecute() {
	        dialog.setMessage("Please wait");
	        dialog.show();
	    }
		
		@Override
		protected Void doInBackground(Void... params) {
			searchUPCdatabase();
			return null;
		}
		
		@Override
	    protected void onPostExecute(Void result) {
			if (dialog.isShowing()) {
	            dialog.dismiss();
	        }
			if (resultDesc == null){
				Toast toast = Toast.makeText(getApplicationContext(),
				"Product not found", Toast.LENGTH_SHORT);
				toast.show();
			}else{
				sendToDataBaseActivity();
			}
	    }
		
		public void sendToDataBaseActivity() {
		    Intent intent = new Intent(ProductActivity.this, AddToDBActivity.class);
		    intent.putExtra(BARCODE, barcode);
		    intent.putExtra(RESULT_SIZE, resultSize);
		    intent.putExtra(RESULT_DESC, resultDesc);
		    startActivity(intent);
		}
		
		public void searchUPCdatabase() {
			
			URL url = null;
			try {
				url = new URL("http://www.upcdatabase.com/xmlrpc");
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			while (barcode.length() < 13){
				barcode = "0" + barcode;
			}

			try {
				XMLRPCClient client  = new XMLRPCClient(url);
				Map<String, String> params = new HashMap<String, String>();
				params.put("rpc_key", "b9fc5e23ef1ce970515ca0151838defb62ef5b16");
				params.put("ean",barcode);
				HashMap result = (HashMap) client.call("lookup", params);

				resultSize = result.get("size").toString();
				resultDesc = result.get("description").toString();	

			} catch (NullPointerException e) {
				e.printStackTrace();

			} catch (XMLRPCException e) {
				e.printStackTrace();
			}
		}	
	}
}