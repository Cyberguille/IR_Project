/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Categories;

import Documents.IDocument;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author online
 */
public class Category implements ICategory {

    public Category(long id, String name) {
        Id = id;
        Name = name;
        DocBelong = new ArrayList<IDocument>();
    }
    public long Id;
    public String Name;
    public ArrayList<IDocument> DocBelong;
    public Centroid centroid;

    @Override
    public long Get_ID() {
        return Id;
    }

    @Override
    public String Get_Name() {
        return Name;
    }

    @Override
    public ArrayList<IDocument> Get_DocBelong() {
        return DocBelong;
    }

    public void RecalCentroid() throws IOException {
        
        String[] allTerms = Get_DocBelong().get(0).getWeightsVector().getAllTerms();
//        centroid.Set_Vector(ConvertToDouble(Get_DocBelong().get(0), allTerms));
        
        centroid = new Centroid(allTerms.length);
        double calc;

        for (int i = 0; i < allTerms.length; i++) {
            calc = 0;
            for (int j = 0; j < Get_DocBelong().size(); j++) {
                calc += Get_DocBelong().get(j).getWeightsVector().getWeight(allTerms[i]);
            }
            centroid.vector[i] = calc / allTerms.length;
        }
        int x=0;
    }
//    private double[] ConvertToDouble(IDocument docBelong, String[] allTerms) throws IOException {
//        double[] temp = new double[docBelong.getWeightsVector().getLength()];
//        for (int i = 0; i < temp.length; i++) {
//            temp[i] = Get_DocBelong().get(i).getWeightsVector().getWeight(allTerms[i]);
//        }
//        return temp;
//    }
}
