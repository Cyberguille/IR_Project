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
public class EuclidianCentroidDistance implements ICentroidDistance{

    @Override
    public double GetSimilarity(IDocument d1, Centroid centroid) throws IOException {
        
        if(d1.getWeightsVector().getLength() != centroid.vector.length) {
            try {
                throw new Exception("Los vectores de pesos de los documentos deben tener la misma longitud");
            } catch (Exception ex) {
                Logger.getLogger(EuclidianDistance.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        double distance = 0;
        double w1, w2;
        String[] allTerms = d1.getWeightsVector().getAllTerms();

        for (int i = 0; i < allTerms.length; i++) {
            
            w1 = d1.getWeightsVector().getWeight(allTerms[i]);
            w2 = centroid.vector[i];
            distance += Math.pow(Math.abs(w1 - w2), 2);
        }
        
        distance = Math.sqrt(distance);

        return distance;
    }
    
}
