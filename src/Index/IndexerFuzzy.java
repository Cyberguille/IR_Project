/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Index;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

/**
 *
 * @author online
 */
public class IndexerFuzzy implements IIndexer {

    TreeMap<Integer, String[]> docsTerms;
    ArrayList<IDocumentToIndex> documents;
    File indexDir;
    String[] allTerms;

    public IndexerFuzzy(ArrayList<IDocumentToIndex> documents, File indexDir) {

        this.indexDir = indexDir;
        this.documents = documents;
    }

    @Override
    public void Index() throws CorruptIndexException, LockObtainFailedException, IOException {

        IndexWriter writer = new IndexWriter(FSDirectory.open(indexDir), new StandardAnalyzer(Version.LUCENE_30), IndexWriter.MaxFieldLength.UNLIMITED);
        
        for (IDocumentToIndex d : documents) {
            //if (d.getFieldsNames().length > 0) {
                Document doc = new Document();
                String content = "";
                
                for (int i = 0; i < d.getFieldsNames().length; i++) {
                    content += " " + d.getFieldsContents()[i];
                }
                
                doc.add(new Field("content",
                        content,
                        Field.Store.YES,
                        Field.Index.ANALYZED,
                        Field.TermVector.YES));
                
                writer.addDocument(doc);
            //}
        }
        
        writer.close();

        // Almaceno todos los terminos indexados en el campo 'allTerms'
        getAllIndexedTermsFromIndex();
    }

    @Override
    public String[] getAllTerms() {

        return allTerms;
    }

    @Override
    public TreeMap<String, Double> getDocWeights(int docID) throws IOException {

        IndexSearcher is = new IndexSearcher(FSDirectory.open(indexDir));
        IndexReader ir = is.getIndexReader();
        
        ArrayList<String> resultTerms = new ArrayList<String>(); //

        TermFreqVector docTerms = ir.getTermFreqVector(docID, "content");
        String[] terms = docTerms.getTerms(); //obtener el array de términos del documento
        int[] frequencies = docTerms.getTermFrequencies(); //obtener el array de frecuencias
        
        TreeMap<String, Double> result = new TreeMap<String, Double>();
        
        for (int i = 0; i < terms.length; i++) {
            result.put(terms[i], (double) frequencies[i]);
        }
        
        double maxfreq = MaxFreq(result);
        double nummax = ir.maxDoc();
        
        for (int i = 0; i < terms.length; i++) {
            double tf = 0.5 + (0.5 * result.get(terms[i])) / maxfreq;
            double idf = Math.log(nummax / ir.docFreq(new Term(docTerms.getField(), terms[i])));
            result.put(terms[i], tf * idf);
        }
        
        ir.close();
        is.close();
        
        return result;
    }

    private void readDocsTerms(int id) throws CorruptIndexException, IOException {

        IndexSearcher is = new IndexSearcher(FSDirectory.open(indexDir));
        IndexReader ir = is.getIndexReader();

        ArrayList<String> resultTerms = new ArrayList<String>();

        TermFreqVector docTerms = ir.getTermFreqVector(id, "contents");
        String[] terms = docTerms.getTerms(); //obtener el array de términos del documento
        docsTerms.put(id, terms);
        
        ir.close();
        is.close();
    }

    private double MaxFreq(TreeMap<String, Double> terms) {
        double max = 0;
        for (double value : terms.values()) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
    
    private void getAllIndexedTermsFromIndex() throws IOException {
        
        IndexSearcher is = new IndexSearcher(FSDirectory.open(indexDir));
        
        IndexReader ir = is.getIndexReader();
        TermEnum tEnum = ir.terms(); //obtener todos los términos de la coleccción
        
        ArrayList<String> terms = new ArrayList<String>();
        
        while (tEnum.next()) {
            
            Term t = tEnum.term();
            terms.add(t.text());            
        }
        
        allTerms = terms.toArray(new String[terms.size()]);
        
        ir.close();
        is.close();
    }

    @Override
    public String[] getDocTerms(int id) throws IOException{
        
        IndexSearcher is = new IndexSearcher(FSDirectory.open(indexDir));
        IndexReader ir = is.getIndexReader();

        TermFreqVector docTerms = ir.getTermFreqVector(id, "content");
        String[] terms = docTerms.getTerms(); //obtener el array de términos del documento
        
        ir.close();
        is.close();
        
        return terms;
    }
    
    @Override
    public void IndexOneDocument(IDocumentToIndex newDoc) throws CorruptIndexException, LockObtainFailedException, IOException {
        
        IndexWriter writer = new IndexWriter(FSDirectory.open(indexDir), new StandardAnalyzer(Version.LUCENE_30), IndexWriter.MaxFieldLength.UNLIMITED);
        
        Document doc = new Document();
        String content = "";
                
        for (int i = 0; i < newDoc.getFieldsNames().length; i++) {
            content += " " + newDoc.getFieldsContents()[i];
        }

        doc.add(new Field("content",
                content,
                Field.Store.YES,
                Field.Index.ANALYZED,
                Field.TermVector.YES));

        writer.addDocument(doc);
        writer.close();
        
        // Almaceno todos los terminos indexados en el campo 'allTerms'
        getAllIndexedTermsFromIndex();
    }
}
