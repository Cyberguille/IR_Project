/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import Distances.ISimilarity;
import java.util.ArrayList;
import java.util.Map.Entry;
import Documents.IDocument;
import java.io.IOException;
import java.util.TreeMap;

/**
 *
 * @author Vassili
 */
public class KNN_Algorithm implements IClassifier {

    public KNN_Algorithm(int k, ISimilarity similarity) {
        K = k;
        this.similarity = similarity;
        neighDocuments = new ArrayList<Entry<IDocument, String>>();
    }
    public int K;
    public ArrayList<Entry<IDocument, String>> trainingDoc;
    public ArrayList<Entry<IDocument, String>> neighDocuments;
    ISimilarity similarity;

    @Override
    public void LearningPhase(ArrayList<Entry<IDocument, String>> trainingDoc) {
        this.trainingDoc = trainingDoc;
    }
    
    private String SearchClass(TreeMap<String, Integer> classRep) {
        String clas = "";
        for (String x : classRep.keySet()) {
            if ("".equals(clas)) {
                clas = x;
            }
            else if (classRep.get(clas) < classRep.get(x)) {
                clas = x;
            }
        }
        return clas;
    }
    
    @Override
    public String PredictPhase(IDocument document) throws IOException{
        TreeMap<Double, Entry<IDocument, String>> trainDocSorted = new TreeMap<Double, Entry<IDocument, String>>();
        
        
        for (Entry<IDocument, String> entry : trainingDoc) {
            trainDocSorted.put(similarity.GetSimilarity(entry.getKey(), document), entry);
        }
        
        int count = 0;
        for (double d : trainDocSorted.keySet()) {
            if(count < K){ 
                neighDocuments.add(trainDocSorted.get(d));
                count++;
            }
            else break;
        }
        
        TreeMap<String, Integer> classRep = new TreeMap<String, Integer>();
        for (Entry<IDocument, String> neighDoc : neighDocuments) {
            if (classRep.containsKey(neighDoc.getValue())) {
                int x = classRep.get(neighDoc.getValue());
                x++;
                classRep.remove(neighDoc.getValue());
                classRep.put(neighDoc.getValue(), x);
            } 
            else {
                classRep.put(neighDoc.getValue(), 1);
            }
        }
        return SearchClass(classRep);
    }
}

