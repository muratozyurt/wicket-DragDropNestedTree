package com.tr.rdmlab.lib.wicket.dynamictree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SampleRecursiveObject  implements Serializable{

 
	private static final long serialVersionUID = 1L;

	long id;
	 
	String name; 
	 
	SampleRecursiveObject parentObject;
	List<SampleRecursiveObject> childObjects = new ArrayList<SampleRecursiveObject>();
	 
	boolean panelExpanded;
	
	public SampleRecursiveObject() {
	 
	}
	public SampleRecursiveObject(String name)
	{
		this();
		this.name = name;
	
	}
	
	public SampleRecursiveObject(SampleRecursiveObject parentObject, String name)
	{
		this(name);
		this.parentObject = parentObject; 
		if(parentObject != null)
		{
			parentObject.getChildObjects().add(this);
		}
		
	}
	
	public SampleRecursiveObject(long id, SampleRecursiveObject parentObject, String name)
	{
		this(parentObject, name);  
		this.id = id;
		
	}
	
	
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	
	public boolean isPanelExpanded() {
		return panelExpanded;
	}
	
	public void setPanelExpanded(boolean panelExpanded) {
		this.panelExpanded = panelExpanded;
	}
	
	 
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public SampleRecursiveObject getParentObject() {
		return parentObject;
	}
	public void setParentObject(SampleRecursiveObject parentObject) {
		this.parentObject = parentObject;
	}
	public List<SampleRecursiveObject> getChildObjects() {
		return childObjects;
	}
	public void setChildObjects(List<SampleRecursiveObject> childObjects) {
		this.childObjects = childObjects;
	} 
	 
	
}
