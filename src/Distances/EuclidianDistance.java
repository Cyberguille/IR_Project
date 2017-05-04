/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Distances;

/**
 *
 * @author online
 */
import Documents.IDocument;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EuclidianDistance implements ISimilarity{
    
    @Override
    public double GetSimilarity(IDocument d1, IDocument d2) throws IOException{
        
        if(d1.getWeightsVector().getLength() != d2.getWeightsVector().getLength()) {
            try {
                throw new Exception("Los vectores de pesos de los documentos deben tener la misma longitud");
            } catch (Exception ex) {
                Logger.getLogger(EuclidianDistance.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        double distance = 0;
        double w1, w2;
        String[] terms1 = d1.getWeightsVector().getDocTerms(d1.getId());
        String[] terms2 = d2.getWeightsVector().getDocTerms(d2.getId());

        for (int i = 0; i < terms1.length; i++)
        {
            w1 = d1.getWeightsVector().getWeight(terms1[i]);
            w2 = d2.getWeightsVector().getWeight(terms1[i]);
            distance += Math.pow(w1 - w2, 2);
        }

        distance = Math.sqrt(distance);

        return distance;
    }

}
