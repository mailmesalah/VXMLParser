package vxmlparser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VXMLData {

	BigDecimal appId;
	String script;
	String fileName;
	String filePath;
	List<VXMLForm> forms = new ArrayList<VXMLForm>();
	VXMLFiles parentFile = new VXMLFiles();
	
	public VXMLData(){
		
	}
	
	public void setAppId(BigDecimal appId){
		this.appId=appId;
	}
	
	public BigDecimal getAppId(){
		return this.appId;
	}
	
	public void setScript(String script){
		this.script=script;
	}
	
	public String getScript(){
		return this.script;
	}
	
	public void setFileName(String fileName){
		this.fileName=fileName;
	}
	
	public String getFileName(){
		return this.fileName;
	}
	
	public void setFilePath(String filePath){
		this.filePath=filePath;
	}
	
	public String getFilePath(){
		return this.filePath;
	}
	
	public void setForms(List<VXMLForm> forms){
		this.forms=forms;
	}
	
	public List<VXMLForm> getForms(){
		return this.forms;
	}
	
	public void setParentFile(VXMLFiles parentFile){
		this.parentFile=parentFile;
	}
	
	public VXMLFiles getParentFile(){
		return this.parentFile;
	}
	
	public void processMessageData() {

	}

	public void processGrammarFile() {

	}
}
