package com;

import com.graph.Edge;
import com.graph.Graph;
import com.graph.SpanningAlgorithms;
import com.graph.Vertex;
import com.graphParser.GraphParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class MainMST {
	public static void main(String[] args) throws Exception {
		//shrd150
		Graph graph = GraphParser.getGraphFromFile("tests/MSTgraphs/crd1005.mst");
		System.out.println(graph);

		//Kruskal1
		System.out.println("Results for Kruskal v1 :\n");
		ArrayList<Edge> edges = SpanningAlgorithms.Kruskal1(graph);
		SpanningAlgorithms.displayKruskal(edges);
		System.out.println(SpanningAlgorithms.getWeight(edges));

		//Kruskal2
		System.out.println("Results for Kruskal v2 :\n");
		edges = SpanningAlgorithms.Kruskal1(graph);
		//SpanningAlgorithms.displayAlgorithmsResults(edges);
		System.out.println(SpanningAlgorithms.getWeight(edges));

		//Prim
		System.out.println("Results for Prim :\n");
		Random r = new Random();
		Vertex randomVertex = graph.getListVertices().get(r.nextInt(graph.getNbVertices()));
		System.out.println(randomVertex);
		Vertex[] pred = SpanningAlgorithms.prim(graph,randomVertex);
		SpanningAlgorithms.displayPrim(graph,pred);

	}
/*
	public static Graph constructGraph(Graph basegraph,Vertex[] pred){
		ArrayList<Edge> list=new ArrayList<>();
		for (int i = 0; i < pred.length; i++) {
			Vertex predVertex = pred[i];
			if(predVertex==null )continue;
			LinkedList<Integer> adj = basegraph.getListAdjacent().get(i);
			for (int nextID : adj) {
				Edge edge = basegraph.getListEdges().get(nextID);
				int neighbourID;
				if (edge.getIndexInitialVertex() == i) neighbourID = edge.getIndexFinalVertex();
				else neighbourID = edge.getIndexInitialVertex();
				if(neighbourID==predVertex.getId()){
					list.add(edge);
					break;
				}
			}
		}
		return new Graph(basegraph.getListVertices(),list,basegraph.getListAdjacent());
	}*/

}
