package com.graph;

import java.util.*;

public class SpanningAlgorithms {
	private static Random random=new Random();
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

	public static int isCycle(Graph graph)
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
		ArrayList<Edge> order = (ArrayList<Edge>) graph.getListEdges().clone();
		order.sort((o1, o2) -> (int) (o2.getFirstValue()-o1.getFirstValue()));
		ArrayList<Edge> T = (ArrayList<Edge>) graph.getListEdges().clone();
		int i=0;
		int n=graph.nbVertices;
		int size= T.size();
		Graph tempGraph = new Graph((ArrayList<Vertex>) graph.listVertices.clone(), T, (ArrayList<LinkedList<Integer>>) graph.listAdjacent.clone());
		while(size>=n){
			//T.remove(order.get(i));
			Edge temp = T.set(order.get(i).getId(),null);
			size--;
			if(!isConnected(tempGraph)){
				T.set(temp.getId(),temp);
				size++;
			}
			i++;
		}
		return T;
	}

	private static void traverse(Graph graph,int vID,boolean[] visited){
		visited[vID]=true;
		for(int edgeID:graph.listAdjacent.get(vID)){
				Edge edge = graph.listEdges.get(edgeID);
				if(edge==null) continue;
				int neighbourID;
				if (edge.getIndexInitialVertex() == vID) neighbourID = edge.getIndexFinalVertex();
				else neighbourID = edge.getIndexInitialVertex();
				if(!visited[neighbourID])
					traverse(graph,neighbourID,visited);
		}
	}

	public static boolean isConnected(Graph graph){
		Vertex vertex = graph.listVertices.get(random.nextInt(graph.getNbVertices()));
		int vID = vertex.getId();
		boolean[] visited = new boolean[graph.nbVertices];
		Arrays.fill(visited,false);
		traverse(graph,vID,visited);
		for(int i=0;i<graph.nbVertices;i++){
			if(!visited[i])
				return false;
		}

		return true;
	}


	/**
	 * Prim Algorithm with a simple priorityQueue
	 */
	public static Vertex[] prim(Graph graph, Vertex v){
		PriorityQueue<Vertex> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(Vertex::getPriority));
		Vertex[] pred = new Vertex[graph.getNbVertices()];

		//Init for the queue and the pred vertices
		for(Vertex vertex:graph.listVertices){
			if(vertex.getId()==v.getId())continue;
			vertex.setPriority(Double.POSITIVE_INFINITY);
			priorityQueue.offer(vertex);
			pred[vertex.getId()]=null;
		}

		//Starting with v --> priority=0
		v.setPriority(0);
		priorityQueue.offer(v);

		//Continuing while elements to check
		while(!priorityQueue.isEmpty()){
			Vertex u = priorityQueue.poll();
			int vID=u.getId();

			//Looping over adjacent edges/vertices
			for(int nextID:graph.listAdjacent.get(vID)){
				//Getting the neighbour ID
				Edge edge = graph.listEdges.get(nextID);
				int neighbourID;
				if (edge.getIndexInitialVertex() == vID) neighbourID = edge.getIndexFinalVertex();
				else neighbourID = edge.getIndexInitialVertex();
				Vertex neighbour = graph.listVertices.get(neighbourID);

				//Checking for an update
				if(priorityQueue.contains(neighbour) && edge.getFirstValue()<=neighbour.getPriority()){
					//Saving the predecessor
					pred[neighbourID]=u;

					//Updating the queue
					priorityQueue.remove(neighbour);
					neighbour.setPriority(edge.getFirstValue());
					priorityQueue.offer(neighbour);
				}
			}
		}
		return pred;
	}


	/**
	 * Prim algorithm with a FibonacciHeap
	 */
	public static Vertex[] primFibo(Graph graph, Vertex v){
		FibonacciHeap<Vertex> fibonacciHeap = new FibonacciHeap<>();
		Vertex[] pred = new Vertex[graph.getNbVertices()];
		boolean[] visited = new boolean[graph.nbVertices];

		//Used to save the references in the heap to avoid any search
		FibonacciHeap.Entry<Vertex>[] visitedVertices = new FibonacciHeap.Entry[graph.nbVertices];

		//Init for the queue and the pred vertices
		for(Vertex vertex:graph.listVertices){
			int id =vertex.getId();
			if(id==v.getId())continue;

			//Saving the reference to the node
			visitedVertices[id]=fibonacciHeap.enqueue(vertex,Double.POSITIVE_INFINITY);

			pred[id]=null;
			visited[id]=false;
		}

		//Starting with v --> priority=0
		fibonacciHeap.enqueue(v,0d);

		//Continuing while elements to check
		while(!fibonacciHeap.isEmpty()){

			//Extract-min
			FibonacciHeap.Entry<Vertex> entry = fibonacciHeap.dequeueMin();
			Vertex u = entry.getValue();
			int vID=u.getId();
			visited[vID]=true;

			//Looping over adjacent edges/vertices
			for(int nextID:graph.listAdjacent.get(vID)){
				//Getting the neighbour ID
				Edge edge = graph.listEdges.get(nextID);
				int neighbourID;
				if (edge.getIndexInitialVertex() == vID) neighbourID = edge.getIndexFinalVertex();
				else neighbourID = edge.getIndexInitialVertex();
				Vertex neighbour = graph.listVertices.get(neighbourID);

				//Checking for an update
				if(!visited[neighbourID] && edge.getFirstValue()<=neighbour.getPriority()){
					//Saving the predecessor
					pred[neighbourID]=u;
					//Updating the queue
					fibonacciHeap.decreaseKey(visitedVertices[neighbourID],edge.getFirstValue());
				}
			}
		}
		return pred;
	}



	public static void displayKruskal(ArrayList<Edge> result){
		StringBuilder res = new StringBuilder();
		/*res.append("---\nList of edges :\n");
		for(Edge edge:result){
			double[] values = edge.getValues();
			res.append(edge.getId()).append(" (").append(edge.getIndexInitialVertex()).append(",").append(edge.getIndexFinalVertex()).append(") [ ");
			for(int i=0;i<edge.getNbValues();i++){
				res.append(values[i]).append(" ");
			}
			res.append("]\n");
		}*/
		res.append("---\n");
		res.append("Total weight = ").append(getWeight(result));
		res.append("\n---");
		System.out.println(res);
	}

	public static void displayPrim(Graph graph,Vertex[] result){
		StringBuilder res = new StringBuilder();
		res.append("---\nList of Vertices :\n");
		for (Vertex vertex : result) {
			if (vertex != null)
				res.append(vertex.getId());
			else
				res.append("null");
			res.append(" --> ");
		}
		res.append("End");
		res.append("---\n");
		res.append("Total weight = ").append(getWeight(graph,result));
		res.append("\n---");
		System.out.println(res);
	}

	private static double getWeight(Graph graph,Vertex[] result) {
		ArrayList<LinkedList<Integer>> adj = graph.getListAdjacent();
		double weight = 0;
		for (int i = 0; i < result.length; i++) {
			if(result[i]==null)
				continue;
			int vID=result[i].getId();
			LinkedList<Integer> edges = adj.get(vID);
			for (int nextID : edges) {
				Edge edge = graph.listEdges.get(nextID);
				int neighbourID;
				if (edge.getIndexInitialVertex() == vID) neighbourID = edge.getIndexFinalVertex();
				else neighbourID = edge.getIndexInitialVertex();
				if(neighbourID==i){
					weight+=edge.getFirstValue();
					break;
				}
			}
		}
		return weight;
	}

	public static double getWeight(ArrayList<Edge> edges){
		double res = 0;
		for(Edge e:edges)
			res+=e.getValues()[0];
		return res;
	}
}
