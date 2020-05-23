package com.graph;

import java.util.*;

public class ColoringAlgorithms {
	private ColoringAlgorithms(){}

	private static int findSmallestColor(boolean[] available, int nb) {
		int color;
		for(color=0;color<nb;color++){
			if(available[color])
				return color;
		}
		return nb;
	}

	/**
	 * Greedy Coloring algorithm. Returns a mapping of Vertex.getID() --> color.
	 */
	public static Map<Integer,Integer> greedyColoring(Graph graph, ArrayList<Vertex> order){
		int nb=graph.nbVertices;
		Map<Integer,Integer> map = new HashMap<>();

		for(Vertex vertex:order){
			map.put(vertex.getId(),-1);
		}

		// Assign the first color to first vertex
		map.put(order.get(0).getId(),0);

		// A temporary array to store the available colors.
		boolean[] available = new boolean[nb];

		// Initially, all colors are available
		Arrays.fill(available, true);

		for(int i=1;i<nb;i++){
			int id=order.get(i).getId();
			available = getAvailableColors(id,graph,map);
			map.put(order.get(i).getId(),findSmallestColor(available,nb));
			// Reset the values back to true for the next iteration
			Arrays.fill(available, true);
		}
		return map;
	}
	/**
	 * Welsh-Powell algorithm. Returns a mapping of Vertex.getID() --> color.
	 */
	public static Map<Integer,Integer> welshPowell(Graph graph, ArrayList<Vertex> order) {
		ArrayList<Vertex> L = (ArrayList<Vertex>) order.clone();

		Map<Integer, Integer> map = new HashMap<>();
		int k = 0;
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

	/**
	 * DSATUR heuristic. Returns a mapping of Vertex.getID() --> color.
	 */
	public static Map<Integer,Integer> dsatur(Graph graph) {
		Map<Integer,Integer> dsatMap = new HashMap<>();
		Map<Integer,Integer> colorMap = new HashMap<>();
		for(Vertex vertex:graph.getListVertices()){
			dsatMap.put(vertex.getId(),vertex.getDegree());
		}
		ArrayList<Integer> coloredID = new ArrayList<>();

		int nb = graph.nbVertices;
		boolean[] available = new boolean[nb];
		Arrays.fill(available,true);

		while (colorMap.size()!=graph.nbVertices){
			int id =getMaxDsat(dsatMap);
			available = getAvailableColors(id, graph, colorMap);
			colorMap.put(id, findSmallestColor(available, available.length));
			// Decreasing the dsat value of the vertex to avoid an infinite loop
			dsatMap.replace(id,-1);
			coloredID.add(id);
			updateAdjacentDSAT(id, graph, colorMap, dsatMap,coloredID);
		}
		return colorMap;
		}

	private static int getMaxDsat(Map<Integer, Integer> dsatMap) {
		int maxID = -1;
		int maxDSAT = -1;
		for(Integer id : dsatMap.keySet()){
			int dsat = dsatMap.get(id);
			if(dsat>maxDSAT) {
				maxID=id;
				maxDSAT=dsat;
			}
		}
		return maxID;
	}

	private static void updateAdjacentDSAT(int id, Graph graph, Map<Integer, Integer> colorMap,Map<Integer,Integer> dsatMap,ArrayList<Integer> coloredID)  {
		//Update dsat of adjacent vertices
		LinkedList<Integer> linkedList = graph.listAdjacent.get(id);
		for(Integer edgeID : linkedList){
			Edge edge = graph.listEdges.get(edgeID);
			int neighbourID;
			if (edge.getIndexInitialVertex() == id)
				neighbourID=edge.getIndexFinalVertex();
			else neighbourID=edge.getIndexInitialVertex();
			if(coloredID.contains(neighbourID)) continue;
			updateDSAT(neighbourID,graph,colorMap,dsatMap);
		}

}

	private static void updateDSAT(int id, Graph graph, Map<Integer, Integer> colorMap, Map<Integer, Integer> dsatMap) {
		boolean[] availableColors = getAvailableColors(id,graph,colorMap);
		int nbColors =0;
		for (boolean color : availableColors) {
			if (!color)
				nbColors++;
		}
		if(nbColors>0){
			dsatMap.replace(id,nbColors);
		}
	}

	/**
	 * Returns the available colors for the given Vertex ID.
	 */
	private static boolean[] getAvailableColors(int id,Graph graph,Map<Integer,Integer> colorMap){
		boolean[] available = new boolean[graph.nbVertices];
		LinkedList<Integer> linkedList = graph.listAdjacent.get(id);
		Arrays.fill(available,true);
		for (Integer integer : linkedList) {
			Edge edge = graph.listEdges.get(integer);
			int neighbourID;
			if (edge.getIndexInitialVertex() == id) neighbourID = edge.getIndexFinalVertex();
			else neighbourID = edge.getIndexInitialVertex();
			if (colorMap.containsKey(neighbourID) && colorMap.get(neighbourID)>=0)
				available[colorMap.get(neighbourID)] = false;
		}
		return available;
	}

	/**
	 * Checks the coloring returned by one of the algorithms above.
	 */
	public static boolean checkColoring(Graph graph,Map<Integer,Integer> colorMap){
		if(colorMap.size()!=graph.nbVertices) return false;
		for (int id = 0; id < graph.listAdjacent.size(); id++) {
			int color = colorMap.get(id);
			LinkedList<Integer> linkedList = graph.listAdjacent.get(id);
			for(Integer edgeID:linkedList){
				Edge edge = graph.listEdges.get(edgeID);
				int neighbourID;
				if (edge.getIndexInitialVertex() == id) neighbourID = edge.getIndexFinalVertex();
				else neighbourID=edge.getIndexInitialVertex();

				if(colorMap.get(neighbourID)==color) return false;
			}
		}
		return true;
	}

	public static void displayColoringResult(Map<Integer,Integer> colors){
		for(int id:colors.keySet()){
			System.out.println("Vertex " + id + " --->  Color "
					+ colors.get(id));
		}
	}

	public static int getMaxColor(Map<Integer,Integer> colorMap){
		int maxColor = 0;
		for(Integer integer : colorMap.keySet()){
			int color=colorMap.get(integer);
			if(color>maxColor)maxColor=color;
		}
		return maxColor;
	}

}
