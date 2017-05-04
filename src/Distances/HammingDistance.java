/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Distances;

import Documents.IDocument;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author online
 */
public class HammingDistance implements ISimilarity {

    @Override
    public double GetSimilarity(IDocument d1, IDocument d2)  throws IOException{
        
        if(d1.getWeightsVector().getLength() != d2.getWeightsVector().getLength()) {
            try {
                throw new Exception("Los vectores de pesos de los documentos deben tener la misma longitud");
            } catch (Exception ex) {
                Logger.getLogger(EuclidianDistance.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        int d = 0;
        String[] terms1 = d1.getWeightsVector().getDocTerms(d1.getId());
        String[] terms2 = d2.getWeightsVector().getDocTerms(d2.getId());
        
        for(int i=0;i<d1.getWeightsVector().getDocTerms(d1.getId()).length;i++)
        {
           if(!contains(terms2, terms1[i])) {
                d++;
            }
        }
        
        for(int i=0;i<d2.getWeightsVector().getDocTerms(d2.getId()).length;i++)
        {
           if(!contains(terms1, terms2[i])) {
                d++;
            }
        }
        
        return d;
    }
    
    private boolean contains(String[] terms, String term)
    {
        for(int i = 0; i < terms.length;i++)
        {
            if(terms[i] == null ? term == null : terms[i].equals(term)) {
                return true;
            }
        }
        
        return false;
    }
}
