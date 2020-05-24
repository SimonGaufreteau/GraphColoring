package com.graph;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Simple implementation of a Graph using {@link Vertex} and {@link Edge}.
 */
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
		updateDegrees();
	}

	public Graph(ArrayList<Vertex> vertices, ArrayList<Edge> edges,ArrayList<LinkedList<Integer>> listAdjacent) {
		this("",false,vertices.size(),edges.size(),vertices.get(0).getNbValues(),edges.get(0).getNbValues(),vertices,edges,listAdjacent);
		updateAdjacent();
		updateDegrees();
	}

	public void updateDegrees(){
		for(Vertex vertex:listVertices){
			vertex.setDegree(listAdjacent.get(vertex.getId()).size());
		}
	}

	public ArrayList<Vertex> getListVertices() {
		return listVertices;
	}

	public ArrayList<Edge> getListEdges() {
		return listEdges;
	}

	public String getName() {
		return name;
	}

	public boolean isDirected() {
		return isDirected;
	}

	public int getNbVertices() {
		return nbVertices;
	}

	public int getNbEdges() {
		return nbEdges;
	}

	public int getNbVertexValues() {
		return nbVertexValues;
	}

	public int getNbEdgesValues() {
		return nbEdgesValues;
	}

	public ArrayList<LinkedList<Integer>> getListAdjacent() {
		return listAdjacent;
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
			res.append("] degree : ").append(vertex.getDegree()).append("\n");
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
				if(integer>=listEdges.size()) continue;
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

	public void updateAdjacent() {
		listAdjacent = new ArrayList<>();
		for(int i=0;i<nbVertices;i++)listAdjacent.add(new LinkedList<>());

		for(Edge edge:listEdges){
			int initialID = edge.getIndexInitialVertex();
			int finalID = edge.getIndexFinalVertex();
			listAdjacent.get(initialID).add(edge.getId());
			listAdjacent.get(finalID).add(edge.getId());
		}
	}

	public int getMaxDegree() {
		int max = 0;
		for(Vertex v:listVertices){
			if(v.getDegree()>max)
				max=v.getDegree();
		}
		return max;
	}
}
