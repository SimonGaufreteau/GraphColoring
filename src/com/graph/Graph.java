package com.graph;

import java.util.ArrayList;
import java.util.LinkedList;

public class Graph {
	String name;
	boolean isDirected;
	int nbVertices;
	int nbEdges;
	int nbVertexValues;
	int nbEdgesValues;
	ArrayList<Vertex> listVertices;
	ArrayList<Edge> listEdges;
	ArrayList<LinkedList<Integer>> listAdjacent;

	public Graph(String name, boolean isDirected, int nbVertices,int nbEdges,int nbVertexValues, int nbEdgesValues, ArrayList<Vertex> listVertices,ArrayList<Edge> listEdges,ArrayList<LinkedList<Integer>> listAdjacent) {
		this.name = name;
		this.nbEdges=nbEdges;
		this.isDirected = isDirected;
		this.nbVertices = nbVertices;
		this.nbVertexValues = nbVertexValues;
		this.nbEdgesValues = nbEdgesValues;
		this.listVertices = listVertices;
		this.listEdges=listEdges;
		this.listAdjacent=listAdjacent;
	}


	public ArrayList<Vertex> getListVertices() {
		return listVertices;
	}

	public String degreesToString(){
		StringBuilder res = new StringBuilder("List of degrees : \n");
		for(Vertex vertex:listVertices){
			res.append(vertex.getId()).append(" -> ").append(vertex.getDegree()).append("\n");
		}
		return res.toString();
	}

	public String toString(){
		StringBuilder res = new StringBuilder("----- GRAPH -----\n" +
				"Graph name : " + name + "\n" +
				"The graph is : " + (isDirected ? "directed" : "not directed") + "\n" +
				"Number of vertices : " + nbVertices + "\n" +
				"Number of edges : " + nbEdges + "\n" +
				"Number of values of the vertices : " + nbVertexValues + "\n" +
				"Number of values of the edges : " + nbEdgesValues + "\n" +
				"---\n" +
				"List of vertices :\n");
		for(Vertex vertex:listVertices){
			res.append(vertex.getId()).append(" [ ");
			double[] values = vertex.getValues();
			for(int i=0;i<vertex.getNbValues();i++){
				res.append(values[i]).append(" ");
			}
			res.append("]\n");
		}
		res.append("---\nList of edges :\n");
		for(Edge edge:listEdges){
			double[] values = edge.getValues();
			res.append(edge.getId()).append(" (").append(edge.getIndexInitialVertex()).append(",").append(edge.getIndexFinalVertex()).append(") [ ");
			for(int i=0;i<edge.getNbValues();i++){
				res.append(values[i]).append(" ");
			}
			res.append("]\n");
		}
		res.append("---\nAdjacent list :\n");
		for(int i=0;i<listAdjacent.size();i++){
			LinkedList<Integer> linkedList=listAdjacent.get(i);
			res.append(i).append(" - ");
			for (Integer integer : linkedList) {
				Edge tempEdge = listEdges.get(integer);
				if (i == tempEdge.getIndexInitialVertex())
					res.append(tempEdge.getIndexFinalVertex()).append("(").append(tempEdge.getId()).append(") ");
				else
					res.append(tempEdge.getIndexInitialVertex()).append("(").append(tempEdge.getId()).append(") ");
			}
			res.append("\n");
		}
		res.append("----- GRAPH -----\n");

		return res.toString();
	}
}
