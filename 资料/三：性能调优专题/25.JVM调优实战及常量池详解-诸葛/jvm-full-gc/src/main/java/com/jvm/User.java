package com.jvm;

public class User {
	
	private int id;
	private String name;

//	byte[] a = new byte[1024*100];


	public User(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	@Override
	protected void finalize() throws Throwable {
//		super.finalize();
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
