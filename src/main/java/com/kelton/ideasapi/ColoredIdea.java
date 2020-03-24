package com.kelton.ideasapi;

public class ColoredIdea extends Idea {

	private String color;
	
	public ColoredIdea() {}
	
	public ColoredIdea(Long id, String description, Status status, String color) {
		super(description, status);
		this.setId(id);
		this.color = color;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	
	
	
	
}
