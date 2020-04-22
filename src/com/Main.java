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
		ArrayList<Vertex> order = (ArrayList<Vertex>) graph.getListVertices().clone();
		Map<Integer,Integer> colorMap = Algorithms.greedyColoring(graph,order);
		Algorithms.displayColoringResult(colorMap);
		System.out.println("Checking coloring : " +(Algorithms.checkColoring(graph,colorMap)?"valid":"invalid"));
		System.out.println("Max color : "+Algorithms.getMaxColor(colorMap));

		System.out.println("\nShuffling the order...\n");
		Collections.shuffle(order);
		colorMap= Algorithms.greedyColoring(graph,order);
		Algorithms.displayColoringResult(colorMap);
		System.out.println("Checking coloring : " +(Algorithms.checkColoring(graph,colorMap)?"valid":"invalid"));
		System.out.println("Max color : "+Algorithms.getMaxColor(colorMap));

		System.out.println("\nWelsh Powell :\n");
		order.sort((o1, o2) -> o2.getDegree()-o1.getDegree());
		System.out.println(Arrays.toString(order.toArray()));
		colorMap= Algorithms.welshPowell(graph,order);
		Algorithms.displayColoringResult(colorMap);
		System.out.println("Checking coloring : " +(Algorithms.checkColoring(graph,colorMap)?"valid":"invalid"));
		System.out.println("Max color : "+Algorithms.getMaxColor(colorMap));

		System.out.println("\nDSATUR :\n");
		colorMap= Algorithms.dsatur(graph);
		Algorithms.displayColoringResult(colorMap);
		System.out.println("Checking coloring : " +(Algorithms.checkColoring(graph,colorMap)?"valid":"invalid"));
		System.out.println("Max color : "+Algorithms.getMaxColor(colorMap));

	}


}
