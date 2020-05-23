package com;

import com.graph.*;
import com.graphParser.GraphParser;

import java.beans.PropertyEditorManager;
import java.util.*;

public class MainMST {
	private static final double nanoToMS =Math.pow(10,6);

	public static void main(String[] args) throws Exception {
		//shrd150
		Graph graph = GraphParser.getGraphFromFile("tests/MSTgraphs/crd1003.mst");
		System.out.println(graph);

		//Kruskal1
		System.out.println("Results for Kruskal v1 :");
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
		System.out.println("\nResults for Prim :");
		Random r = new Random();
		Vertex randomVertex = graph.getListVertices().get(r.nextInt(graph.getNbVertices()));
		startTime=System.nanoTime();
		Vertex[] pred = SpanningAlgorithms.prim(graph,randomVertex);
		resultTime = (System.nanoTime()-startTime)/nanoToMS;
		SpanningAlgorithms.displayPrim(graph,pred);
		System.out.println("Time : "+resultTime+"ms");


		//Prim Fibo
		System.out.println("\nResults for Prim (Fibonacci) :");
		startTime=System.nanoTime();
		pred = SpanningAlgorithms.primFibo(graph,randomVertex);
		resultTime = (System.nanoTime()-startTime)/nanoToMS;
		SpanningAlgorithms.displayPrim(graph,pred);
		System.out.println("Time : "+resultTime+"ms");
	}

}
