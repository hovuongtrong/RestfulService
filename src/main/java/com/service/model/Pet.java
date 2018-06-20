package com.service.model;

public class Pet {
	private String name;
	private String category;
	private int age;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	public Pet() {}
	public Pet(String name, String cat, int age) {
		this.name = name;
		this.category = cat;
		this.age = age;
	}
	
}
