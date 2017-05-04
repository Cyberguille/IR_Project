/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import Documents.IDocument;
import Index.IDocumentToIndex;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;

/**
 *
 * @author online
 */
public interface IClassifier {
    void LearningPhase(ArrayList<Entry<IDocument, String>> trainingDoc);
    String PredictPhase(IDocument document) throws IOException;
}
