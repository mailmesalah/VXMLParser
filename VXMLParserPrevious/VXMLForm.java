package vxmlparser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VXMLForm {

	BigDecimal menuID;
	String script;
	String formName="Default";
	String formVXML;
	String grammarFileReference=null;
	String grammarText;
	String inputModes="None";
	int noInputCount=-1;
	int noMatchCount=-1;
	List<String> targetMenus = new ArrayList<>();
	Map<String,String[]> messages = new HashMap<>();
	
	public VXMLForm(){
		
	}
	
	public void setMenuID(BigDecimal menuID){
		this.menuID=menuID;
	}
	
	public BigDecimal getMenuID(){
		return this.menuID;
	}
	
	public void setScript(String script){
		this.script=script;
	}
	
	public String getScript(){
		return this.script;
	}
	
	public void setFormName(String formName){
		this.formName=formName;
	}
	
	public String getFormName(){
		return this.formName;
	}	
	
	public void setFormVXML(String formVXML){
		this.formVXML=formVXML;
	}
	
	public String getFormVXML(){
		return this.formVXML;
	}
	
	public void setGrammarFileReference(String grammarFileReference){
		this.grammarFileReference=grammarFileReference;
	}
	
	public String getGrammarFileReference(){
		return this.grammarFileReference;
	}
	
	public void setGrammarText(String grammarText){
		this.grammarText=grammarText;
	}
	
	public String getGrammarText(){
		return this.grammarText;
	}
	
	public void setInputModes(String inputModes){
		this.inputModes=inputModes;
	}
	
	public String getInputModes(){
		return this.inputModes;
	}
	
	public void setNoInputCount(int noInputCount){
		this.noInputCount=noInputCount;		
	}
	
	public int getNoInputCount(){
		return this.noInputCount;
	}
	
	public void setNoMatchCount(int noMatchCount){
		this.noMatchCount=noMatchCount;
	}
	
	public int getNoMatchCount(){
		return this.noMatchCount;
	}
	
	public void setTargetMenus(List<String> targetMenus){
		this.targetMenus=targetMenus;
	}
	
	public List<String> getTargetMenus(){
		return this.targetMenus;
	}
	
	public void setMessages(Map<String,String[]> messages){
		this.messages=messages;
	}
	
	public Map<String,String[]> getMessages(){
		return this.messages;
	}
	
	public void processMessageData(){
		
	}
	
	public void processGrammarFile(){
		
	}
	
	public String toString(){
		
		return null;
	}
}
