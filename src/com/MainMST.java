package com;

import com.graph.Edge;
import com.graph.Graph;
import com.graph.SpanningAlgorithms;
import com.graphParser.GraphParser;

import java.util.ArrayList;

public class MainMST {
	public static void main(String[] args) throws Exception {
		//shrd150
		Graph graph = GraphParser.getGraphFromFile("tests/MSTgraphs/shrd150.mst");
		System.out.println(graph);

		//Kruskal1
		System.out.println("Results for Kruskal v1 :\n");
		ArrayList<Edge> edges = SpanningAlgorithms.Kruskal1(graph);
		SpanningAlgorithms.displayAlgorithmsResults(edges);

		//Kruskal2
		System.out.println("Results for Kruskal v2 :\n");
		edges = SpanningAlgorithms.Kruskal1(graph);
		SpanningAlgorithms.displayAlgorithmsResults(edges);
	}

}
