/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Distances;

import Categories.Centroid;
import Documents.IDocument;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author online
 */
public class HammingCentroidDistance implements ICentroidDistance{

    @Override
    public double GetSimilarity(IDocument d1, Centroid centroid) throws IOException {
        
        if(d1.getWeightsVector().getLength() != centroid.vector.length) {
            try {
                throw new Exception("Los vectores de pesos de los documentos deben tener la misma longitud");
            } catch (Exception ex) {
                Logger.getLogger(EuclidianDistance.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        int d = 0;
        String[] allTerms = d1.getWeightsVector().getAllTerms();

        for (int i = 0; i < allTerms.length; i++) {
            
            if(d1.getWeightsVector().getWeight(allTerms[i]) != centroid.vector[i]) {
                d++;
            }
        }
        
        return d;
    }
}
