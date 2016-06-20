package com.arff.events;

import java.util.EventObject;

public class Event extends EventObject{
	
	public static final int ATTRIBUTES_LIST_SUBMITTED = 0;
	
	
	
	
	public Event(Object source) {
		super(source);
		
	}
	
	
}
