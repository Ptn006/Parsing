package com.ssc.eha.tools.server.standalone;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;




/**
 * @description: SettingsDOMParser.java 
 * @createdOn : 03/2017
 * 
 * @author Tulasi Pilli
 * @version 1.0.
 */
public class SettingsDOMParser
{
    	public static  String text="";
    	 
     
	public static void main(String[] args)
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try
		{   	
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			org.w3c.dom.Document doc = builder.parse("WEB-INF/cfg/settings_prod_clo.xml");
			
			Node settingsNode = doc.getElementsByTagName("settings").item(0);
			
			printAllElements(settingsNode);
			printToPdf(text);

		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	private static void printAllElements(Node node)
	{
		System.out.println("\n-------------------------");
		System.out.print("Element: " + node.getNodeName());
		 text += "\n-------------------------";
		 text+=  "Element: " + node.getNodeName();
	          
		//printToPdf("\n-------------------------");
		//printToPdf("Element: " + node.getNodeName());
		
		printAllAttributes(node);
		
		NodeList childNodes = node.getChildNodes();
		if(childNodes.getLength() > 0)
		{
			for(int i=0; i < childNodes.getLength(); i++)
			{
				Node childNode = childNodes.item(i);
				
				if (childNode.getNodeType() == Node.ELEMENT_NODE)
				{	
					printAllElements(childNode);
				}
				else if (childNode.getNodeType() == Node.COMMENT_NODE)
				{	
					System.out.println("\n" + childNode.getTextContent().trim());
					//printToPdf("\n" + childNode.getTextContent().trim());
					text+="\n" + childNode.getTextContent().trim();
				}
			}
		}		
	}	
	
	private static void printAllAttributes(Node node)
	{	
		NamedNodeMap nodeAttributes = node.getAttributes();		
		if(nodeAttributes.getLength() > 0)
		{
			System.out.print("(");
			text+="(";
			//printToPdf("(");
			boolean first = true;
			for(int i=0; i < nodeAttributes.getLength(); i++)
			{
				Node attr = nodeAttributes.item(i);
				
				if(first)
					first = false;
				else
					System.out.print(", ");
				text+=", ";
					//printToPdf("' ");
				System.out.print(attr.getNodeName() + ": " + attr.getTextContent());
				text+=attr.getNodeName() + ": " + attr.getTextContent();
				//printToPdf(attr.getNodeName() + ": " + attr.getTextContent());
			}
			
			System.out.print(")");
			text+=")";
			//printToPdf(")");
		}
	}
	
	private static void printToPdf(String text)
	{
	    
	    Document document = new Document();
	    //File document =new File("H:\\HelloWorld.pdf");
	    try {
	            PdfWriter.getInstance(document,
	                            new FileOutputStream("H:\\Settings_pdf.pdf"));
	           
	            document.open();
	            document.add(new Paragraph(text));
	            
	            System.out.print("SUCCESS");
	    } catch (DocumentException e) {
	            System.err.println(e.getMessage());
	    } catch (IOException ex) {
	            System.err.println(ex.getMessage());
	    }
	    document.close();
	}
	
	
	
	
}