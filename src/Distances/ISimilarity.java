/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Distances;

import Documents.IDocument;
import java.io.IOException;

/**
 *
 * @author online
 */
public interface ISimilarity {
    
    double GetSimilarity(IDocument d1, IDocument d2) throws IOException;
}
