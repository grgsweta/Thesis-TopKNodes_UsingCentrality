/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testproject;

/**
 *
 * @author Sweta
 */
public class AdjacencyMatrixTranspose {
    
    public int[][] adjacency_matrix_transpose;
 
    public AdjacencyMatrixTranspose(int[][] adjacency_matrix, int vertices) {
        int column = vertices;
        int row = vertices;
        adjacency_matrix = new int[vertices + 1][vertices + 1];
        int c, d; 
       
        int adjacency_matrix_transpose[][] = new int[column][row];

        for ( c = 0 ; c < row ; c++ ) {
           for ( d = 0 ; d < column ; d++ )               
              adjacency_matrix_transpose[d][c] = adjacency_matrix[c][d];
        }

        System.out.println("Transpose of entered matrix:-");

        for ( c = 0 ; c < column ; c++ ) {
           for ( d = 0 ; d < row ; d++ )
                 System.out.print(adjacency_matrix_transpose[c][d]+"\t");

           System.out.print("\n");
        }
        
    }
}
