package com;

import com.graph.*;
import com.graphParser.GraphParser;

import java.util.*;

public class MainMST {
	private static final double nanoToMS =Math.pow(10,6);

	public static void main(String[] args) throws Exception {
		//shrd150
		Graph graph = GraphParser.getGraphFromFile("tests/MSTgraphs/sym709.mst");
		System.out.println(graph);

		//Kruskal1
		System.out.println("Results for Kruskal v1 :");
		long startTime=System.nanoTime();
		ArrayList<Edge> edges = SpanningAlgorithms.Kruskal1(graph);
		double resultTime = (System.nanoTime()-startTime)/nanoToMS;
		SpanningAlgorithms.displayKruskal(edges);
		System.out.println("Time : "+resultTime);


		//Kruskal2
		/*System.out.println("Results for Kruskal v2 :\n");
		startTime=System.nanoTime();
		edges =  SpanningAlgorithms.Kruskal2(graph);
		resultTime = (System.nanoTime()-startTime)/nanoToMS;
		SpanningAlgorithms.displayKruskal(edges);
		System.out.println("Time : "+resultTime);*/

		//Prim
		System.out.println("\nResults for Prim :");
		Random r = new Random();
		Vertex randomVertex = graph.getListVertices().get(r.nextInt(graph.getNbVertices()));
		startTime=System.nanoTime();
		Vertex[] pred = SpanningAlgorithms.prim(graph,randomVertex);
		resultTime = (System.nanoTime()-startTime)/nanoToMS;
		SpanningAlgorithms.displayPrim(graph,pred);
		System.out.println("Time : "+resultTime);


		//Prim Fibo
		System.out.println("\nResults for Prim (Fibonacci) :");
		startTime=System.nanoTime();
		pred = SpanningAlgorithms.primFibo(graph,randomVertex);
		resultTime = (System.nanoTime()-startTime)/nanoToMS;
		SpanningAlgorithms.displayPrim(graph,pred);
		System.out.println("Time : "+resultTime);
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
