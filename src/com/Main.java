package com;

import com.graph.Graph;
import com.graphParser.GraphParser;

public class Main {
	public static void main(String[] args) throws Exception {
		String filepath = "tests/myciel3.col";
		Graph graph= GraphParser.getGraphFromFile(filepath);
		System.out.println(graph);

		String filepath2 = "tests/anna.col";
		Graph graph2= GraphParser.getGraphFromFile(filepath2);
		System.out.println(graph2);
	}
}
