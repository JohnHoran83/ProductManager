package com.productmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DB_Helper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "products";
	public static final String TABLE_PRODUCTS = "products";

	public static String _ID= "_id";
	public static final String DESCRIPTION = "description";
	public static final String PRICE = "price";
	public static final String QUANTITY = "quantity";
	public static final String BARCODE = "barcode";
	private static final String SIZE = "size";
	
	private static int DATABASE_VERSION = 1;
	
//	private Context appContext;
	
	private static final String CREATE_PRODUCT_TABLE = 
			"CREATE TABLE " + TABLE_PRODUCTS + 
			" (" + 
					_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					DESCRIPTION + " TEXT, " + 
					PRICE + " REAL, " + 
					QUANTITY + " INTEGER, "  +
					BARCODE + " TEXT UNIQUE, " + 
					SIZE + " TEXT  );";


	public static final String DROP_PRODUCTS_TABLE =       // SQL DDL statement to delete table
			"DROP TABLE IF EXISTS " + TABLE_PRODUCTS + ";";

	// constructor
	public DB_Helper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
//		this.appContext = context.getApplicationContext();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DROP_PRODUCTS_TABLE);		
		createProductsTable(db);
	}

	private void createProductsTable(SQLiteDatabase db) {
		Log.d("createProductsTable() method: ", "Create the products Table");
		db.execSQL(CREATE_PRODUCT_TABLE);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL( DROP_PRODUCTS_TABLE );
		onCreate(db);
	}

	public List<Product> queryAllProducts() {
		String strSQL = "SELECT * FROM " + TABLE_PRODUCTS;
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(strSQL, null);
		List<Product> productList = cursorToList( cursor );
		db.close();
		return productList;
	}
	
	public List<Product> queryCustom(String strSQL) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor =  db.rawQuery(strSQL, null);
		List<Product> eventList = cursorToList( cursor );
		db.close();
		return eventList;		
	}

	private List<Product> cursorToList(Cursor cursor) {
		List<Product> productList = new ArrayList<Product>();
		if ( cursor.moveToFirst() ) {
			 do {
				 int id = cursor.getInt( cursor.getColumnIndex(_ID) );
				 String description = cursor.getString( cursor.getColumnIndex( DESCRIPTION) );
				 double price = cursor.getDouble( cursor.getColumnIndex( PRICE) );
				 int quantity = cursor.getInt( cursor.getColumnIndex( QUANTITY) );
				 String barcode = cursor.getString( cursor.getColumnIndex( BARCODE) );   
				 String size = cursor.getString( cursor.getColumnIndex( SIZE) );
				productList.add( new Product(id, description, price, quantity, barcode, size) );
	            } while (cursor.moveToNext());
	        }
		return productList;
	}
	
	public boolean deleteEntry(String barcode) {
		SQLiteDatabase db = getReadableDatabase();
		db.delete(TABLE_PRODUCTS, BARCODE + "=" + "'" + barcode + "'", null);
		return false;		
	}

	public long addToProductTable(Product product) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put( DESCRIPTION, product.getDescription());
		values.put( PRICE, product.getPrice());
		values.put( QUANTITY, product.getQuantity());
		values.put( BARCODE, product.getBarcode());
		values.put( SIZE, product.getSize());
		long insertID = db.insert(TABLE_PRODUCTS, null, values);
		db.close();
		return insertID;	
	}
}
