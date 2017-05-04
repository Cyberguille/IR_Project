/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Categories;

import java.io.IOException;

/**
 *
 * @author online
 */
public class Centroid {
    
    public double[] vector;
    double norm = -1;
    
    public Centroid(int lenght){
        vector = new double[lenght];
    }    
    
    public double getNorm() throws IOException {
        
        if(norm != -1) {
            return norm;
        }
        
        double euclidianNorm = 0;
        
        for (int i = 0; i < vector.length; i++) {
            
            euclidianNorm += Math.pow(vector[i], 2);
        }
        
        norm = Math.sqrt(euclidianNorm);
        return norm;
    }
}
