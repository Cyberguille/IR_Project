/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Index;

import java.util.ArrayList;

/**
 *
 * @author online
 */
public class DocumentToIndex implements IDocumentToIndex{

    ArrayList<String> fields;
    ArrayList<String> contents;
    
    public DocumentToIndex()
    {
        fields = new ArrayList<String>();
        contents = new ArrayList<String>();
    }
    
    @Override
    public String[] getFieldsNames() {
        return fields.toArray(new String[0]);
    }

    @Override
    public String[] getFieldsContents() {
        return contents.toArray(new String[0]);
    }

    @Override
    public void addField(String name, String content) {
        fields.add(name);
        contents.add(content);
    }
}
