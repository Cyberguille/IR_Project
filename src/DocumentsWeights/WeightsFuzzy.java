/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DocumentsWeights;

import Index.IIndexer;
import java.io.IOException;

/**
 *
 * @author online
 */
public class WeightsFuzzy implements IWeightsVector{

    IIndexer indexer;
    int docID;
    double norm;
    String[] docTerms = new String[0];
    
    public WeightsFuzzy(int docID, IIndexer indexer) throws IOException {
        this.indexer = indexer;
        this.docID = docID;
    }
    
    @Override
    public int getLength() {
        return indexer.getAllTerms().length;
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
    public double getWeight(String term) throws IOException{
        
        String[] allTerms = indexer.getAllTerms();
        
        String[] docTerms = indexer.getDocTerms(docID);
        
        for(int i = 0; i < docTerms.length; i++)
        {
            if(docTerms[i] == null ? docTerms == null : docTerms[i].equals(allTerms[i])) {
                return 1;
            }
        }
        
        return 0;
    }

    @Override
    public String[] getDocTerms(int id) throws IOException {
        
        if(docTerms.length == 0) docTerms = indexer.getDocTerms(id);
        return docTerms;
    }

    @Override
    public String[] getAllTerms() {
        return indexer.getAllTerms();
    }
}
