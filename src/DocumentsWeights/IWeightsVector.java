/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DocumentsWeights;

import java.io.IOException;

/**
 *
 * @author online
 */
public interface IWeightsVector {
    
    double getWeight(String term) throws IOException;
    int getLength();
    double getNorm() throws IOException ;
    String [] getDocTerms(int id) throws IOException;
    String [] getAllTerms();
}
