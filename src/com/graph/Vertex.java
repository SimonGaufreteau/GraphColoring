package com.graph;

import java.util.Arrays;

public class Vertex {
	private int id;
	private String name;
	private int degree;
	private int degreePos;
	private int color;
	private int nbValues;
	private double[] values;

	public Vertex(int id,String name,int nbValues,double[] values){
		this.id=id;
		this.name=name;
		this.nbValues=nbValues;
		this.values=values;
		this.color=0;
		this.degree=0;
		this.degreePos=0;
	}

	public void incrementDegree(){
		this.degree++;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getDegree() {
		return degree;
	}

	public int getDegreePos() {
		return degreePos;
	}

	public int getColor() {
		return color;
	}

	public int getNbValues() {
		return nbValues;
	}

	public double[] getValues() {
		return values;
	}


	@Override
	public String toString() {
		return "Vertex{" +
				"id=" + id +
				", name='" + name + '\'' +
				", degree=" + degree +
				", degreePos=" + degreePos +
				", color=" + color +
				", nbValues=" + nbValues +
				", values=" + Arrays.toString(values) +
				'}';
	}
}
