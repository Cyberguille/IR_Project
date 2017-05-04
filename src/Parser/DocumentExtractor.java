/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Parser;


import Categories.Category;
import Index.DocumentToIndex;
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
 * @author online
 */
public final class DocumentExtractor {
    
    Exception exception;
    ArrayList<Entry<IDocumentToIndex,String >> trainingSet;
    ArrayList<Entry<IDocumentToIndex,String >> testSet;
    public DocumentExtractor(String path,final String type)
    {
        trainingSet= new ArrayList<Entry<IDocumentToIndex, String>>();
        testSet = new ArrayList<Entry<IDocumentToIndex, String>>();
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
        for(File f: elements)
        {
            Parse(f, type);
        }
    }
    public  void Parse(File f,String type)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try 
        {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(f );
            if(type.equals("xml")) {
                Xml_Parse(document);
            }    
            else {
                Sgm_Parse(document);
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
        } 
        catch (ParserConfigurationException pce) 
        {
        // Parser with specified options can't be built
            exception = pce;         
        } 
        catch (IOException ioe)
        {
        // I/O error
            exception = ioe;
        }
    }
    
    private void Xml_Parse(Document document)
    {
        NodeList records = document.getElementsByTagName("RECORD");
        for (int i = 0; i < records.getLength(); i++)
        {
            Node record = records.item(i);
            NodeList recordChilds = record.getChildNodes();

            IDocumentToIndex  xml = new DocumentToIndex();
            String category = ""; 
            for (int j = 0; j < recordChilds.getLength(); j++) {
                Node recordChild = recordChilds.item(j);

                if ("TITLE".equals(recordChild.getNodeName())) {
                    xml.addField("TITLE",recordChild.getTextContent());
                } else if ("ABSTRACT".equals(recordChild.getNodeName())) {
                    xml.addField("ABSTRACT",recordChild.getTextContent());
                }
                else if("MAJORSUBJ".equals(recordChild.getNodeName()))
                {
                    NodeList Child = recordChild.getChildNodes();
                    Node record1 = Child.item(0);
                    category = record1.getTextContent();
                }
            }

            trainingSet.add(new SimpleEntry<IDocumentToIndex, String>(xml,category));
        }
    }
    
    private void  Sgm_Parse(Document document)
    {
         NodeList reuters = document.getElementsByTagName("REUTERS");

            for (int i = 0; i < reuters.getLength(); i++) {
                Node reuter = reuters.item(i);
                NodeList reuterChilds = reuter.getChildNodes();
                boolean error = false;
                IDocumentToIndex sgm =  new DocumentToIndex();
                
                NamedNodeMap nd =  reuter.getAttributes();
                Node namedItem = nd.getNamedItem("LEWISSPLIT");
                boolean train = false;
                if("TRAIN".equals(namedItem.getNodeValue()))
                {
                    train = true;
                }
                String category="";
                boolean categorized = false;
                for (int j = 0; j < reuterChilds.getLength(); j++) 
                {
                    Node reuterChild = reuterChilds.item(j);
                    
                    
                    if ("TEXT".equals(reuterChild.getNodeName())) 
                    {
                        NamedNodeMap nt =  reuterChild.getAttributes();
                        if(nt!= null)
                        {
                            Node namedt = nt.getNamedItem("TYPE");
                            if(namedt != null&&"UNPROC".equals(namedt.getNodeValue()))
                            {
                                error = true;
                                break;
                            }
                                
                                
                        }
                        NodeList reuterChildSons = reuterChild.getChildNodes();
                       
                        for (int k = 0; k < reuterChildSons.getLength(); k++) 
                        {
                            Node reuterChildSon = reuterChildSons.item(k);
                            
                            if ("TITLE".equals(reuterChildSon.getNodeName())) 
                            {
                                 sgm.addField("TITLE",reuterChildSon.getTextContent());
                            } 
                            else if ("BODY".equals(reuterChildSon.getNodeName())) 
                            {
                                sgm.addField("BODY",reuterChildSon.getTextContent());
                            }
                           
                        }
                    }
                            
                    // String category = getCategory(reuterChild); 
                            
                     else if("TOPICS".equals(reuterChild.getNodeName())&& train &&!categorized)
                            {
                                 NodeList child = reuterChild.getChildNodes();
                                 if(child.getLength()>0)
                                 {
                                     categorized =true;
                                     category = child.item(0).getTextContent();
                                 }
                            }
                            else if("PLACES".equals(reuterChild.getNodeName())&& train&&!categorized)
                            {
                                 NodeList child = reuterChild.getChildNodes();
                                 if(child.getLength()>0)
                                 {
                                     categorized =true;
                                     category = child.item(0).getTextContent();
                                 }
                            }
                            else if("PEOPLE".equals(reuterChild.getNodeName())&& train &&!categorized)
                            {
                                 NodeList child = reuterChild.getChildNodes();
                                 if(child.getLength()>0)
                                 {
                                     categorized =true;
                                     category = child.item(0).getTextContent();
                                 }
                            }
                            else if("ORGS".equals(reuterChild.getNodeName())&& train &&!categorized)
                            {
                                 NodeList child = reuterChild.getChildNodes();
                                 if(child.getLength()>0)
                                 {
                                     categorized =true;
                                     category = child.item(0).getTextContent();
                                 }
                            }
                            else if("EXCHANGES".equals(reuterChild.getNodeName())&& train &&!categorized)
                            {
                                 NodeList child = reuterChild.getChildNodes();
                                 if(child.getLength()>0)
                                 {
                                     categorized =true;
                                     category = child.item(0).getTextContent();
                                 }
                            }
                }
                if(error)
                    continue;
                else if(train) {
                     trainingSet.add(new SimpleEntry<IDocumentToIndex, String>(sgm,category) );
                }
                else {
                    testSet.add(new SimpleEntry<IDocumentToIndex, String>(sgm,category));
                }                 
                
            }

    }
    
    public ArrayList<Entry<IDocumentToIndex,String>> getTrainingSet()
    {
        return trainingSet;
    }
    public ArrayList<Entry<IDocumentToIndex,String>> getTestgSet()
    {
        return testSet ;
    }
    
}
