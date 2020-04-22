package com;

import com.graph.Algorithms;
import com.graph.Graph;
import com.graph.Vertex;
import com.graphParser.GraphParser;

import java.util.*;

public class Main {
	public static void main(String[] args) throws Exception {
		String filepath = "tests/anna.col";
		Graph graph= GraphParser.getGraphFromFile(filepath);
		System.out.println(graph);
		System.out.println(graph.degreesToString());
		Algorithms.displayColoringResult(Algorithms.greedyColoring(graph,graph.getListVertices()));
		System.out.println("\nShuffling the order...\n");
		ArrayList<Vertex> order = graph.getListVertices();
		Collections.shuffle(order);
		Algorithms.displayColoringResult(Algorithms.greedyColoring(graph,order));

		System.out.println("\nWelsh Powell :\n");
		graph.getListVertices().sort((o1, o2) -> o2.getDegree()-o1.getDegree());
		System.out.println(Arrays.toString(graph.getListVertices().toArray()));
		Algorithms.displayColoringResult(Algorithms.welshPowell(graph,order));



		/*
		String filepath2 = "tests/anna.col";
		Graph graph2= GraphParser.getGraphFromFile(filepath2);
		System.out.println(graph2);
		System.out.println(graph2.degreesToString());*/
	}
}
