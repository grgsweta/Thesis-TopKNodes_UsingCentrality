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
import java.util.concurrent.ConcurrentHashMap;
import Jama.*;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.sun.xml.internal.bind.api.impl.NameConverter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Scanner;
//import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
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

    
    //public static HashMap<Integer, ArrayList<Integer>> original_adjacencyMap = new HashMap<>();  //HashMap with original Node id and its neighbours list
    public static ConcurrentHashMap<Integer, ArrayList<Integer>> adjacencyMap = new ConcurrentHashMap<>();           //ConcurrentHashMap with NEW Node id  and its neighbours' new ids list
    public static ConcurrentHashMap<Integer, Integer> nodesMapping = new ConcurrentHashMap<>();   //original node ID -> serial numbers (starting from 0) assigned to each node id
    public static Matrix adjacencyMatrix;   
    public static Matrix outDegreeMatrix;  //pagerank   
    public static KatzCentralities kcMatrix;
    public static ConcurrentHashMap<Integer, Double> kcMap = new ConcurrentHashMap<>();  //kcMap -> contains nodes and their corresponding Katz Centralities
    public static ArrayList<Integer> finalTopKList = new ArrayList<>();  //kcMap -> contains nodes and their corresponding Katz Centralities

//    public static ConcurrentHashMap<Integer, ArrayList<Integer>> nbrKatzCentralitiesMap = new ConcurrentHashMap<>();
//    public static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Double>> nbrKatzCentralitiesMap = new ConcurrentHashMap<>();
   
    public static void main(String args[]) throws IOException {
        long startTime = System.currentTimeMillis();
//        String sourceFile = "input/papereg.edges";
//        String sourceFile = "input/mypapereg.edges";
//        String sourceFile = "input/soc-twitter-follows-mun.edges";
//        String sourceFile = "input/soc-Epinions1.txt";
//        String sourceFile = "input/epinionsdata_truncated.txt"; //1136 48742
//        String sourceFile = "input/epinionsdata_truncated1.txt";  //1247 51558 '0.01194013245316612' *
//        String sourceFile = "input/epinionsdata_truncated3.txt";  //2023 65346
//        String sourceFile = "input/epinionsdata_truncated2.txt";  //2022 65163
//        String sourceFile = "input/epinionsdata_truncated4.txt";  //1799 61037  '0.01194013245316612'*
//        String sourceFile = "input/107.edges";    //1034 53498 '0.00811588280169696' * 
//        String sourceFile = "input/1912.edges"; //747
//        String sourceFile = "input/414.edges"; //150
//        String sourceFile = "input/0-414.txt"; //480
        String sourceFile = "input/0-414-107.txt"; //1495 61922 * '0.008115881292588719'
//        String sourceFile = "input/facebook-0.edges";
//        String sourceFile = "input/twitter-truncated-data.txt";
//        String sourceFile = "input/twitter-truncated1.txt"; //80059 247958
//        String sourceFile = "input/amazon_truncated.txt";  //1500  6010 * higher connectivity
//        String sourceFile = "input/pagerankexample.txt";  //pagerank example from Book: Social Media Mining
        
        ConcurrentHashMap<Integer, ArrayList<Integer>> original_adjacencyMap = new ConcurrentHashMap<>();  //ConcurrentHashMap with original Node id and its neighbours list
        BufferedReader sourceFileRead = null;
        int noOfEdges = 0; //number of edges
        
        try {
            sourceFileRead = new BufferedReader(new FileReader(sourceFile));
            //StringBuffer document = new StringBuffer();

            String currentLine = null;
            int fromNode;  //source node
            int toNode;  //destination node
            
            
            while ((currentLine = sourceFileRead.readLine()) != null) {
                if (Character.isDigit(currentLine.charAt(0))) {        //distinguishes each document by the starting point ".i"
//                        SrtingTokenizer token = new StringTokenizer(currentLine);
                    noOfEdges +=1;
                    String[] element = currentLine.split("\\s+");
                    fromNode = parseInt(element[0]);
                    toNode = parseInt(element[1]);
                    if (original_adjacencyMap.containsKey(fromNode)) {
                        //System.out.println("1case: " + fromNode +"->"+toNode);
                        
                        if (!original_adjacencyMap.get(fromNode).contains(toNode)) {
                            original_adjacencyMap.get(fromNode).add(toNode);
                        }
                        
                    } else if (!original_adjacencyMap.containsKey(fromNode)) {
                        
                        ArrayList<Integer> valueList = new ArrayList<>();
                        valueList.add(toNode);
                        //System.out.println("2case: " + fromNode +"->"+valueList);
                        original_adjacencyMap.put(fromNode, valueList);
                    } 
                    
                    //for undirected graph
                    //1 - 2 means there is a path from 1 to 2 and 2 to 1.
                    if (original_adjacencyMap.containsKey(toNode)) {
                        ArrayList<Integer> valueList = new ArrayList<>();
                        //System.out.println(toNode + ":"+ fromNode+" - > " + original_adjacencyMap.get(toNode));
                        if (!original_adjacencyMap.get(toNode).contains(fromNode)) {
                            original_adjacencyMap.get(toNode).add(fromNode);
                        }
                    } else {
                        ArrayList<Integer> valueList = new ArrayList<>();
                        valueList.add(fromNode);
                        original_adjacencyMap.put(toNode, valueList);
                    } 
                    
                    //Entry the toNode in the HashMap
                    if (!original_adjacencyMap.containsKey(toNode)) {
                       ArrayList<Integer> valueList = new ArrayList<>();
                       //System.out.println("3case: " + toNode +"->"+valueList);
                       original_adjacencyMap.put(toNode, valueList);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int nodesCount = original_adjacencyMap.size();
        System.out.println("Number of Nodes in the network: "+ nodesCount);
        System.out.println("Number of Edges in the network: "+ noOfEdges);
      
        /*
        //to print the values of the hashmap (node, neighbours list)
        for (Entry<Integer, ArrayList<Integer>> entry : original_adjacencyMap.entrySet()) {
            System.out.println(entry.getKey()+" : "+entry.getValue().size() +" : "+entry.getValue());
        }
        */

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
            //System.out.println(new_nodeID + "-" + nodeID);
            new_nodeID++;
        }
        //System.out.println();

        int node;
        int neighbour;
        for (Entry<Integer, ArrayList<Integer>> e : original_adjacencyMap.entrySet()) {
            //System.out.println("here");
            node = nodesMapping.get(e.getKey()); //getting the new id for the node from nodesMapping --key
            ArrayList<Integer> neighbourList = e.getValue();
            ArrayList<Integer> neighbourListUpdated = new ArrayList<>();
            //System.out.println(node +" "+neighbourList);
            
            for (Integer n : neighbourList) {
                neighbour = nodesMapping.get(n); //getting the new id for the node from nodesMapping --values
                //System.out.println(node + "-> "+ neighbour);
                neighbourListUpdated.add(neighbour);
            }

            adjacencyMap.put(node, neighbourListUpdated);
        }
        
        outDegreeMatrix = new Matrix(nodesCount, nodesCount); //pagerank
        adjacencyMatrix = new Matrix(nodesCount, nodesCount);
        int fromNode;
        double listSize;
        for (Entry<Integer, ArrayList<Integer>> e : adjacencyMap.entrySet()) {
            //System.out.println("here");
            fromNode = e.getKey();
            ArrayList<Integer> newList = e.getValue();
            for (Integer toNodes : newList) {
                //System.out.println(fromNode + "->" + toNodes);
                adjacencyMatrix.set(fromNode, toNodes, 1.0);
            }
            //System.out.println(fromNode + " -> "+ newList.size());
            listSize = (newList.size() > 1) ? newList.size() : 1.0;
            //System.out.println("listSize " + listSize);
            outDegreeMatrix.set(fromNode, fromNode, 1/listSize); //pagerank
        }
        
        //outDegreeMatrix = outDegreeMatrix.inverse();  //pagerank
        //outDegreeMatrix.print(nodesCount, nodesCount); //pagerank
        
        /*        
        //To Print the Adjacency Matrix of the Entire Network
        NumberFormat n1 = NumberFormat.getIntegerInstance();
        outDegreeMatrix.print(n1,nodesCount);
        */
        
        
        //System.out.println("before centrality");
        /*       
        //To Print the Adjacency Matrix of the Entire Network
        NumberFormat n = NumberFormat.getIntegerInstance();
        adjacencyMatrix.print(n,nodesCount);
        */
        /*
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
        */

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
        
        //pagerank testing
        computeKatzCentralities(nodesCount);
        findTopKNodes(nodesCount);
        
        long stopTime = System.currentTimeMillis();
        long timeElapsed = (stopTime-startTime)/1000;
        System.out.println();
        System.out.println("Total Execution time: " +timeElapsed + "seconds");
    }

    public static void computeKatzCentralities(int nodesCount) {
        kcMatrix = new KatzCentralities(nodesCount);
        
        //for Pagerank
//        Matrix pagerankeigen = adjacencyMatrix;
//        pagerankeigen = pagerankeigen.times(outDegreeMatrix);
//        ArrayList<Double> eigenValuesList = kcMatrix.getEigenValues(pagerankeigen);
        
        //For Katz Centrality
        ArrayList<Double> eigenValuesList = kcMatrix.getEigenValues(adjacencyMatrix);
        
        double lambda = Collections.max(eigenValuesList, null); //largest eigen value
        
        System.out.println("The value for Alpha should be less than '" + (1/lambda) + "'");
        
        //Commented for experimentation
        Scanner inputAlpha = new Scanner(System.in);
        System.out.println("Provide the value for Alpha: ");
        String stringAlpha = inputAlpha.next().trim();
        
        double alpha;
        if (stringAlpha == null || Double.parseDouble(stringAlpha) > (1 / lambda)) {
            alpha = ThreadLocalRandom.current().nextDouble(0, 1 / lambda);  //damping factor
            System.out.println("Random alpha value within the desired range value will be used.");
            System.out.println("Alpha value used: " + alpha);
        } else {
            alpha = Double.parseDouble(stringAlpha);
        }
        
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
        
        kcMatrix.compute(alpha,beta);
        
        
        /* //Experiment Purpose
        System.out.println("Alpha is 0.008; Beta is 1");
        kcMatrix.compute(0.30,0.1);
        */
        //kcMatrix.print();
    }
    
    
    public static void findTopKNodes(int nodesCount) throws IOException {
        //kcMap -> contains nodes and their corresponding Katz Centralities
        double nodeKatz;
        double globalSumKatz = 0.00;
        
        Matrix katzMatrix = kcMatrix.getKatzMatrix();
        double[][] katzArray = katzMatrix.getArray();   //getting the Katz values in Array
        //System.out.println( " " + katzArray.length );
        
        // Looping through the the Katz values and putting in a ConcurrentHashMap <node, its katz Value>
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
        System.out.println("Global Avg Katz:         " + globalAvgKatz);
        
        //StandardDeviation
        double sd = 0.00;
        for(int node = 0; node < katzArray.length; node++) {
            //System.out.println("here");
            nodeKatz = katzArray[node][0];
            sd += Math.pow((globalAvgKatz-nodeKatz),2);
        }
        sd = sd/nodesCount;
        sd = Math.sqrt(sd);
        
        System.out.println("Standard Deviation is: " + sd);
        
        /*Commented for experiment
        File file = new File("input/TopKNodesList.txt");
        File file1 = new File("input/LocalLessthanGlobal.txt");
        
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        BufferedWriter bw1 = new BufferedWriter(new FileWriter(file1));
        bw.write("The total number of nodes in the network: " + nodesCount);
        bw.newLine();
        bw.newLine();
        
        bw.write("fromNode fromNodeKatz localAvgKatz");
        bw.newLine();
        */
        ArrayList<Integer> topKList = new ArrayList<>();
        ConcurrentHashMap<Integer, Double> topKListHash = new ConcurrentHashMap<>();
        
        int fromNode;
        double fromNodeKatz;
        double localAvgKatz;
        double localSumKatz; 
        int neighbourCount;
        double toNodeKatz;
        
        System.out.println("Those having Local centrality < global+SD");
        
        for(Entry<Integer, ArrayList<Integer>> e : adjacencyMap.entrySet()){
            fromNode = e.getKey();
            
            fromNodeKatz = kcMap.get(fromNode);
            //System.out.println(fromNode + "->" + fromNodeKatz);
            
            if (fromNodeKatz >= (globalAvgKatz+sd)){
                localSumKatz = fromNodeKatz;
//                double localSumKatz = 0.00;
                
                ArrayList<Integer> neighboursList = e.getValue();
                neighbourCount = neighboursList.size();
                
                for (Integer toNode : neighboursList) {
                    toNodeKatz = kcMap.get(toNode);
                    localSumKatz += toNodeKatz;
                }
                
                localAvgKatz = localSumKatz/(neighbourCount + 1);
//                localAvgKatz = localSumKatz/(neighbourCount);
//                System.out.println(fromNode + "->" + fromNodeKatz);
//                System.out.println("Local Avg Katz of node '" + fromNode + "': "+localAvgKatz);
                
                
                if (localAvgKatz >= globalAvgKatz) {
                    topKList.add(fromNode);
                    
  
                   //bw.write(fromNode + " " + fromNodeKatz+ " " + localAvgKatz);
                   //bw.newLine();
                   
//                    System.out.println(fromNode + "->" + fromNodeKatz);
//                    System.out.println("Local Avg Katz of node '" + fromNode + "': "+localAvgKatz);
                }
//                System.out.println("Local < Global");
                
                 //Commented for experiment
                if (localAvgKatz < globalAvgKatz) {
                    //bw1.write(fromNode + " " + fromNodeKatz+ " " + localAvgKatz);
                    //bw1.newLine();
                    System.out.println(fromNode + "->" + fromNodeKatz);
                    System.out.println("Local Avg Katz of node '" + fromNode + "': "+localAvgKatz);
                }
                        
                
            }  
        }
        //System.out.println(topKList);
        /*Commented for experiment
        bw.close();
        bw1.close();
        */
        for (Integer newNodeID : topKList) {
            //one-to-one relationship between Key and Value
            for (Entry<Integer, Integer> entry : nodesMapping.entrySet()) {
                if (Objects.equals(newNodeID, entry.getValue())) {
                    finalTopKList.add(entry.getKey());  //getting the Old id for the node from nodesMapping --values
                    topKListHash.put(entry.getKey(), kcMap.get(entry.getValue()));
                }
            }
        }
        
        //System.out.println("The total number of nodes in the network: " + nodesCount);
        System.out.println("The total number of top-k nodes found: " + finalTopKList.size());
        System.out.println(finalTopKList);
        System.out.println();
        /*Commented for experiment
        System.out.println("How many top-K nodes do you want?: ");
        Scanner inputK = new Scanner(System.in);
        
        String stringK = inputK.next().trim();
        int topK;
        if (stringK == null) {
            System.out.println("All the top nodes will be displayed.");
            topK = nodesCount;
        } else {
            topK = Integer.parseInt(stringK);
        }
        */
        int topK = 10; //for experiment
        
//        ValueComparator bvc =  new ValueComparator(topKListHash);
//        TreeMap<Integer,Double> sorted_map = new TreeMap<>(bvc);
        
        
//        System.out.println(sorted_map);
        
        boolean DESC = false;
        HashMap<Integer, Double> sortedMapDesc = sortByComparator(topKListHash, DESC);
        
        System.out.println("Top " + topK + "nodes are as follows: ");
        printMap(sortedMapDesc,topK,nodesCount);

    }
    
    
    
     private static HashMap<Integer, Double> sortByComparator(ConcurrentHashMap<Integer, Double> unsortMap, final boolean order) {

        List<Entry<Integer, Double>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<Integer, Double>>()
        {
            public int compare(Entry<Integer, Double> o1,
                    Entry<Integer, Double> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        HashMap<Integer, Double> sortedMap = new LinkedHashMap<>();
        for (Entry<Integer, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public static void printMap(HashMap<Integer, Double> map, Integer topK, Integer nodesCount) {
        int i = 1;
        for (Entry<Integer, Double> entry : map.entrySet()) {
            if (i > topK && !Objects.equals(topK, nodesCount)) break;
            System.out.println("Node : " + entry.getKey() + " Katz Centrality : "+ entry.getValue());
            i++;
        }
    }
}

