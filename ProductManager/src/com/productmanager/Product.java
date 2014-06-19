package com.productmanager;

public class Product {
	
	private String description;
	private double price;
	private int quantity;
	private String size;
	private int id;
	private String barcode;

	public Product(String description, double price, int quantity, String barcode, String size) {
		this.description = description;
		this.price = price;
		this.quantity = quantity;
		this.barcode = barcode;
		this.size = size;
	}

	public Product(int id, String description, double price, int quantity, String barcode, String size) {
		this.id = id;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
		this.barcode = barcode;
		this.size = size;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public String toString(){
		return description + " " + price + " " + quantity + " " + barcode + " " + size;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
}
