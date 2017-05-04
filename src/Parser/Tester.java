/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Parser;

import Algorithms.KFold;
import Algorithms.KNN_Algorithm;
import Algorithms.Rocchio_Algorithm;
import Distances.CosineCentroidDistance;
import Distances.CosineDistance;
import Distances.EuclidianCentroidDistance;
import Distances.EuclidianDistance;
import Distances.HammingCentroidDistance;
import Distances.HammingDistance;
import Documents.Document;
import Documents.IDocument;
import DocumentsWeights.WeightsVectorial;
import Index.IDocumentToIndex;
import Index.IndexerFuzzy;
import Index.IndexerVectorial;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.store.LockObtainFailedException;

/**
 *
 * @author online
 */
public class Tester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws CorruptIndexException, LockObtainFailedException, IOException {
        
//        // Limpiar la carpeta de indexado
//        File folder = new File("C:\\Users\\online\\Desktop\\Coleccionesde prueba\\reuters21578 buenos\\index");
//        File[] listOfFiles = folder.listFiles();
//
//        for (int i = 0; i < listOfFiles.length; i++) {
//            if (listOfFiles[i].isFile()) {
//                listOfFiles[i].delete();
//            }
//        }

        // Parseo de documentos entrenantes
        DocumentExtractor de = new DocumentExtractor("C:\\Users\\online\\Desktop\\Coleccionesde prueba\\Computacion-Sistemas_de_Informacion-colecciones", "xml");
        ArrayList<Entry<IDocumentToIndex, String>> docTrainCat = de.getTrainingSet();
        System.out.println("Documentos entrenantes parseados...");

//        ArrayList<IDocumentToIndex> dti = new ArrayList<IDocumentToIndex>();
//        // Indexado de documentos entrenantes
//        for (int i=0; i < docTrainCat.size() - 1; i++) {
//            dti.add(docTrainCat.get(i).getKey());
//        }
//
//        IndexerVectorial indexer = new IndexerVectorial(dti, new File("C:\\Users\\online\\Desktop\\Coleccionesde prueba\\reuters21578 buenos\\index"));
//        indexer.Index();
//        System.out.println("Documentos entrenantes indexados...");
//
//        // Creacion de documentos con sus categorias
//        ArrayList<Entry<IDocument, String>> docCat = new ArrayList<Entry<IDocument, String>>();
//
//        for (int i = 0; i < docTrainCat.size(); i++) {
//            docCat.add(new SimpleEntry<IDocument, String>(new Document(i, new WeightsVectorial(i, indexer)), docTrainCat.get(i).getValue()));
//        }
//        System.out.println("Documentos entrenantes listos para clasificar...");
//
////         Annadiendo nuevo documento al index para luego categorizarlo
//        indexer.IndexOneDocument(docTrainCat.get(docCat.size() - 1).getKey());
//
//        // Actualizo la lista de los documentos categorizados
//        for (int i = 0; i < docTrainCat.size() - 1; i++) {
//            docCat.add(new SimpleEntry<IDocument, String>(new Document(i, new WeightsVectorial(i, indexer)), docTrainCat.get(i).getValue()));
//        }
//        
//        // Ejecutando KNN
        KNN_Algorithm knn = new KNN_Algorithm(3, new CosineDistance());
////        knn.LearningPhase(docCat);
//
//        // Rocchio
        Rocchio_Algorithm rocchio = new Rocchio_Algorithm(new CosineDistance(), new CosineCentroidDistance());
//        rocchio.LearningPhase(docCat);
////        
//        int x = 0;
////        String[] s = new String[docCat.size()];
////        
////        for (int i = 0; i < docCat.size(); i++) {
////            
//        /*s[i] =*/String s = rocchio.PredictPhase(new Document(docTrainCat.size() - 1, new WeightsVectorial(docTrainCat.size() - 1, indexer)));
////        }
//
        KFold kfold = new KFold(5, rocchio);
        double d = kfold.KFoldMethod(docTrainCat);
        KFold kfold1 = new KFold(5, knn);
        double d1 = kfold1.KFoldMethod(docTrainCat);
//
        int x = 0;
    }
}
