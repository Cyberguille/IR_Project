/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Documents;

import DocumentsWeights.IWeightsVector;
import java.io.IOException;

/**
 *Representa un documento de la colección
 * @author online
 */
public interface IDocument
{
    int getId();
    IWeightsVector getWeightsVector();
    String[] getTerms() throws IOException;
}
