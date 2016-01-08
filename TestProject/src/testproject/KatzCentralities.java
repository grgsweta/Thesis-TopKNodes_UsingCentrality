/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testproject;
/**
 *
 * @author useradmin
 */
import Jama.*;
import java.util.ArrayList;

public class KatzCentralities {
    private Matrix mGraph;
    private int mGraphSize;
    
    public KatzCentralities(Matrix m, int size) {
        this.mGraph = m;
        this.mGraphSize = size;
    }
    
    public ArrayList<Double> getEigenValues(Matrix adjacency_matrix) {
        //final int vertices = 8;                // Matrix size
        Matrix m = adjacency_matrix;  
	
//        for(int i = 0; i < vertices; i++) {
//            for(int j = 0; j <= i; j++) {
//		int val = adjacency_matrix[i][j];
//                m.set(i,j,val);
//		m.set(j,i,val);
//	    }
//	}     

//        System.out.println("Initial Radom Matrix is:");
//        m.print(8,4);

        //Get the Eigen value decomposition
	EigenvalueDecomposition eigen = m.eig();

	double [] realPart = eigen.getRealEigenvalues();
	double [] imagPart = eigen.getImagEigenvalues();

        ArrayList<Double> eigenValuesList = new ArrayList<>();
	for(int i = 0; i < realPart.length; i++) {
//	    System.out.println("Eigen Value " + i + " is " +
//			       "[" + realPart[i] + ", " + 
//			       + imagPart[i] + "]");
            eigenValuesList.add(realPart[i]);
        }


	//Get the block diagonal matrix of
	//Eigen values
//	Matrix d = eigen.getD();
//	System.out.println("Diagonal matrix of Eigen values is:");
//        d.print(8,4);

//	Matrix evectors = eigen.getV();
//
//	System.out.println("Matrix of Eigen Vectors is:");
//	evectors.print(8,4);

	//Get transpose of evectors
//	Matrix trans = evectors.transpose();
//
//	//Form trans*evectors (which should be unit matrix)
//	Matrix u = evectors.times(trans);
//
//	System.out.println("Matrix of trans * evectors is :");
//	u.print(8,4);
        return eigenValuesList;
    } 
}
