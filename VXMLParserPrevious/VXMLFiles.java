package vxmlparser;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VXMLFiles {

	Map<BigDecimal,List<VXMLData>> files= new HashMap<BigDecimal, List<VXMLData>>();
	
	public VXMLFiles(){
		files= new HashMap<BigDecimal, List<VXMLData>>();
	}
	
	public Map<BigDecimal,List<VXMLData>> getFiles(){
		return files;
	}
	
	public void setFiles(Map<BigDecimal,List<VXMLData>> files){
		this.files =files;
	}
	
	public void processMessageData() {

	}

	public void processGrammarFile() {

	}
}
