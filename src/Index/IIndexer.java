/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Index;

import Documents.IDocument;
import java.io.IOException;
import java.util.TreeMap;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.store.LockObtainFailedException;

/**
 *
 * @author online
 */
//import java.util.ArrayList;
public interface IIndexer {

    void Index() throws CorruptIndexException, LockObtainFailedException, IOException;

    String[] getAllTerms();

    TreeMap<String, Double> getDocWeights(int docID) throws IOException;

    String[] getDocTerms(int id) throws IOException;
    
    void IndexOneDocument(IDocumentToIndex doc) throws CorruptIndexException, LockObtainFailedException, IOException;
}
