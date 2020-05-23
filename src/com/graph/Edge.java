package com.graph;

import java.util.Arrays;

/**
 * A simple Edge implementation for the {@link Graph} class.
 */
public class Edge {
	private int id;
	private int indexInitialVertex;
	private int indexFinalVertex;
	private String nameFinalVertex;
	private String nameInitialVertex;
	private int nbValues;
	private double[] values;

	public Edge(int id,int indexInitialVertex,int indexFinalVertex,String nameInitialVertex,String nameFinalVertex,int nbValues,double[] values){
		this.id=id;
		this.indexFinalVertex=indexFinalVertex;
		this.indexInitialVertex=indexInitialVertex;
		this.nameFinalVertex=nameFinalVertex;
		this.nameInitialVertex=nameInitialVertex;
		this.nbValues=nbValues;
		this.values=values;
	}

	public int getId() {
		return id;
	}

	public int getIndexInitialVertex() {
		return indexInitialVertex;
	}

	public int getIndexFinalVertex() {
		return indexFinalVertex;
	}

	public String getNameFinalVertex() {
		return nameFinalVertex;
	}

	public String getNameInitialVertex() {
		return nameInitialVertex;
	}

	public int getNbValues() {
		return nbValues;
	}

	public double[] getValues() {
		return values;
	}

	public double getFirstValue(){
		if(nbValues>0)
			return values[0];
		return 0;
	}
	@Override
	public String toString() {
		return "Edge{" +
				"id=" + id +
				", indexInitialVertex=" + indexInitialVertex +
				", indexFinalVertex=" + indexFinalVertex +
				", nameFinalVertex='" + nameFinalVertex + '\'' +
				", nameInitialVertex='" + nameInitialVertex + '\'' +
				", nbValues=" + nbValues +
				", values=" + Arrays.toString(values) +
				'}';
	}
}
