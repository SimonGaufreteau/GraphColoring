package com;

import com.graph.*;
import com.graphParser.GraphParser;

import java.beans.PropertyEditorManager;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainMST {
	private static final double nanoToMS =Math.pow(10,6);

	private static void printToFile(String[] args) throws Exception {
		if(args.length!=1){
			throw new Exception("Args length must be 1. Only the path to the test directory containing .col files is accepted as an argument.");
		}
		PrintStream printStream = new PrintStream(args[0]+"/DMST4.csv");
		System.setOut(printStream);
		//Random CSV output
		//System.out.println("Graph,Max color,Min color,Average color,Max time,Min time,Average Time");

		//One test CSV output
		System.out.println("Graph,Weight,Time");
		try (Stream<Path> walk = Files.walk(Paths.get(args[0]))) {
			List<String> result = walk.map(Path::toString)
					.filter(f -> f.endsWith(".mst")).collect(Collectors.toList());

			for(String fileName:result){
				Graph graph= GraphParser.getGraphFromFile(fileName);
				//Standard output
				//System.out.println("Testing on : "+fileName);

				//CSV output
				System.out.print(new File(fileName).getName()+",");
				testDMST(graph,4);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void testKruskal1(Graph graph){
		long startTime=System.nanoTime();
		ArrayList<Edge> edges = SpanningAlgorithms.Kruskal1(graph);
		double resultTime = (System.nanoTime()-startTime)/nanoToMS;
		int weight = (int)SpanningAlgorithms.getWeight(edges);
		System.out.println(weight+","+resultTime);
	}

	private static void testKruskal2(Graph graph){
		long startTime=System.nanoTime();
		ArrayList<Edge> edges = SpanningAlgorithms.Kruskal2(graph);
		double resultTime = (System.nanoTime()-startTime)/nanoToMS;
		edges.removeIf(Objects::isNull);
		int weight = (int)SpanningAlgorithms.getWeight(edges);
		System.out.println(weight+","+resultTime);
	}

	private static void testPrim(Graph graph){
		Random r = new Random();
		Vertex randomVertex = graph.getListVertices().get(r.nextInt(graph.getNbVertices()));
		long startTime=System.nanoTime();
		Vertex[] pred = SpanningAlgorithms.prim(graph,randomVertex);
		double resultTime = (System.nanoTime()-startTime)/nanoToMS;
		int weight = (int)SpanningAlgorithms.getWeight(graph,pred);
		System.out.println(weight+","+resultTime);
	}

	private static void testPrimFibo(Graph graph){
		Random r = new Random();
		Vertex randomVertex = graph.getListVertices().get(r.nextInt(graph.getNbVertices()));
		long startTime=System.nanoTime();
		Vertex[] pred = SpanningAlgorithms.primFibo(graph,randomVertex);
		double resultTime = (System.nanoTime()-startTime)/nanoToMS;
		int weight = (int)SpanningAlgorithms.getWeight(graph,pred);
		System.out.println(weight+","+resultTime);
	}

	private static void testDMST(Graph graph,int k){
		long startTime=System.nanoTime();
		ArrayList<Edge> edges =  SpanningAlgorithms.primDMST(graph,k);
		double resultTime = (System.nanoTime()-startTime)/nanoToMS;
		int weight = (int)SpanningAlgorithms.getWeight(edges);
		System.out.println(weight+","+resultTime);
	}


	public static void main(String[] args) throws Exception {
		printToFile(args);
		//sampleTest();
	}
	public static void sampleTest() throws Exception {
		//shrd150
		Graph graph = GraphParser.getGraphFromFile("tests/MSTgraphs/crd1000.mst");
		System.out.println(graph);

		//Kruskal1
		/*System.out.println("Results for Kruskal v1 :");
		long startTime=System.nanoTime();
		ArrayList<Edge> edges = SpanningAlgorithms.Kruskal1(graph);
		double resultTime = (System.nanoTime()-startTime)/nanoToMS;
		SpanningAlgorithms.displayKruskal(edges);
		System.out.println("Time : "+resultTime+"ms");

		//Kruskal2
		System.out.println("\nResults for Kruskal v2 :");
		startTime=System.nanoTime();
		edges =  SpanningAlgorithms.Kruskal2(graph);
		resultTime = (System.nanoTime()-startTime)/nanoToMS;
		edges.removeIf(Objects::isNull);
		SpanningAlgorithms.displayKruskal(edges);
		System.out.println("Time : "+resultTime+"ms");

		//Prim
		Random r = new Random();
		Vertex randomVertex = graph.getListVertices().get(r.nextInt(graph.getNbVertices()));
		System.out.println("\nResults for Prim :");
		startTime=System.nanoTime();
		Vertex[] pred = SpanningAlgorithms.prim(graph,randomVertex);
		resultTime = (System.nanoTime()-startTime)/nanoToMS;
		SpanningAlgorithms.displayPrim(graph,pred);
		System.out.println("Time : "+resultTime+"ms");


		//Prim Fibo
		System.out.println("\nResults for Prim (Fibonacci) :");
		startTime=System.nanoTime();
		Vertex[] pred = SpanningAlgorithms.primFibo(graph,randomVertex);
		resultTime = (System.nanoTime()-startTime)/nanoToMS;
		SpanningAlgorithms.displayPrim(graph,pred);
		System.out.println("Time : "+resultTime+"ms");*/


		//Prim D-MST
		int k=2;
		long startTime=System.nanoTime();
		ArrayList<Edge> edges =  SpanningAlgorithms.primDMST(graph,k);
		double resultTime = (System.nanoTime()-startTime)/nanoToMS;
		int weight = (int)SpanningAlgorithms.getWeight(edges);
		System.out.println(weight+","+resultTime);

		//D-MST
		/*System.out.println("\nResults for D-MST ("+k+") :");
		startTime=System.nanoTime();
		edges =  SpanningAlgorithms.dmst(graph,k);
		resultTime = (System.nanoTime()-startTime)/nanoToMS;
		SpanningAlgorithms.displayKruskal(edges);
		System.out.println("Time : "+resultTime+"ms");*/



	}

}
