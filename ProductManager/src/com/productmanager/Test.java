package com.productmanager;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

public class Test {
	
	private DB_Helper db;

	public Test(Context context){

	    db = new DB_Helper(context);
	}

	public void testDelete(String barcode) {
//		db.deleteEntry("Cadbury Twirl");
//		db.deleteEntry("Twix");
		db.deleteEntry(barcode);
//		db.addToProductTable("Twix", 1.00, 40, "654688");
//		db.addToProductTable("Mars", 1.00, 40, "345504");
//		Log.d("Query: ", product.getBarcode() + " " + product.getSize());
//		Log.d("Select: ", "All twix");
//		String table = DB_Helper.TABLE_PRODUCTS;
//		String where = " WHERE " + DB_Helper.NAME + " = 'Twix'";
//		String strSQL = "SELECT * FROM " + table + where + ";";
//		Log.d("Select SQL Statement: ", strSQL);
//		List<Product> Products = db.queryCustom( strSQL );
//		for ( Product t : Products ) {
//			Log.d("Twix: ", t.toString() );
//		}
//		queryCustomTest(db, " =345504");
	}
	
	public void queryAllTest() {
		Log.d("Select: ", "Selecting all products");
		List<Product> products = db.queryAllProducts();
		StringBuilder output = new StringBuilder();
		for ( Product p : products ) {
			output.append(p.toString() + "\n");
		}
		Log.d("Query: ", output.toString() );
		
		
	}
	
	private void queryCustomTest(DB_Helper db, String where) {
		List<Product> products = db.queryCustom("Select * from " + DB_Helper.TABLE_PRODUCTS + " WHERE " + DB_Helper.BARCODE + where);
		for ( Product p : products ) {
			Log.d("Products: ", p.toString() );
		}
	}
}
