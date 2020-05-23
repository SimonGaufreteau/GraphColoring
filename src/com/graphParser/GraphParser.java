package com.graphParser;

import com.graph.Edge;
import com.graph.Graph;
import com.graph.Vertex;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class GraphParser {

	private GraphParser(){}

	/**
	 * Returns a graph from a file path. The file must contain the following elements : a line starting with p describing the Graph. Ex : e 150 2500 (150 : number of vertices, 2500 : number of edges)
	 * Lines starting with e describing an edge. Ex : e 10 12 (edge between the vertices 10 and 12).
	 */
	public static Graph getGraphFromFile(String filepath) throws Exception {
		File f = new File(filepath);
		BufferedReader br = new BufferedReader(new FileReader(f));

		String name=f.getName();
		boolean isDirected=false;
		int nbVertices=0;
		int nbEdges=0;
		int nbVertexValues=0;
		int nbEdgesValues=0;
		ArrayList<Vertex> listVertices=new ArrayList<>();
		ArrayList<Edge> listEdges = new ArrayList<>();
		ArrayList<LinkedList<Integer>> listAdjacent=new ArrayList<>();

		String[] splitLine;
		String line = br.readLine();
		int edgeCounter=0;
		while (line!=null){
			switch(line.charAt(0)){
				case 'c':
					line=br.readLine();
					continue;
				case 'p' :
					splitLine = line.split(" ");
					if(splitLine.length<3) throw new Exception("Incorrect line. Split length of the 'p' line must be 3");
					int n = 1;
					String extension= name.split("[.]")[1];
					if(extension.equals("col"))
						n++;
					nbVertices=Integer.parseInt(splitLine[n]);
					nbEdges=Integer.parseInt(splitLine[n]);

					for(int i=0;i<nbVertices;i++){
						Vertex tempVertex = new Vertex(i,"",nbVertexValues,null);
						listVertices.add(tempVertex);
						listAdjacent.add( new LinkedList<>());
					}
					break;
				case 'e':
					splitLine = line.split(" ");
					if(splitLine.length<3) throw new Exception("Incorrect line. Split length of the 'e' line must be 3");
					int initialID= Integer.parseInt(splitLine[1])-1;
					int finalID=Integer.parseInt(splitLine[2])-1;
					double[] values=null;
					if(splitLine.length>3){
						nbEdgesValues=splitLine.length-3;
						values = new double[splitLine.length-3];
						for(int i=3;i<splitLine.length;i++){
							values[i-3]= Double.parseDouble(splitLine[i]);
						}
					}
					Edge edge = new Edge(edgeCounter,initialID,finalID,"","",nbEdgesValues,values);
					listEdges.add(edge);
					listVertices.get(initialID).incrementDegree();
					listVertices.get(finalID).incrementDegree();
					listAdjacent.get(initialID).add(edge.getId());
					listAdjacent.get(finalID).add(edge.getId());
					edgeCounter ++;
					break;

			}
			line=br.readLine();

		}
		return new Graph(name,isDirected,nbVertices,nbEdges,nbVertexValues,nbEdgesValues,listVertices,listEdges,listAdjacent);
	}
}
