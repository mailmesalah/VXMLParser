package vxmlparser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		for (Iterator iterator = forms.iterator(); iterator.hasNext();) {
			VXMLForm vxmlForm = (VXMLForm) iterator.next();
			vxmlForm.processMessageData();
		}
	}
	
	public void processGrammarFile() {
		for (Iterator iterator = forms.iterator(); iterator.hasNext();) {
			VXMLForm vxmlForm = (VXMLForm) iterator.next();
			vxmlForm.processGrammarFile();
		}
	}

	public String getVariableValue(String sVariable){
		if(script!=null){
			//Check for pattern sVariable+"\\s*=\\s*initSystemTypeVariable\\(\\s*'"+sVar+"'\\s?,\\s?'(.+)'
			String sVar=sVariable.substring(sVariable.lastIndexOf(".")+1, sVariable.length());
			Pattern pattern= Pattern.compile(sVariable+"\\s*=\\s*initSystemTypeVariable\\(\\s*'"+sVar+"'\\s?,\\s?'(.+)'");
			Matcher matcher=pattern.matcher(script);
			if(matcher.find()) {
                String sMatch=matcher.group();
                sMatch=sMatch.replace("'", "");
                sMatch=sMatch.substring(sMatch.lastIndexOf(",")+1,sMatch.length());
                return sMatch;
			}
			
			//Check for pattern	State.APP_LANGUAGE\s?=\s?'(.+)'	
			pattern= Pattern.compile(sVariable+"\\s*=\\s*'(.+)'");
			matcher=pattern.matcher(script);
			if(matcher.find()) {
                String sMatch=matcher.group();
                sMatch=sMatch.replace("'", "");
                sMatch=sMatch.substring(sMatch.lastIndexOf(" ")+1,sMatch.length());
                return sMatch;
			}			
		}
		
		return null;
	}
	
	
	public String toString()
	{
		StringBuilder response = new StringBuilder();
		String lineSeparator = System.getProperty("line.separator");
	
		response.append(lineSeparator);
		response.append("********************************************************************");
		response.append(lineSeparator);
		response.append("******************* VXML DATA (APPLICATION) START ******************");
		response.append(lineSeparator);
		
		response.append("File Name: ");
		response.append(this.fileName);
		response.append(lineSeparator);

		response.append("File Path: ");
		response.append(this.filePath);
		response.append(lineSeparator);
		
		response.append("Script: ");
		response.append(this.script);
		response.append(lineSeparator);
		
		for(VXMLForm form : this.forms)
		{
			response.append(form.toString());
		}
		
		response.append("******************* VXML DATA (APPLICATION) END ******************");
		response.append(lineSeparator);
		response.append("********************************************************************");
		response.append(lineSeparator);
		response.append(lineSeparator);
		
		return response.toString();
	}
}
