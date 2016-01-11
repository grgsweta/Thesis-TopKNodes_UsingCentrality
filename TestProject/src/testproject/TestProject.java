/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.HashMap;
import Jama.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
//import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Sweta
 */
public class TestProject {

    /**
     * @param args the command line arguments
     */
    //List of global variables
    //public static String sourceFile = "input/0.edges";
    public static String sourceFile = "input/soc-twitter-follows-mun.edges";
    
    public static HashMap<Integer, ArrayList<Integer>> original_adjacencyMap = new HashMap<>();  //HashMap with original Node id and its neighbours list
    public static HashMap<Integer, ArrayList<Integer>> adjacencyMap = new HashMap<>();           //HashMap with NEW Node id  and its neighbours' new ids list
    public static HashMap<Integer, Integer> nodesMapping = new HashMap<>();   //original node ID -> serial numbers (starting from 0) assigned to each node id
    public static Matrix adjacencyMatrix;   
    public static int nodesCount;
    public static KatzCentralities kcMatrix;
    public static HashMap<Integer, Double> kcMap = new HashMap<>();  //kcMap -> contains nodes and their corresponding Katz Centralities

//    public static HashMap<Integer, ArrayList<Integer>> nbrKatzCentralitiesMap = new HashMap<>();
//    public static HashMap<Integer, HashMap<Integer, Double>> nbrKatzCentralitiesMap = new HashMap<>();
   
    public static void main(String args[]) {
//        int v, e, count = 1, to = 0, from = 0;
//        Scanner locationReader = new Scanner(System.in);
//        System.out.println("Graph File Location: ");
//        String graphFile = locationReader.next();
//        graphFile = graphFile.replaceAll("\\\\", "/");
//        //GraphRead inputGraph; 
//        GraphRead.getGraphData(graphFile);
//        Map<Integer, ArrayList<Integer>> adjacencyMap = new HashMap<>();
        BufferedReader sourceFileRead = null;

//        Scanner in = new Scanner(System.in);
//        System.out.println("Enter the file path: ");
//        String filePath = in.next();
//        File sourceFile = new File(filePath);
//        System.out.println("");
//        if(sourceFile.exists() ){//&& sourceFile.isDirectory()) {
//            Map<Integer, ArrayList<Integer>> adjacencyMap = new HashMap<>();
//            BufferedReader sourceFileRead = null;
//            System.out.println("1");
        try {
            sourceFileRead = new BufferedReader(new FileReader(sourceFile));
            //StringBuffer document = new StringBuffer();

            String currentLine = null;

            while ((currentLine = sourceFileRead.readLine()) != null) {
                if (Character.isDigit(currentLine.charAt(0))) {        //distinguishes each document by the starting point ".i"
//                        SrtingTokenizer token = new StringTokenizer(currentLine);
                    String[] element = currentLine.split("\\s+");
                    int element0 = parseInt(element[0]);
                    int element1 = parseInt(element[1]);
                    if (original_adjacencyMap.containsKey(element0)) {
                        //System.out.println("1case: " + element0 +"->"+element1);
                        original_adjacencyMap.get(element0).add(element1);
                    } else {
                        
                        ArrayList<Integer> valueList = new ArrayList<>();
                        valueList.add(element1);
                        //System.out.println("2case: " + element0 +"->"+valueList);
                        original_adjacencyMap.put(element0, valueList);
                    } 
                                     
                    if (!original_adjacencyMap.containsKey(element1)) {
                       ArrayList<Integer> valueList = new ArrayList<>();
                       //System.out.println("3case: " + element1 +"->"+valueList);
                       original_adjacencyMap.put(element1, valueList);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int nodesCount = original_adjacencyMap.size();
        adjacencyMatrix = new Matrix(nodesCount, nodesCount);

//          try 
//            {
                /*Using Jama Libraries*/
        
//        /*original_adjacencyMap -> Data Display*/
//        for (Entry<Integer, ArrayList<Integer>> e : original_adjacencyMap.entrySet()) {
//            int node = e.getKey();//getting the new id for the node from nodesMapping --key
//            ArrayList<Integer> neighbourList = e.getValue();
//            System.out.println(node +" "+neighbourList);
//        }
        
        /*Original Node ID mapping to the new serial ids*/
        Set<Integer> keyset = new HashSet<>(original_adjacencyMap.keySet());
        //System.out.println("Key values are \n" + keyset);
        int new_nodeID = 0;
        for (Integer nodeID : keyset) {
            nodesMapping.put(nodeID, new_nodeID);
            //System.out.println(new_nodeID + "->" + nodeID);
            new_nodeID++;
        }
        //System.out.println();

        for (Entry<Integer, ArrayList<Integer>> e : original_adjacencyMap.entrySet()) {
            //System.out.println("here");
            int node = nodesMapping.get(e.getKey()); //getting the new id for the node from nodesMapping --key
            ArrayList<Integer> neighbourList = e.getValue();
            ArrayList<Integer> neighbourListUpdated = new ArrayList<>();
            //System.out.println(node +" "+neighbourList);
            int neighbour;
            for (Integer n : neighbourList) {
                neighbour = nodesMapping.get(n); //getting the new id for the node from nodesMapping --values
                //System.out.println(node + "-> "+ neighbour);
                neighbourListUpdated.add(neighbour);
            }

            adjacencyMap.put(node, neighbourListUpdated);
        }

        for (Entry<Integer, ArrayList<Integer>> e : adjacencyMap.entrySet()) {
            //System.out.println("here");
            int fromNode = e.getKey();
            ArrayList<Integer> newList = e.getValue();
            for (Integer toNodes : newList) {
                //System.out.println(fromNode + "->" + toNodes);
                adjacencyMatrix.set(fromNode, toNodes, 1.0);
            }
        }
        
        NumberFormat n = NumberFormat.getIntegerInstance();
        //adjacencyMatrix.print(nodesCount, nodesCount);
        adjacencyMatrix.print(n,nodesCount);

        File file = new File("input/file.txt");
        PrintWriter printWriter = null;

        try {
            printWriter = new PrintWriter(file);
            adjacencyMatrix.print(printWriter, nodesCount, nodesCount);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }

//                Set keyset = adjacencyMap.keySet();
//                System.out.println("Key values are \n" + keyset);
//                int nodesCount = adjacencyMap.size();
//                GraphAdjacencyMatrix graph = new GraphAdjacencyMatrix(nodesCount);
//                for(Entry<Integer, ArrayList<Integer>> e : adjacencyMap.entrySet()){
//                    //System.out.println("here");
//                    int fromNode = e.getKey();
//                    ArrayList<Integer> newList = e.getValue();
//                    for (Integer toNodes : newList) {
//                        graph.makeEdge(toNodes, fromNode, 1);
//                    } 
//                    
//                    newList.forEach(System.out::println);
//                }
//
//                System.out.println("The adjacency matrix for the given graph is: ");
//                System.out.print("  ");
//                for (int i = 1; i <= nodesCount; i++)
//                    System.out.print(i + " ");
//                System.out.println();
//
//                for (int i = 1; i <= nodesCount; i++) 
//                {
//                    System.out.print(i + " ");
//                    for (int j = 1; j <= nodesCount; j++) 
//                        System.out.print(graph.getEdge(i, j) + " ");
//                    System.out.println();
//                }
//            } catch (Exception e) {
//                System.out.println("Somthing went wrong");
//            }
//            sc.close();
        /*To check/print the values in the adjacencyMap*/
//        for(Entry<Integer, ArrayList<Integer>> e : adjacencyMap.entrySet()){
//            //System.out.println("here");
//            int key = e.getKey();
//            System.out.print("From -> " + key);
//            ArrayList<Integer> newList = e.getValue();
//            newList.forEach(System.out::println);
//        }
        computeKatzCentralities(nodesCount);
        findTopKNodes(nodesCount);
    }

    public static void computeKatzCentralities(int nodesCount) {
        kcMatrix = new KatzCentralities(nodesCount);
        
        ArrayList<Double> eigenValuesList = kcMatrix.getEigenValues(adjacencyMatrix);
        double lambda = Collections.max(eigenValuesList, null); //largest eigen value
        double alpha = ThreadLocalRandom.current().nextDouble(0, 1 / lambda);  //damping factor
        
        Scanner inputBeta = new Scanner(System.in);
        System.out.println("Provide the value for Beta: ");
        String stringBeta = inputBeta.next().trim();
        double beta;
        if (stringBeta == null) {
            System.out.println("The value for Beta i.e. 1.0 will be used.");
            beta = 1.0;
        } else {
            beta = Double.parseDouble(stringBeta);
        }
        
        alpha = 0.25;
        kcMatrix.compute(alpha,beta);
        
        kcMatrix.print();
    }
    
    
    public static void findTopKNodes(int nodesCount) {
        //kcMap -> contains nodes and their corresponding Katz Centralities
        double nodeKatz;
        double globalSumKatz = 0.00;
        
        Matrix katzMatrix = kcMatrix.getKatzMatrix();
        double[][] katzArray = katzMatrix.getArray();   //getting the Katz values in Array
        //System.out.println( " " + katzArray.length );
        
        // Looping through the the Katz values and putting in a HashMap <node, its katz Value>
        for(int node = 0; node < katzArray.length; node++) {
            //System.out.println("here");
            nodeKatz = katzArray[node][0];
            globalSumKatz += nodeKatz;
            kcMap.put(node, nodeKatz);
        }
        
//        for(int node = 0; node <= nodesCount ; node++) {
//            nodeKatz = kcMatrix.getNodeKatz(node, 0);
//            sumKatz += nodeKatz;
//            kcMap.put(node, nodeKatz);
//        }
        double globalAvgKatz = globalSumKatz/nodesCount; 
        System.out.println("Global Avg Katz: "+globalAvgKatz);
        
        ArrayList<Integer> topKList = new ArrayList<>();
//        HashMap<Integer, Double> 
        
        for(Entry<Integer, ArrayList<Integer>> e : adjacencyMap.entrySet()){
            int fromNode = e.getKey();
            
            double fromNodeKatz = kcMap.get(fromNode);
            System.out.println(fromNode + "->" +fromNodeKatz);
            
            if (fromNodeKatz >= globalAvgKatz){
                double localAvgKatz;
                double localSumKatz = fromNodeKatz;
//                double localSumKatz = 0.00;
                
                ArrayList<Integer> neighboursList = e.getValue();
                int neighbourCount = neighboursList.size();
                double toNodeKatz;
                
                for (Integer toNode : neighboursList) {
                    toNodeKatz = kcMap.get(toNode);
                    localSumKatz += toNodeKatz;
                }
                
                localAvgKatz = localSumKatz/(neighbourCount + 1);
                System.out.println("Local Avg Katz of node '" + fromNode + "': "+localAvgKatz);
                
                
                if (localAvgKatz >= globalAvgKatz) {
                    topKList.add(fromNode);
                } 
            }  
        }
        System.out.println(topKList);
    }
}
