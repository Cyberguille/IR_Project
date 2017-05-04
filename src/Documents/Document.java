/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Documents;

/**
 *
 * @author online
 */
import DocumentsWeights.IWeightsVector;
import java.io.IOException;

public class Document implements IDocument{

    int id;
    IWeightsVector weightsVector;
    
    public Document(int id, IWeightsVector weightsVector){
        this.id = id;
        this.weightsVector = weightsVector;
    }
    
    @Override
    public int getId() {
        return id;
    }

    @Override
    public IWeightsVector getWeightsVector() {
        return weightsVector;
    }

    @Override
    public String[] getTerms()  throws IOException{
        return weightsVector.getDocTerms(id);
    }
}
