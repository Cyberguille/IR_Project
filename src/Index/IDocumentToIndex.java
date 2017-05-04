/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Index;
/**
 *
 * @author online
 */
public interface IDocumentToIndex {
    
    String[] getFieldsNames();
    String[] getFieldsContents();
    void addField(String name, String content);
}
