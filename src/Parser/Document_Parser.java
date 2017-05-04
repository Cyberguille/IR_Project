/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Parser;

import Categories.Category;
import Index.IDocumentToIndex;
import com.sun.org.apache.xerces.internal.impl.xs.opti.NamedNodeMapImpl;
import java.util.ArrayList;

//Estas lineas son añadidas para importar JAXP APIs y ppuedan ser usadas
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
//Son añadidas por excepsiones que  puede lanzar el documento xml 
//cuando es parseado
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
//las siguientes lineas son para leer un xml y edi=entificar errores
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
//Finalmente importamos la definición del DOM por W3C y el DOM excepción
import java.util.AbstractMap.SimpleEntry;
import java.util.Dictionary;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
//
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



/**
 *
 * @author Guille
 */
public class Document_Parser implements Iterable<Entry<String,String>>,IDocumentToIndex
{
    String type;
    TreeMap<String, String> fields;
    static Exception exception;
    String category ="";
    boolean train;
    
    public Document_Parser(String type)
    {
        this.type = type;
        fields = new TreeMap();
    }
    
    public String Get_Type()
    {
        return type;
    }           
       
    public static ArrayList<Document_Parser> Process(String path,final String type)
    {
        File indexDir = new File(path);
        File[] elements = indexDir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                if (pathname.getName().endsWith(type)) {
                    return true;
                }
                return false;
            }
        });
        ArrayList<Document_Parser> result = new ArrayList<Document_Parser>();
        for(File f: elements)
        {
            result.addAll(Parse(f, type));
        }
        return result;
    }
    
    public static ArrayList<Document_Parser> Parse(File f,String type)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try 
        {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(f );
            if(type.equals("xml")) {
                return Xml_Parse(document);
            }    
            else {
                return Sgm_Parse(document);
            }
            
        } 
        catch (SAXException sxe)
        {
        // Error generated during parsing)
            Exception x = sxe;
            if (sxe.getException() != null) {
                x = sxe.getException();
            }
            exception = x;
            return new ArrayList<Document_Parser>();
        } 
        catch (ParserConfigurationException pce) 
        {
        // Parser with specified options can't be built
            exception = pce;
            return new ArrayList<Document_Parser>();         
        } 
        catch (IOException ioe)
        {
        // I/O error
            exception = ioe;
            return new ArrayList<Document_Parser>();
        }
    }
    
    private static ArrayList<Document_Parser> Xml_Parse(Document document)
    {
        NodeList records = document.getElementsByTagName("RECORD");
        ArrayList<Document_Parser> result = new ArrayList<Document_Parser>();
        for (int i = 0; i < records.getLength(); i++)
        {
            Node record = records.item(i);
            NodeList recordChilds = record.getChildNodes();

            Document_Parser xml = new Document_Parser("xml");
            for (int j = 0; j < recordChilds.getLength(); j++) {
                Node recordChild = recordChilds.item(j);

                if ("TITLE".equals(recordChild.getNodeName())) {
                    xml.AddField("TITLE",recordChild.getTextContent());
                } else if ("ABSTRACT".equals(recordChild.getNodeName())) {
                    xml.AddField("ABSTRACT",recordChild.getTextContent());
                }
            }

            result.add(xml);
        }
        return result;
    }
    
    private static ArrayList<Document_Parser> Sgm_Parse(Document document)
    {
         NodeList reuters = document.getElementsByTagName("REUTERS");

            ArrayList<Document_Parser> result = new ArrayList<Document_Parser>();

            for (int i = 0; i < reuters.getLength(); i++) {
                Node reuter = reuters.item(i);
                NodeList reuterChilds = reuter.getChildNodes();
                
                Document_Parser sgm = new Document_Parser("sgm");
                
                NamedNodeMap nd =  reuter.getAttributes();
                Node namedItem = nd.getNamedItem("LEWISSPLIT");
                if("TRAIN".equals(namedItem.getNodeValue()))
                {
                    sgm.setTrain(true);
                }
                
                for (int j = 0; j < reuterChilds.getLength(); j++) 
                {
                    Node reuterChild = reuterChilds.item(j);
                    boolean categorized = false;
                    if ("TEXT".equals(reuterChild.getNodeName())) 
                    {
                        NodeList reuterChildSons = reuterChild.getChildNodes();
                       
                        for (int k = 0; k < reuterChildSons.getLength(); k++) 
                        {
                            Node reuterChildSon = reuterChildSons.item(k);
                            
                            if ("TITLE".equals(reuterChildSon.getNodeName())) 
                            {
                                 sgm.AddField("TITLE",reuterChildSon.getTextContent());
                            } 
                            else if ("BODY".equals(reuterChildSon.getNodeName())) 
                            {
                                sgm.AddField("BODY",reuterChildSon.getTextContent());
                            }
                           
                        }
                    }
                     else if("TOPICS".equals(reuterChild.getNodeName())&& sgm.getTrain()&&!categorized)
                            {
                                 NodeList child = reuterChild.getChildNodes();
                                 if(child.getLength()>0)
                                 {
                                     categorized =true;
                                     sgm.category = child.item(0).getNodeValue();
                                 }
                            }
                            else if("PLACES".equals(reuterChild.getNodeName())&& sgm.getTrain()&&!categorized)
                            {
                                 NodeList child = reuterChild.getChildNodes();
                                 if(child.getLength()>0)
                                 {
                                     categorized =true;
                                     sgm.category = child.item(0).getNodeValue();
                                 }
                            }
                            else if("PEOPLE".equals(reuterChild.getNodeName())&& sgm.getTrain()&&!categorized)
                            {
                                 NodeList child = reuterChild.getChildNodes();
                                 if(child.getLength()>0)
                                 {
                                     categorized =true;
                                     sgm.category = child.item(0).getNodeValue();
                                 }
                            }
                            else if("ORGS".equals(reuterChild.getNodeName())&& sgm.getTrain()&&!categorized)
                            {
                                 NodeList child = reuterChild.getChildNodes();
                                 if(child.getLength()>0)
                                 {
                                     categorized =true;
                                     sgm.category = child.item(0).getNodeValue();
                                 }
                            }
                            else if("EXCHANGES".equals(reuterChild.getNodeName())&& sgm.getTrain()&&!categorized)
                            {
                                 NodeList child = reuterChild.getChildNodes();
                                 if(child.getLength()>0)
                                 {
                                     categorized =true;
                                     sgm.category = child.item(0).getNodeValue();
                                 }
                            }
                }
            }

            return result;
    }

    public void AddField(String string, String textContent)
    {
         fields.put(string,textContent);
    }

    @Override
    public Iterator<Entry<String, String>> iterator() {
        return new java.util.ListIterator<Entry<String, String>>() {

        Entry<String,String> nextElement = fields.firstEntry();;
        String[] list =  fields.keySet().toArray(new String[0]);
        int i=1;
            // Method to test whether more elements are available
            @Override
            public boolean hasNext()
            {
                return  nextElement != null;
            }

            // Method to return the next available object from the linked list
            @Override
            public Entry<String, String> next() 
            {
                Entry<String,String> element = nextElement;
                if(element == null) 
                {
                     throw new java.util.NoSuchElementException();
                }
                nextElement = getNext();
                return element;
            }

            @Override
            public boolean hasPrevious() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Entry<String, String> previous() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public int nextIndex() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public int previousIndex() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            // Method to remove the last element retrieved from the linked list
            // You don’t want to support this operation for the linked list
            // so just throw the exception
            @Override
            public void remove() 
            {
                throw new IllegalStateException();
            }

            private Entry<String, String> getNext() 
            {
               return i<list.length?new SimpleEntry<String, String>(list[i],fields.get(list[i++])):null;
               
            }

            @Override
            public void set(Entry<String, String> e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void add(Entry<String, String> e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }   
    
    public void PrintLine()
    {
        for(Entry<String,String> entry:this)
        {
            System.out.println(entry.getKey());
            System.out.println();
            System.out.println(entry.getValue());   
            System.out.println();
        }
    }

    @Override
    public String[] getFieldsNames() {
        return fields.keySet().toArray(new String[0]);
    }

    @Override
    public String[] getFieldsContents() {
        return fields.values().toArray(new String[0]);
    }
    
    public String getCategory()
    {
        return category;
    }
    
    public void setCategory(String category)
    {
        this.category = category;
    }
    
     public Boolean getTrain()
    {
        return train;
    }
    
    public void setTrain(boolean train)
    {
        this.train = train;
    }

    @Override
    public void addField(String name, String content) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
