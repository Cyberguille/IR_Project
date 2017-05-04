/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Distances;

import Categories.Centroid;
import Documents.IDocument;
import java.io.IOException;

/**
 *
 * @author online
 */
public interface ICentroidDistance {
    
    double GetSimilarity(IDocument d1, Centroid centroid) throws IOException;
}
