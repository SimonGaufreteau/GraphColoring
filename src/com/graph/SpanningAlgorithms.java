package com.graph;

import java.lang.reflect.Executable;
import java.time.Year;
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

	/**
	 * Not used here : detects a cycle in a graph using find-union
	 * @param graph
	 * @return
	 */
	public static int isCycle(Graph graph)
	{
		int E = graph.nbEdges;
		for (int e = 0; e < E; e++)
		{
			subset[] subsets = makeSets(graph);
			Edge edge = graph.listEdges.get(e);
			int x = find(subsets, edge.getIndexInitialVertex());
			int y = find(subsets, edge.getIndexFinalVertex());
			if(x == y){
				System.out.println(subsets);
				return 1;
			}
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


	/**
	 * Enum used in the DMST algorithm to tag vertices
	 */
	private enum status {GOOD,MEDIUM,BAD};

	/**
	* Degree-constrained Minimum Spanning Tree heuristic proposition
	* @deprecated : Not functionnal
	 */
	public static ArrayList<Edge> dmst(Graph graph,int k) {
		Random random = new Random();
		ArrayList<Edge> spanningTree = constructSpanningTree(graph);
		Graph tempGraph = new Graph((ArrayList<Vertex>) graph.listVertices.clone(), spanningTree, (ArrayList<LinkedList<Integer>>) graph.listAdjacent.clone());
		tempGraph.updateAdjacent();

		status[] statuses = new status[graph.getNbVertices()];
		//Phase 1 : Initializing status for all vertices
		int badCount = updateBadVertices(tempGraph, statuses, k);
		while (badCount>0) {
			ArrayList<Integer> goodEdgesIDs = new ArrayList<>();

			//Checking GOOD edges (both vertices are marked as GOOD)
			for (int i = 0; i < graph.getNbVertices(); i++) {
				for (int j = 0; j < graph.getNbVertices(); j++) {
					if (statuses[i] == status.GOOD && statuses[j] == status.GOOD) {
						LinkedList<Integer> adj = graph.getListAdjacent().get(i);
						//If both vertices are marked as good, looping over adjacent to i to find if there exist an edge between i and j
						for (int edgeID : adj) {
							Edge edge = graph.listEdges.get(edgeID);
							int neighbourID;
							if (edge.getIndexInitialVertex() == i) neighbourID = edge.getIndexFinalVertex();
							else neighbourID = edge.getIndexInitialVertex();

							//If we find a link between i and j, add the edge to the list of 'good' edges. Else continue to search for a link.
							//If no link are found, pass to the next combination of vertices
							if (j == neighbourID) {
								goodEdgesIDs.add(edgeID);
								break;
							}
						}//end of adj loop
					}
				}//end of j loop
			}//end of i loop

			if (goodEdgesIDs.size() == 0) {
				Edge defaultEdge;
				do {
					defaultEdge = graph.getListEdges().get(random.nextInt(graph.nbEdges));
				} while (spanningTree.contains(defaultEdge));
				goodEdgesIDs.add(defaultEdge.getId());
			}

			//Phase 2 : While there are BAD vertices && an arc between GOOD vertices
			while (badCount > 0 && goodEdgesIDs.size() > 0) {
				//Phase 2.a : Taking an arc between GOOD vertices (in goodEdgesIDs) and add it to the spanning tree
				int edgeID = goodEdgesIDs.remove(random.nextInt(goodEdgesIDs.size()));
				Edge edge = graph.getListEdges().get(edgeID);
				spanningTree.add(edge);
				tempGraph.nbEdges++;
				tempGraph.updateAdjacent();

				//Phase 2.b : Deleting the worst edge in the cycle
				//Retrieving the cycle from the getCycle method
				ArrayList<Edge> bridgeEdges = bridge(tempGraph, graph.getListEdges());
				ArrayList<Edge> cycleEdges = getCycleEdgeFromBridge(spanningTree, bridgeEdges);
				Edge toBeRemoved = getWorstEdgeFromCycle(cycleEdges, statuses);

				//Removing the edge with the most value
				spanningTree.remove(toBeRemoved);

				//Phase 2.c : Setting the BAD vertices in the cycle to GOOD (May be optimized)
				for (Edge cycleEdge : cycleEdges) {
					int firstID = cycleEdge.getIndexInitialVertex();
					int secondID = cycleEdge.getIndexFinalVertex();
					if (statuses[firstID] == status.BAD)
						badCount--;
					if (statuses[secondID] == status.BAD)
						badCount--;
					statuses[firstID] = status.GOOD;
					statuses[secondID] = status.GOOD;
				}
			}
			tempGraph.updateDegrees();
			//Phase 3 : updating BAD vertices and going back to phase 2 if their are still at least one
			badCount = updateBadVertices(tempGraph, statuses, k);
			//System.out.println(badCount);
		}
		int degree = tempGraph.getMaxDegree();
		System.out.println("Degree : "+degree);
		return spanningTree;
	}

	private static Edge getWorstEdgeFromCycle(ArrayList<Edge> cycleEdges,status[] statuses) {
		double maxValue = -1;
		Edge worstEdge=null;
		for(Edge cycleEdge : cycleEdges){
			if(statuses[cycleEdge.getIndexInitialVertex()]==status.BAD || statuses[cycleEdge.getIndexFinalVertex()]==status.BAD){
				if(cycleEdge.getFirstValue()>maxValue){
					maxValue=cycleEdge.getFirstValue();
					worstEdge=cycleEdge;
				}
				else if (worstEdge==null)
					worstEdge=cycleEdge;
			}
		}
		if (worstEdge==null){
			Random random=new Random();
			worstEdge=cycleEdges.get(random.nextInt(cycleEdges.size()));
		}
		return worstEdge;
	}

	private static ArrayList<Edge> getCycleEdgeFromBridge(ArrayList<Edge> spanningTree, ArrayList<Edge> bridgeEdges) {
		ArrayList<Edge> result = (ArrayList<Edge>) spanningTree.clone();
		for(Edge bridge:bridgeEdges)
			result.remove(bridge);
		return result;
	}

	private static ArrayList<Edge> constructSpanningTree(Graph graph) {
		return Kruskal1(graph);
	}

	// A recursive function that finds and prints bridges
	// using DFS traversal
	// u --> The vertex to be visited next
	// visited[] --> keeps tract of visited vertices
	// disc[] --> Stores discovery times of visited vertices
	// parent[] --> Stores parent vertices in DFS tree
	private static void bridgeUtil(int u, boolean[] visited, int[] disc,
						   int[] low, int[] parent,Graph graph,ArrayList<Edge> allEdges,int time,ArrayList<Edge> result)
	{
		ArrayList<LinkedList<Integer>> adj = graph.getListAdjacent();
		// Mark the current node as visited
		visited[u] = true;

		// Initialize discovery time and low value
		disc[u] = low[u] = ++time;

		// Go through all vertices adjacent to this (edgesID at first)
		for (int edgeID : adj.get(u)) {
			Edge edge = allEdges.get(edgeID);
			int v;
			if (edge.getIndexInitialVertex() == u) v = edge.getIndexFinalVertex();
			else v = edge.getIndexInitialVertex();
			// v is current adjacent of u

			// If v is not visited yet, then make it a child
			// of u in DFS tree and recur for it.
			// If v is not visited yet, then recur for it
			if (!visited[v]) {
				parent[v] = u;
				bridgeUtil(v, visited, disc, low, parent, graph, allEdges,time,result);

				// Check if the subtree rooted with v has a
				// connection to one of the ancestors of u
				low[u] = Math.min(low[u], low[v]);

				// If the lowest vertex reachable from subtree
				// under v is below u in DFS tree, then u-v is
				// a bridge
				if (low[v] > disc[u])
					result.add(edge);
			}

			// Update low value of u for parent function calls.
			else if (v != parent[u])
				low[u] = Math.min(low[u], disc[v]);
		}
	}


	// DFS based function to find all bridges. It uses recursive
	// function bridgeUtil()
	private static ArrayList<Edge> bridge(Graph graph, ArrayList<Edge> allEdges)
	{
		int V = graph.getNbVertices();
		// Mark all the vertices as not visited
		boolean[] visited = new boolean[V];
		int[] disc = new int[V];
		int[] low = new int[V];
		int[] parent = new int[V];
		ArrayList<Edge> result = new ArrayList<>();

		// Initialize parent and visited, and ap(articulation point)
		// arrays
		for (int i = 0; i < V; i++)
		{
			parent[i] = -1;
			visited[i] = false;
		}


		// Call the recursive helper function to find Bridges
		// in DFS tree rooted with vertex 'i'
		for (int i = 0; i < V; i++){
			if (!visited[i])
				bridgeUtil(i, visited, disc, low, parent,graph,allEdges,0,result);
		}

		return result;

	}


	private static int updateBadVertices(Graph graph,status[] statuses,int k){
		int badCount=0;
		for(Vertex vertex:graph.getListVertices()){
			int degree = vertex.getDegree();
			int id = vertex.getId();
			if(degree>=k){
				statuses[id] = status.BAD;
				badCount++;
			}
			else if (degree==k-1)
				statuses[id] = status.MEDIUM;
			else
				statuses[id]=status.GOOD;
		}
		return badCount;
	}


	/**
	 * DMST using prim algorithm
	 */
	public static ArrayList<Edge> primDMST(Graph graph, int k) throws NullPointerException{
		Random random = new Random();
		Vertex x = graph.getListVertices().get(random.nextInt(graph.getNbVertices()));
		ArrayList<Vertex> R = new ArrayList<>();
		R.add(x);
		ArrayList<Vertex> XR = (ArrayList<Vertex>) graph.getListVertices().clone();
		XR.remove(x);

		ArrayList<Edge> T = new ArrayList<>();
		int[] degree = new int[graph.getNbVertices()];

		ArrayList<Edge> U = (ArrayList<Edge>) graph.getListEdges().clone();
		while(R.size()!=graph.nbVertices){
			Edge minEdge=null;
			Vertex z=null;
			Vertex y=null;
			double minValue=Double.POSITIVE_INFINITY;
			for (Vertex vertex : R) {
				int vID = vertex.getId();
				LinkedList<Integer> adj = graph.getListAdjacent().get(vID);
				for(int nextID:adj){
					Edge edge = graph.getListEdges().get(nextID);
					if(!U.contains(edge))
						continue;
					int neighbourID;
					if (edge.getIndexInitialVertex() == vID) neighbourID = edge.getIndexFinalVertex();
					else neighbourID = edge.getIndexInitialVertex();
					Vertex neighbour = graph.getListVertices().get(neighbourID);
					if(XR.contains(neighbour)){
						if(edge.getFirstValue()<minValue){
							minValue=edge.getFirstValue();
							minEdge=edge;
							z=neighbour;
							y=vertex;
						}
					}
				}
			}

			if(minEdge==null){
				throw new NullPointerException();
			}

			if(degree[y.getId()]<k && degree[z.getId()]<k){
				degree[y.getId()]++;
				degree[z.getId()]++;
				T.add(minEdge);
				R.add(z);
				XR.remove(z);
			}
			else
				U.remove(minEdge);
		}
		return T;
	}



	public static void displayKruskal(ArrayList<Edge> result){
		StringBuilder res = new StringBuilder();
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
