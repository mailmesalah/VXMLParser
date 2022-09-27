package vxmlparser;

import java.math.BigDecimal;
import java.nio.file.FileSystem;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		Set<BigDecimal>appIDs= files.keySet();
		for (Iterator iterator = appIDs.iterator(); iterator.hasNext();) {
			BigDecimal id = (BigDecimal) iterator.next();
			List<VXMLData>vxmlData= files.get(id);
			for (int i = 0; i < vxmlData.size(); i++) {
				vxmlData.get(i).processMessageData();
			}
		}
	}

	public void processGrammarFile() {
		Set<BigDecimal>appIDs= files.keySet();
		for (Iterator iterator = appIDs.iterator(); iterator.hasNext();) {
			BigDecimal id = (BigDecimal) iterator.next();
			List<VXMLData>vxmlData= files.get(id);
			for (int i = 0; i < vxmlData.size(); i++) {
				vxmlData.get(i).processGrammarFile();
			}
		}
	}
	
	public String toString() {
		StringBuilder response = new StringBuilder();
		String lineSeparator = System.getProperty("line.separator");
		
		Set<BigDecimal> ids = files.keySet();
		for (Iterator<BigDecimal> iterator = ids.iterator(); iterator.hasNext();) {
			BigDecimal appid = (BigDecimal) iterator.next();

			response.append("App Id: ");
			response.append(appid);
			response.append(lineSeparator);
			
			List<VXMLData> vxmlData = files.get(appid);

			response.append("VXML File Count for this Application: ");
			response.append(vxmlData.size());
			response.append(lineSeparator);

			for (int i = 0; i < vxmlData.size(); i++) {	
				response.append(vxmlData.get(i).toString());
				response.append(lineSeparator);
			}
			response.append(lineSeparator);
		}
		
		return response.toString();
	}
}
