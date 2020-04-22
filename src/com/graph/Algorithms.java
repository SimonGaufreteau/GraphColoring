package com.graph;

import java.util.*;
import java.util.stream.Collectors;

public class Algorithms {
	private Algorithms(){}

	private static int findSmallestColor(boolean[] available, int nb) {
		int color;
		for(color=0;color<nb;color++){
			if(available[color])
				return color;
		}
		return nb;
	}

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
		return map;
	}

	public static Map<Integer,Integer> welshPowell(Graph graph, ArrayList<Vertex> order) {
		ArrayList<Vertex> L = (ArrayList<Vertex>) order.clone();

		Map<Integer, Integer> map = new HashMap<>();
		int k = 1;
		while (!L.isEmpty()) {
			Vertex x = L.get(0);
			map.put(x.getId(), k);
			L.remove(x);
			if(L.isEmpty()) break;
			Vertex y = L.get(0);
			for (int i = 0; i < L.size(); i++) {
				Vertex vertex = L.get(i);
				if (!isAdjacentColor(y, graph, map, k)) {
					map.put(y.getId(), k);
					L.remove(y);
					if(L.isEmpty()) break;
				}
				y = vertex;
			}
			k++;
		}
		return map;
	}

	private static boolean isAdjacentColor(Vertex y, Graph graph,Map<Integer,Integer> colors,int k) {
		LinkedList<Integer> linkedList = graph.listAdjacent.get(y.getId());
		for(Integer edgeID:linkedList){
			Edge edge = graph.listEdges.get(edgeID);
			int neighbourID;
			if (edge.getIndexInitialVertex() == y.getId())
				neighbourID=edge.getIndexFinalVertex();
			else neighbourID=edge.getIndexInitialVertex();

			if(colors.get(neighbourID)!=null  && colors.get(neighbourID)==k) return true;
		}
		return false;
	}


	public static void displayColoringResult(Map<Integer,Integer> colors){
		for(int id:colors.keySet()){
			System.out.println("Vertex " + id + " --->  Color "
					+ colors.get(id));
		}
	}

}
