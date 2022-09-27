package vxmlparser;

public class SubElement {
	String attributes="";
	String elements="";
	
	public SubElement(){
		
	}
	
	public SubElement(String sAttributes, String sElements) {
		this.attributes=sAttributes;
		this.elements=sElements;
	}

	public void setAttributes(String attr){
		this.attributes=attr;
	}
	
	public String getAttributes(){
		return this.attributes;
	}
	
	public void setElements(String element){
		this.elements=element;
	}
	
	public String getElements(){
		return this.elements;
	}
}
