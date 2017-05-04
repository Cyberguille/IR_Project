/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import Distances.ISimilarity;
import Documents.Document;
import Documents.IDocument;
import DocumentsWeights.WeightsVectorial;
import Index.IDocumentToIndex;
import Index.IIndexer;
import Index.IndexerFuzzy;
import Index.IndexerVectorial;
import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map.Entry;

/**
 *
 * @author online
 */
public class KFold {

    public int K;
    public IClassifier Algorithm;

    public KFold(int k, IClassifier algorithm) {
        K = k;
        Algorithm = algorithm;
    }

    public double KFoldMethod(ArrayList<Entry<IDocumentToIndex, String>> documents) throws IOException {

        ArrayList<Entry<IDocumentToIndex, String>> trainSet = new ArrayList<Entry<IDocumentToIndex, String>>();
        ArrayList<Entry<IDocumentToIndex, String>> testSet = new ArrayList<Entry<IDocumentToIndex, String>>();
        ArrayList<String> categories = new ArrayList<String>();

        int subSetlength = documents.size() / K;
        int[] errors = new int[K];

        for (int i = 0; i < K; i++) {
            for (int j = 0; j < documents.size(); j++) {
                int ini = i * subSetlength;
                int fin = ini + subSetlength - 1;
                if (j >= ini && j < fin) {
                    testSet.add(documents.get(j));
                } else {
                    trainSet.add(documents.get(j));
                    categories.add(documents.get(j).getValue());
                }
            }
            
            // Extraccion de IdocumentToIndex
            ArrayList<IDocumentToIndex> dti = new ArrayList<IDocumentToIndex>();
            for (Entry<IDocumentToIndex, String> entry : documents) {
                dti.add(entry.getKey());
            }
            
            //Limpiar Directorio de indexado
            IndexDirectoryClean("C:\\Users\\online\\Desktop\\Coleccionesde prueba\\reuters21578 buenos\\index");
            
            // Indexado de documentos entrenantes
            IIndexer indexer = new IndexerVectorial(dti, new File("C:\\Users\\online\\Desktop\\Coleccionesde prueba\\reuters21578 buenos\\index"));
            indexer.Index();
            
            // Creacion de IDocument
            ArrayList<Entry<IDocument, String>> testSetIDocument = new ArrayList<Entry<IDocument, String>>();
            
            for (int k = 0; k < testSet.size(); k++) {
                testSetIDocument.add(new AbstractMap.SimpleEntry<IDocument, String>(new Document(k, new WeightsVectorial(k, indexer)), testSet.get(k).getValue()));
            }
            
            // Fase de entrenamiento
            Algorithm.LearningPhase(testSetIDocument);
            
            for (Entry<IDocument, String> entry : testSetIDocument) {
                if (categories.contains(entry.getValue())) {
                    if (!Algorithm.PredictPhase(entry.getKey()).equals(entry.getValue())) {
                        errors[i]++;
                    }
                }
            }
            trainSet.clear();
            testSet.clear();
        }

        int errores = 0;
        for (int i = 0; i < K; i++) {
            errores += errors[i];
        }
        
        return 1 - (double)errores / (double)documents.size();
    }
    
    private void IndexDirectoryClean(String path)
    {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                listOfFiles[i].delete();
            }
        }
    }
}
