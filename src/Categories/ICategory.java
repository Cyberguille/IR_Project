/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Categories;

import Documents.IDocument;
import java.util.ArrayList;

/**
 *
 * @author online
 */
public interface ICategory
{
     long Get_ID();
     String Get_Name();
     ArrayList<IDocument> Get_DocBelong();
}
