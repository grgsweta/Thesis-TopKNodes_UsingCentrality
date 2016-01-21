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
//import java.text.NumberFormat;
import java.util.ArrayList;
//import static testproject.TestProject.adjacencyMap;
import static testproject.TestProject.adjacencyMatrix;
//import static testproject.TestProject.kcMap;
//import static testproject.TestProject.kcMatrix;

public class KatzCentralities {
    private Matrix KatzMatrix;
    private final int KatzMatrixSize;
    
    public KatzCentralities(int size) {
        KatzMatrix = new Matrix(size, 1);
        KatzMatrixSize = size;
    }
    
    public Matrix getKatzMatrix() {
        return KatzMatrix;
    }
    
    public double getNodeKatz(int row, int col) {
        
        //row = KatzMatrix.getRowDimension();
        //col = KatzMatrix.getColumnDimension();
        //KatzMatrix.print(KatzMatrixSize, KatzMatrixSize);
        //System.out.println(KatzMatrix.get(row, col));
        return KatzMatrix.get(row, col);
    }
    
    public Matrix getTranspose() {
        return KatzMatrix.transpose();
    }
    
    public void print() {
        //NumberFormat n = NumberFormat.getIntegerInstance();
        KatzMatrix.print(KatzMatrixSize, KatzMatrixSize);
        //KatzMatrix.print(n,KatzMatrixSize);
        
    }
    
    public void compute(double alpha, double beta) {
        //C_Katz = beta * (I - alpha*A_transpose)^(-1)*1
        Matrix identityMatrix = Matrix.identity(KatzMatrixSize, KatzMatrixSize);
        Matrix onesMatrix = new Matrix(KatzMatrixSize, 1,1);
        
        //System.out.println("The value of Alpha is: " + alpha);
        
        Matrix intermediateMatrix = adjacencyMatrix.transpose();                   //A_transpose
        intermediateMatrix.timesEquals(alpha);                                     //- alpha*A_transpose
        intermediateMatrix = identityMatrix.minusEquals(intermediateMatrix);       //(I - alpha*A_transpose)
        //intermediateMatrix.print(KatzMatrixSize, KatzMatrixSize);
        intermediateMatrix = intermediateMatrix.inverse();                         //(I - alpha*A_transpose)^(-1)
               
        //intermediateMatrix.print(KatzMatrixSize, KatzMatrixSize);
        
        intermediateMatrix.timesEquals(beta);                                      //beta * (I - alpha*A_transpose)^(-1)
        //intermediateMatrix.print(KatzMatrixSize, KatzMatrixSize);
        //onesMatrix.print(KatzMatrixSize, 1);
        
        KatzMatrix = intermediateMatrix.times(onesMatrix);                         //beta * (I - alpha*A_transpose)^(-1)*1
        //KatzMatrix.print(KatzMatrixSize, KatzMatrixSize);
       
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
	//double [] imagPart = eigen.getImagEigenvalues();

        ArrayList<Double> eigenValuesList = new ArrayList<>();
	for(int i = 0; i < realPart.length; i++) {
            //System.out.println("Eigen Value " + i + " is " + "[" + realPart[i] + ", " + imagPart[i] + "]");
            eigenValuesList.add(realPart[i]);
        }

        return eigenValuesList;
    } 
}
