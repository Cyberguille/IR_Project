/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DocumentsWeights;

import Index.IIndexer;
import java.io.IOException;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author online
 */
public class WeightsVectorial implements IWeightsVector {

    IIndexer indexer;
    int docID;
    double norm = -1;
    String[] docTerms = new String[0];
    TreeMap<String, Double> docWeights = new TreeMap<String, Double>();

    public WeightsVectorial(int docID, IIndexer indexer) throws IOException {
        this.indexer = indexer;
        this.docID = docID;
    }

    @Override
    public double getWeight(String term) {

        try {
            if(docWeights.size() != 0) {
                if(docWeights.containsKey(term))
                {
                    return docWeights.get(term);
                }
            }
            else
            {
                docWeights = indexer.getDocWeights(docID);
                
                if (docWeights.containsKey(term)) {
                    return docWeights.get(term);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(WeightsVectorial.class.getName()).log(Level.SEVERE, null, ex);
        }

        return 0;
    }

    @Override
    public double getNorm() throws IOException {
        
        if(norm != -1) {
            return norm;
        }
        
        double euclidianNorm = 0;
        String[] terms1 = getDocTerms(docID);
        
        for (int i = 0; i < terms1.length; i++) {
            
            euclidianNorm += Math.pow(getWeight(terms1[i]), 2);
        }
        
        norm = Math.sqrt(euclidianNorm);
        return norm;
    }

    @Override
    public int getLength() {
        return indexer.getAllTerms().length;
    }

    @Override
    public String[] getDocTerms(int id) throws IOException {
        
        if(docTerms.length == 0) {
            docTerms = indexer.getDocTerms(id);
        }
        return docTerms;
    }
    
    @Override
    public String[] getAllTerms() {
        return indexer.getAllTerms();
    }
}