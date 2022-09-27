package vxmlparser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VXMLForm {

	BigDecimal menuID;
	String script;
	String formName = "Default";
	String formVXML;
	String grammarFileReference = null;
	String grammarText;
	String inputModes = "None";
	int noInputCount = -1;
	int noMatchCount = -1;
	List<String> targetMenus = new ArrayList<>();
	Map<String, String[]> messages = new HashMap<>();
	VXMLData parent;

	public VXMLForm() {

	}

	public void setMenuID(BigDecimal menuID) {
		this.menuID = menuID;
	}

	public BigDecimal getMenuID() {
		return this.menuID;
	}

	public void setParent(VXMLData parent) {
		this.parent = parent;
	}

	public VXMLData getParent() {
		return this.parent;
	}
	
	public void setScript(String script) {
		this.script = script;
	}

	public String getScript() {
		return this.script;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getFormName() {
		return this.formName;
	}

	public void setFormVXML(String formVXML) {
		this.formVXML = formVXML;
	}

	public String getFormVXML() {
		return this.formVXML;
	}

	public void setGrammarFileReference(String grammarFileReference) {
		this.grammarFileReference = grammarFileReference;
	}

	public String getGrammarFileReference() {
		return this.grammarFileReference;
	}

	public void setGrammarText(String grammarText) {
		this.grammarText = grammarText;
	}

	public String getGrammarText() {
		return this.grammarText;
	}

	public void setInputModes(String inputModes) {
		this.inputModes = inputModes;
	}

	public String getInputModes() {
		return this.inputModes;
	}

	public void setNoInputCount(int noInputCount) {
		this.noInputCount = noInputCount;
	}

	public int getNoInputCount() {
		return this.noInputCount;
	}

	public void setNoMatchCount(int noMatchCount) {
		this.noMatchCount = noMatchCount;
	}

	public int getNoMatchCount() {
		return this.noMatchCount;
	}

	public void setTargetMenus(List<String> targetMenus) {
		this.targetMenus = targetMenus;
	}

	public List<String> getTargetMenus() {
		return this.targetMenus;
	}

	public void setMessages(Map<String, String[]> messages) {
		this.messages = messages;
	}

	public Map<String, String[]> getMessages() {
		return this.messages;
	}

	public void processMessageData() {		
			Set<String> fileNames = messages.keySet();
			for (Iterator<String> iterator = fileNames.iterator(); iterator.hasNext();) {
				String fileName = (String) iterator.next();
				String[] fileLocationAndText = messages.get(fileName);

				StringTokenizer st = new StringTokenizer(fileLocationAndText[0], "+");								
				String fileLoc="";
				while (st.hasMoreTokens()) {
				
					String currentToken = st.nextToken().trim();
					if (!currentToken.startsWith("'")) {						
						fileLoc=fileLoc+getVariableValue(currentToken);
					}else{
						fileLoc=fileLoc+currentToken.replace("'", "");
					}
				}
				messages.get(fileName)[0]=fileLoc;
			}
		

	}

	private String getVariableValue(String sVariable){
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
		}else{			
			return parent.getVariableValue(sVariable);			
		}
		
		return null;
	}
	
	public void processGrammarFile() {
		//Processing GrammarFileReference	
		if(grammarFileReference!=null){
			String sVal=grammarFileReference;
			Pattern pattern= Pattern.compile("\"(.*)\"");
			Matcher matcher=pattern.matcher(sVal);
			String fileLoc="";
			if(matcher.find()){
				StringTokenizer st = new StringTokenizer(matcher.group().replace("\"", ""), "+");								
				while (st.hasMoreTokens()) {
					
					String currentToken = st.nextToken().trim();
					if (!currentToken.startsWith("'")) {						
						System.out.println("Variable " + currentToken);
						fileLoc=fileLoc+getVariableValue(currentToken);
					}else{
						fileLoc=fileLoc+currentToken.replace("'", "");
					}
				}			
			}		
			grammarFileReference=fileLoc;
		}
	}

	public String toString() {
		StringBuilder response = new StringBuilder();
		String lineSeparator = System.getProperty("line.separator");

		response.append(lineSeparator);
		response.append("******************* VXML FORM START ******************");
		response.append(lineSeparator);

		response.append("Menu ID: ");
		response.append(this.menuID);
		response.append(lineSeparator);

		response.append("Script: ");
		response.append(this.script);
		response.append(lineSeparator);

		response.append("Form Name: ");
		response.append(this.formName);
		response.append(lineSeparator);

		response.append("VXML: ");
		response.append(this.formVXML);
		response.append(lineSeparator);

		response.append("Grammar File Reference: ");
		response.append(this.grammarFileReference);
		response.append(lineSeparator);

		response.append("Grammar Text: ");
		response.append(this.grammarText);
		response.append(lineSeparator);

		response.append("Input Modes: ");
		response.append(this.inputModes);
		response.append(lineSeparator);

		response.append("No Input Count: ");
		response.append(this.noInputCount);
		response.append(lineSeparator);

		response.append("No Match Count: ");
		response.append(this.noMatchCount);
		response.append(lineSeparator);

		response.append("Target Menus: ");
		boolean first = true;
		for (String target : this.targetMenus) {
			response.append(target);
			if (!first) {
				response.append(", ");
			}
			first = false;
		}
		response.append(lineSeparator);
		response.append(lineSeparator);

		response.append("Menu Messages: ");
		response.append(lineSeparator);
		Set<String> fileNames = messages.keySet();
		for (Iterator<String> iterator = fileNames.iterator(); iterator
				.hasNext();) {
			String fileName = (String) iterator.next();
			String[] fileLocationAndText = messages.get(fileName);

			response.append("File Name: ");
			response.append(fileName);
			response.append(", File Location: ");
			response.append(fileLocationAndText[0]);
			response.append(", Message Text: ");
			response.append(fileLocationAndText[1]);
			response.append(lineSeparator);
		}

		response.append("******************* VXML FORM END ******************");
		response.append(lineSeparator);

		return response.toString();
	}
}
