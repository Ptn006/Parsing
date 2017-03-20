import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;
/**
 * @description: Frame.java 
 * @createdOn : 02/2017
 * 
 * @author Tulasi Pilli
 * @version 1.0.
 */
public class Frame {

       private SAXTreeBuilder saxTree = null;
       private static String file = "";

       public static void main(String args[]){
              JFrame frame = new JFrame("Settings XMLTreeView: [ WEB-INF/cfg/settings_prod_clo.xml ]");
              frame.setSize(800,800);

              frame.addWindowListener(new WindowAdapter(){
                   public void windowClosing(WindowEvent ev){
                       System.exit(0);
                   }
              });
              file = "WEB-INF/cfg/settings_prod_clo.xml";
              new Frame(frame);
       }

       public Frame(JFrame frame){ 
    	   
    	   java.awt.Container content = frame.getContentPane();
    	   content.setLayout(new BorderLayout());
    	  /* JBook workbook = new JBook();
     		content.add(workbook);
     		frame.setBounds(100, 100, 500, 500 );
        	frame.setVisible(true);*/
        	
             DefaultMutableTreeNode top = new DefaultMutableTreeNode(file);

              saxTree = new SAXTreeBuilder(top); 

              try {             
              SAXParser saxParser = new SAXParser();
              saxParser.setContentHandler(saxTree);
              saxParser.parse(new InputSource(new FileInputStream(file)));
              }catch(Exception ex){
                 top.add(new DefaultMutableTreeNode(ex.getMessage()));
              }
              JTree tree = new JTree(saxTree.getTree()); 
              JScrollPane scrollPane = new JScrollPane(tree);

              content.add("Center",scrollPane);                                           
              frame.setVisible(true);       
        } 
}

class SAXTreeBuilder extends DefaultHandler{

       private DefaultMutableTreeNode currentNode = null;
       private DefaultMutableTreeNode previousNode = null;
       private DefaultMutableTreeNode rootNode = null;

       public SAXTreeBuilder(DefaultMutableTreeNode root){
              rootNode = root;
       }
       public void startDocument(){
              currentNode = rootNode;
       }
       public void endDocument(){
       }
       public void characters(char[] data,int start,int end){
              String str = new String(data,start,end);              
              if (!str.equals("") && Character.isLetter(str.charAt(0)))
                  currentNode.add(new DefaultMutableTreeNode(str));           
       }
       public void startElement(String uri,String qName,String lName,Attributes atts){
              previousNode = currentNode;
              currentNode = new DefaultMutableTreeNode(lName);
              // Add attributes as child nodes //
              attachAttributeList(currentNode,atts);
              previousNode.add(currentNode);              
       }
       public void endElement(String uri,String qName,String lName){
              if (currentNode.getUserObject().equals(lName))
                  currentNode = (DefaultMutableTreeNode)currentNode.getParent();              
       }
       public DefaultMutableTreeNode getTree(){
              return rootNode;
       }

       private void attachAttributeList(DefaultMutableTreeNode node,Attributes atts){
               for (int i=0;i<atts.getLength();i++){
                    String name = atts.getLocalName(i);
                    String value = atts.getValue(name);
                    node.add(new DefaultMutableTreeNode(name + " = " + value));
               }
       }

}