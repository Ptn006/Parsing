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

import com.lowagie.text.Chapter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;


/**
 * @description: SettingsTableParser.java 
 * @createdOn : 02/2017
 * 
 * @author Tulasi Pilli
 * @version 1.0.
 */
public class SettingsTableParser
{
    	
    	public static PdfPTable pdftable = new PdfPTable(1); 
    	public static  String child_attribute="";
    	
         
     
	public static void main(String[] args)
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try
		{   	
		    	pdftable.setSpacingBefore(25);             
		    	pdftable.setSpacingAfter(25);
		        DocumentBuilder builder = factory.newDocumentBuilder();
			
			org.w3c.dom.Document doc = builder.parse("WEB-INF/cfg/settings_prod_clo.xml");
			
			Node settingsNode = doc.getElementsByTagName("settings").item(0);
			
			printAllElements(settingsNode);
			printToPdf();

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
		
		
		PdfPCell Headers = new PdfPCell(new Phrase(node.getNodeName(),FontFactory.getFont(FontFactory.TIMES_ROMAN, 
			10, Font.BOLD,new CMYKColor(10, 200, 0, 0)))); 
		
		pdftable.addCell(Headers);
		
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
					
				}
			}
		}		
	}	
	
	private static void printAllAttributes(Node node)
	{	
		NamedNodeMap nodeAttributes = node.getAttributes();		
		if(nodeAttributes.getLength() > 0)
		{
		    	child_attribute="";
		    	System.out.print("(");
		    	
			
			boolean first = true;
			for(int i=0; i < nodeAttributes.getLength(); i++)
			{
				Node attr = nodeAttributes.item(i);
				
				if(first)
				{
					first = false;
				}
				else
				{
					System.out.print(", ");
					
					child_attribute+=", ";
				}
				
					
				System.out.print(attr.getNodeName() + ": " + attr.getTextContent());
				
				child_attribute+=attr.getNodeName() + ": " + attr.getTextContent();
				 
			}
			
			System.out.print(")");
			
			
			PdfPTable pdftable_2 = new PdfPTable(1); 
			pdftable_2.addCell(child_attribute);
			 pdftable.addCell(pdftable_2);
			
		}
	}
	
	private static void printToPdf()
	{
	    
	    Document document = new Document();
	   
	    try {
	            PdfWriter.getInstance(document,
	                            new FileOutputStream("H:\\Settings_pdf.pdf"));
	           
	            document.open();
	            Paragraph title1 = new Paragraph("settings_prod_clo",FontFactory.getFont(FontFactory.HELVETICA,18, Font.BOLDITALIC, new CMYKColor(0, 255, 255,17)));   
	            Chapter chapter1 = new Chapter(title1, 1);       
	            chapter1.setNumberDepth(0);   
	            
	            chapter1.add(pdftable);
	            document.add(chapter1);
	            
	            System.out.print("SUCCESS");
	    } catch (DocumentException e) {
	            System.err.println(e.getMessage());
	    } catch (IOException ex) {
	            System.err.println(ex.getMessage());
	    }
	    document.close();
	}
	
	
	
	
}