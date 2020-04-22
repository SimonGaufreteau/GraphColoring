package com.graph;

import java.util.*;
import java.util.stream.Collectors;

public class Algorithms {
	private Algorithms(){}

	/**
	 * Greedy Coloring algorithm. Returns a mapping of Verted.getID() --> color.
	 */
	public static Map<Integer,Integer> greedyColoring(Graph graph, ArrayList<Vertex> order){
		int nb=graph.nbVertices;
		Map<Integer,Integer> map = new HashMap<>();

		for(Vertex vertex:order){
			map.put(vertex.getId(),-1);
		}

		// Assign the first color to first vertex
		map.put(order.get(0).getId(),0);

		// A temporary array to store the available colors. False
		// value of available[cr] would mean that the color cr is
		// assigned to one of its adjacent vertices
		boolean[] available = new boolean[nb];

		// Initially, all colors are available
		Arrays.fill(available, true);

		for(int i=1;i<nb;i++){
			LinkedList<Integer> linkedList = graph.listAdjacent.get(order.get(i).getId());
			for (Integer integer : linkedList) {
				Edge edge = graph.listEdges.get(integer);
				int neighbourID;
				if (edge.getIndexInitialVertex() == order.get(i).getId()) neighbourID = edge.getIndexFinalVertex();
				else neighbourID = edge.getIndexInitialVertex();
				if(map.get(neighbourID)!=-1)
					available[map.get(neighbourID)]=false;
			}
			map.put(order.get(i).getId(),findSmallestColor(available,nb));
			// Reset the values back to true for the next iteration
			Arrays.fill(available, true);
		}

		// print the result
		for(int id:map.keySet()){
			System.out.println("Vertex " + id + " --->  Color "
					+ map.get(id));
		}
		return map;
	}

	private static int findSmallestColor(boolean[] available, int nb) {
		int color;
		for(color=0;color<nb;color++){
			if(available[color])
				return color;
		}
		return nb;
	}
}
