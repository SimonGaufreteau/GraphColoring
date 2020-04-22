package com;

import com.graph.Algorithms;
import com.graph.Graph;
import com.graph.Vertex;
import com.graphParser.GraphParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Main {
	public static void main(String[] args) throws Exception {
		String filepath = "tests/myciel3.col";
		Graph graph= GraphParser.getGraphFromFile(filepath);
		System.out.println(graph);
		System.out.println(graph.degreesToString());
		Algorithms.greedyColoring(graph,graph.getListVertices());
		System.out.println("\nShuffling the order...\n");
		ArrayList<Vertex> order = graph.getListVertices();
		Collections.shuffle(order);
		Algorithms.greedyColoring(graph,order);

		/*
		String filepath2 = "tests/anna.col";
		Graph graph2= GraphParser.getGraphFromFile(filepath2);
		System.out.println(graph2);
		System.out.println(graph2.degreesToString());*/
	}
}
