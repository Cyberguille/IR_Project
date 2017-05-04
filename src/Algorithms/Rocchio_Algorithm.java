/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import Categories.Category;
import Distances.ICentroidDistance;
import Distances.ISimilarity;
import Documents.IDocument;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author online
 */
public class Rocchio_Algorithm implements IClassifier {

    public TreeMap<String, Category> categories;
    public int Id;
    public TreeMap<String, Category> cat;
    public ISimilarity similarity;
    public ICentroidDistance dist;

    public Rocchio_Algorithm(ISimilarity similarity, ICentroidDistance dist) {
        Id = 0;
        this.similarity = similarity;
        this.dist = dist;
    }

    @Override
    public void LearningPhase(ArrayList<Entry<IDocument, String>> trainingDoc) {
        cat = new TreeMap<String, Category>();
        for (Entry<IDocument, String> temp : trainingDoc) {
            if (!cat.containsKey(temp.getValue())) {
                cat.put(temp.getValue(), new Category(Id++, temp.getValue()));
            }
            cat.get(temp.getValue()).DocBelong.add(temp.getKey());
        }
        for (Category category : cat.values()) {
            try {
                category.RecalCentroid();
            } catch (IOException ex) {
                Logger.getLogger(Rocchio_Algorithm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public String PredictPhase(IDocument document)  throws IOException{
        double distance = Double.MAX_VALUE;
        double auxiliar;
        Category temp = null;
        for (Category category : cat.values()) {

            auxiliar = dist.GetSimilarity(document, category.centroid);
            if (auxiliar < distance) {
                distance = auxiliar;
                temp = category;
            }
        }
        if(temp == null)
        {
            int x=0;
        }
        temp.DocBelong.add(document);
        temp.RecalCentroid();
        return temp.Name;
    }
}
