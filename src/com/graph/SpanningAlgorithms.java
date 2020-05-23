package com.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.concurrent.RunnableScheduledFuture;

public class SpanningAlgorithms {
	private SpanningAlgorithms(){}


	// Implementation of Union-find structure defined in the wikipedia page : https://fr.wikipedia.org/wiki/Union-find
	private static class subset{
		int parent;
		int rank;
	}

	private static int find(subset [] subsets , int i)
	{
		if (subsets[i].parent != i)
			subsets[i].parent = find(subsets,
					subsets[i].parent);
		return subsets[i].parent;
	}

	static void union(subset [] subsets,
			   int x , int y )
	{
		int xroot = find(subsets, x);
		int yroot = find(subsets, y);

		if (subsets[xroot].rank < subsets[yroot].rank)
			subsets[xroot].parent = yroot;
		else if (subsets[yroot].rank < subsets[xroot].rank)
			subsets[yroot].parent = xroot;
		else
		{
			subsets[xroot].parent = yroot;
			subsets[yroot].rank++;
		}
	}

	private static subset makeSet(int id){
		subset subset= new subset();
		subset.parent = id;
		subset.rank = 0;
		return subset;
	}

	private static subset[] makeSets(Graph graph){
		int V = graph.nbVertices;
		subset [] subsets = new subset[V];
		for (int v = 0; v < V; v++)
			subsets[v]=makeSet(v);
		return subsets;
	}

	private static int isCycle(Graph graph)
	{
		int E = graph.nbEdges;
		for (int e = 0; e < E; e++)
		{
			subset[] subsets = makeSets(graph);
			Edge edge = graph.listEdges.get(e);
			int x = find(subsets, edge.getIndexInitialVertex());
			int y = find(subsets, edge.getIndexFinalVertex());
			if(x == y)
				return 1;
			union(subsets, x, y);
		}
		return 0;
	}

	/**
	 * 	Implementation of the Kruskal algorithm (v1)
	 */
	public static ArrayList<Edge> Kruskal1(Graph graph){
		subset[] subsets = makeSets(graph);
		ArrayList<Edge> result = new ArrayList<>();
		ArrayList<Edge> order = (ArrayList<Edge>) graph.getListEdges().clone();
		order.sort((Comparator.comparingDouble(Edge::getFirstValue)));
		for(Edge edge:order){
			if(find(subsets,edge.getIndexInitialVertex())!=find(subsets,edge.getIndexFinalVertex())){
				union(subsets,edge.getIndexInitialVertex(),edge.getIndexFinalVertex());
				result.add(edge);
			}
		}
		return result;
	}

	/**
	 * 	Implementation of the Kruskal algorithm (v2)
	 */
	public static ArrayList<Edge> Kruskal2(Graph graph) {
		subset[] subsets = makeSets(graph);
		ArrayList<Edge> result = new ArrayList<>();
		ArrayList<Edge> order = (ArrayList<Edge>) graph.getListEdges().clone();
		order.sort((o1, o2) -> (int) (o2.getFirstValue()-o1.getFirstValue()));
		ArrayList<Edge> T = (ArrayList<Edge>) graph.getListEdges().clone();
		int i=0;
		int n=graph.nbVertices;
		while(T.size()>=n){
			T.remove(order.get(i));
			Graph tempGraph = new Graph((ArrayList<Vertex>)graph.listVertices.clone(),T,(ArrayList<LinkedList<Integer>>)graph.listAdjacent.clone());
			if(!isConnected(tempGraph))
				T.add(order.get(i));
		}
		return T;
	}

	private static void traverse(Graph graph,int vID,boolean[] visited){
		visited[vID]=true;
		for(int edgeID:graph.listAdjacent.get(vID)){
				Edge edge = graph.listEdges.get(edgeID);
				int neighbourID;
				if (edge.getIndexInitialVertex() == vID) neighbourID = edge.getIndexFinalVertex();
				else neighbourID = edge.getIndexInitialVertex();
				if(!visited[neighbourID])
					traverse(graph,neighbourID,visited);
		}
	}

	public static boolean isConnected(Graph graph){
		for(Vertex vertex:graph.listVertices){
			int vID = vertex.getId();
			boolean[] visited = new boolean[graph.nbVertices];
			Arrays.fill(visited,false);
			traverse(graph,vID,visited);
			for(int i=0;i<graph.nbVertices;i++){
				if(!visited[i])
					return false;
			}
		}
		return true;
	}

	public static void displayAlgorithmsResults(ArrayList<Edge> result){
		StringBuilder res = new StringBuilder("Displaying results :\n");
		res.append("---\nList of edges :\n");
		for(Edge edge:result){
			double[] values = edge.getValues();
			res.append(edge.getId()).append(" (").append(edge.getIndexInitialVertex()).append(",").append(edge.getIndexFinalVertex()).append(") [ ");
			for(int i=0;i<edge.getNbValues();i++){
				res.append(values[i]).append(" ");
			}
			res.append("]\n");
		}
		res.append("---\n");
		res.append("Total weight = ").append(getWeight(result));
		res.append("\n---\n");
		System.out.println(res);
	}

	public static double getWeight(ArrayList<Edge> edges){
		double res = 0;
		for(Edge e:edges)
			res+=e.getValues()[0];
		return res;
	}
}
