/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgtry;
import java.io.File;
import java.util.Scanner;
/**
 *
 * @author Sweta
 */
public class Try {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        // TODO code application logic here
        // /Users/psangat/NetBeansProjects/DeptApp/build.xml
        Scanner in = new Scanner(System.in);
        
        System.out.print("Enter the file path: ");
        String filePath = in.next();
        File file = new File(filePath);
        if (file.exists() && file.isDirectory())
        {
            // do some processing here
            System.out.println("File exists.");
        }
        else
        {
            System.err.println("File does not exists.");
        }
    }
    
    
}
