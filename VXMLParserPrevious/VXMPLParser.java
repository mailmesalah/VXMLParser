package vxmlparser;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class VXMPLParser {

	//For storing the files structure with id anf vxml files
	private static Map<BigDecimal, List<String>> mIDnFiles = new HashMap<BigDecimal, List<String>>();
	//Its has all the combined files in structure
	private static VXMLFiles mVxmlFiles = new VXMLFiles();
	//Document for XML parsing
	private static Document mDocument;
	//current VXMLfile
	private static File mFXmlFile;

	public static void main(String[] args) {

		// Reading the folders and files inside the vxmlFiles folder
		File directory = new File("vxmlFiles");// reading the vxml folder
		if (directory.exists()) {// checks if the folder exists

			// lists all the file and folders in the string array
			File[] fListing = directory.listFiles();
			if (fListing != null) {
				// assuming vxmlfiles fodler has all folder and names with
				// BigDecimal
				for (int i = 0; i < fListing.length; i++) {
					// creating file object with the folder name
					File f = fListing[i];
					// calls the parse function to parse through the file system
					traverseFileSystemForVXMLFilesnID(f);
				}
			}
		}

		objectifyVXMLFilesLoaded();

	}

	/*
	 * This method is used to traverse the file system for id(folder name) and VXML files
	 */
	private static void traverseFileSystemForVXMLFilesnID(File directory) {

		File[] fileListing = directory.listFiles();
		List<String> fileList = new ArrayList<String>();
		if (fileListing != null) {
			for (int i = 0; i < fileListing.length; i++) {
				// iterate through all the sub files or folders of the argument
				// folder
				File file = fileListing[i];
				if (file.isDirectory()) {
					// then parse function of that particular folder is called
					traverseFileSystemForVXMLFilesnID(file);
				} else if (file.getAbsoluteFile().toString().endsWith(".vxml")) {
					// the file path is saved in the list
					fileList.add(file.getAbsoluteFile().toString());
				}

			}
		}

		// list of file path is attached with the folder id
		mIDnFiles.put(new BigDecimal(directory.getName()), fileList);

	}

	/*
	 * Method to print the traversed ids and related file system
	 */
	private static void printIDnFiles() {
		Set keys = mIDnFiles.keySet();
		for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
			BigDecimal key = (BigDecimal) iterator.next();
			List<String> files = mIDnFiles.get(key);
			for (int i = 0; i < files.size(); i++) {
				System.out.println("ID=" + key + " File=" + files.get(i));
			}
		}
	}

	/*
	 *Converts each vxmlfiles in the file system, which is loaded previously, to VXMLFile object 
	 */	
	private static void objectifyVXMLFilesLoaded() {
		//gets all the id
		Set ids = mIDnFiles.keySet();
		for (Iterator iterator = ids.iterator(); iterator.hasNext();) {
			BigDecimal appid = (BigDecimal) iterator.next();
			//gets all the vxmlfile names under the app id
			List<String> files = mIDnFiles.get(appid);
			for (int i = 0; i < files.size(); i++) {		
				//loads each vxml file for processing
				loadXMLFile(files.get(i));
				// parse through the vxml file and process it to convert to vxmlFile
				parseXML2VXMLFile(appid);
			}
		}
	}

	/*
	 * Loads the current xml file in DOM XML parser
	 */	
	private static void loadXMLFile(String xmlFile) {
		try {
			//vxmlfile
			mFXmlFile = new File(xmlFile);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			mDocument = dBuilder.parse(mFXmlFile);
			// optional, but recommended
			mDocument.getDocumentElement().normalize();
		} catch (Exception e) {

		}
	}

	/*
	 * Parser through current VXML File to convert to VXMLFile object
	 */	
	private static void parseXML2VXMLFile(BigDecimal appID) {
		try {

			//Creates VxmlData object and sets its values
			VXMLData vxmlData = new VXMLData();			
			vxmlData.setAppId(appID);
			vxmlData.setFileName(mFXmlFile.getName());
			vxmlData.setFilePath("");

			//Search for Script on document level and store it in script member of VxmlData
			NodeList nList = mDocument.getDocumentElement().getChildNodes();
			if (nList.getLength() > 0) {
				for (int i = 0; i < nList.getLength(); i++) {
					switch (nList.item(i).getNodeType()) {
						case Node.ELEMENT_NODE : {
							Element element = (Element) nList.item(i);
							if (element.getNodeName().equalsIgnoreCase("script")&& element.getTextContent().length() > 0) {							
								vxmlData.setScript(element.getTextContent());
							}

							break;
						}
					}
				}
			}

			vxmlData.setParentFile(mVxmlFiles);

			//Traverse through all the Forms in the current document
			nList = mDocument.getElementsByTagName("form");
			if (nList.getLength() > 0) {
				for (int i = 0; i < nList.getLength(); i++) {
					//VXMLForm object is created to store form data
					VXMLForm vxmlForm = new VXMLForm();
					//check if current node of current form is an element
					if (nList.item(i).getNodeType() == Node.ELEMENT_NODE) {
						Element nElement = (Element) nList.item(i);
						//Form object is populated with xml form data
						// FormVXML
						vxmlForm.setFormVXML(getElementXML(nElement));
						// FormName
						vxmlForm.setFormName(nElement.getAttribute("id"));
						// GrammerFileReference
						NodeList nlGrammar = nElement.getElementsByTagName("grammar");
						if (nlGrammar.getLength() > 0) {
							for (int j = 0; j < nlGrammar.getLength(); j++) {
								if(nlGrammar.item(0).getAttributes().getLength()>0){
									vxmlForm.setGrammarFileReference("<grammar "+nlGrammar.item(0).getAttributes().item(0)+"/>");									
								}
							}
						}						

						// InputModes
						NodeList nlProperty = nElement.getElementsByTagName("property");
						for (int j = 0; j < nlProperty.getLength(); j++) {							
							for (int j2 = 0; j2 < nlProperty.item(j).getAttributes().getLength(); j2++) {											
								if (nlProperty.item(j).getAttributes().item(j2).getNodeName().trim().equalsIgnoreCase("name")&&nlProperty.item(j).getAttributes().item(j2).getNodeValue().trim().equalsIgnoreCase("inputmodes")) {																		
									for (int k = 0; k < nlProperty.item(j).getAttributes().getLength(); k++) {
										if(nlProperty.item(j).getAttributes().item(k).getNodeName().trim().equalsIgnoreCase("value")){
											vxmlForm.setInputModes(nlProperty.item(j).getAttributes().item(k).getNodeValue().trim());
											break;		
										}
									}
								}

							}
						}
						
						// NoInputCount
						NodeList nlCatch = nElement.getElementsByTagName("catch");						
						for (int j = 0; j < nlCatch.getLength(); j++) {							
							for (int j2 = 0; j2 < nlCatch.item(j).getAttributes().getLength(); j2++) {																
								if (nlCatch.item(j).getAttributes().item(j2).getNodeName().trim().equalsIgnoreCase("event")&&nlCatch.item(j).getAttributes().item(j2).getNodeValue().trim().equalsIgnoreCase("noinput")) {																		
									for (int k = 0; k < nlCatch.item(j).getAttributes().getLength(); k++) {
										if(nlCatch.item(j).getAttributes().item(k).getNodeName().trim().equalsIgnoreCase("count")){
											vxmlForm.setNoInputCount(Integer.parseInt(nlCatch.item(j).getAttributes().item(k).getNodeValue().trim()));
											break;		
										}
									}
								}
							}
						}
						
						// NoMatchCount
						nlCatch = nElement.getElementsByTagName("catch");						
						for (int j = 0; j < nlCatch.getLength(); j++) {							
							for (int j2 = 0; j2 < nlCatch.item(j).getAttributes().getLength(); j2++) {																
								if (nlCatch.item(j).getAttributes().item(j2).getNodeName().trim().equalsIgnoreCase("event")&&nlCatch.item(j).getAttributes().item(j2).getNodeValue().trim().equalsIgnoreCase("nomatch")) {																		
									for (int k = 0; k < nlCatch.item(j).getAttributes().getLength(); k++) {
										if(nlCatch.item(j).getAttributes().item(k).getNodeName().trim().equalsIgnoreCase("count")){
											vxmlForm.setNoMatchCount(Integer.parseInt(nlCatch.item(j).getAttributes().item(k).getNodeValue().trim()));
											break;		
										}
									}
								}
							}
						}
						
						// Goto
						NodeList nlGoto = nElement.getElementsByTagName("goto");
						for (int j = 0; j < nlGoto.getLength(); j++) {							
							for (int j2 = 0; j2 < nlGoto.item(j).getAttributes().getLength(); j2++) {																
								if (nlGoto.item(j).getAttributes().item(j2).getNodeName().trim().equalsIgnoreCase("next")) {																		
									vxmlForm.getTargetMenus().add(nlGoto.item(j).getAttributes().item(j2).getNodeValue().trim());									
								}
							}
						}
						
						// Script
						NodeList nlScript = nElement.getElementsByTagName("script");
						for (int j = 0; j < nlScript.getLength(); j++) {							
							if(nlScript.item(0).getTextContent().length()>0){
								vxmlForm.setScript(nlScript.item(0).getTextContent());
								break;
							}
						}													

					}
					//Adding the vxmlForm object to vxmlData
					vxmlData.getForms().add(vxmlForm);
				}
			}
			//Adding the vxmlData object to the vxmlFile
			if(mVxmlFiles.getFiles().containsKey(appID)){
				mVxmlFiles.getFiles().get(appID).add(vxmlData);
			}else{
				mVxmlFiles.getFiles().put(appID, new ArrayList<>());
				mVxmlFiles.getFiles().get(appID).add(vxmlData);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/*
	 * Starting method for recursive traversing of xml element
	 */
	private static String getElementXML(Node nElement) {
		String sElements = "";
		sElements = sElements + "\n<" + nElement.getNodeName();
		SubElement se = getXMLOfElement(nElement, 1);
		// check if the node has attributes
		if (se.getAttributes().length() > 0) {
			sElements = sElements + " " + se.getAttributes();
		}

		// check if the node has elements
		if (se.getElements().length() > 0) {
			sElements = sElements + ">" + se.getElements() + "\n<"
					+ nElement.getNodeName() + "/>";
		} else {
			sElements = sElements + "/>";
		}

		return sElements;
	}

	/*
	 * Recursive method for traversing through xml element
	 */
	private static SubElement getXMLOfElement(Node nElement, int indent) {
		String sAttributes = "";
		String sElements = "";

		NodeList nl = nElement.getChildNodes();
		if (nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				Node node = nl.item(i);
				switch (node.getNodeType()) {

					case Node.ELEMENT_NODE : {
						String sTab = repeatString("\t", indent);
						Element element = (Element) node;
						sElements = sElements + "\n" + sTab + "<"
								+ element.getNodeName();
						SubElement se = getXMLOfElement(node, ++indent);
						--indent;
						// check if the node has attributes
						if (se.getAttributes().length() > 0) {
							sElements = sElements + " " + se.getAttributes();
						}

						// check if the node has elements
						if (se.getElements().length() > 0) {
							sElements = sElements + ">" + se.getElements()
									+ "\n" + sTab + "<" + element.getNodeName()
									+ "/>";
						} else {
							sElements = sElements + "/>";
						}
						break;
					}

					case Node.CDATA_SECTION_NODE : {
						System.out.println("CData");
						break;
					}
					case Node.COMMENT_NODE : {
						System.out.println("Comment");
						break;
					}
					case Node.DOCUMENT_FRAGMENT_NODE : {
						System.out.println("Doc Frag");
						break;
					}
					case Node.DOCUMENT_NODE : {
						System.out.println("Doc");
						break;
					}
					case Node.DOCUMENT_TYPE_NODE : {
						System.out.println("Doc Type");
						break;
					}
					case Node.ENTITY_NODE : {
						System.out.println("Entity");
						break;
					}
					case Node.ENTITY_REFERENCE_NODE : {
						System.out.println("Entity Ref");
						break;
					}
					case Node.NOTATION_NODE : {
						System.out.println("Notation");
						break;
					}
					case Node.PROCESSING_INSTRUCTION_NODE : {
						System.out.println("Process");
						break;
					}
					case Node.TEXT_NODE : {
						Text t = (Text) node;
						// System.out.println("Text "+node.getNodeValue().trim());
						break;
					}
					default :
						System.out.println("This Type is Not Included Type="
								+ node.getNodeType());

				}
			}						
		}
		
		NamedNodeMap attribNodes = nElement.getAttributes();
		if (attribNodes.getLength() > 0) {				
			for (int i = 0; i < attribNodes.getLength(); i++) {
				sAttributes = sAttributes + attribNodes.item(i)+" ";					
			}
		}

		return new SubElement(sAttributes, sElements);
	}

	private static String repeatString(String s, int n) {
		String val = "";
		for (int i = 0; i < n; i++) {
			val = val + s;
		}
		return val;
	}

}
