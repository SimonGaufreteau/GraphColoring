package com;

import com.graph.Algorithms;
import com.graph.Graph;
import com.graph.Vertex;
import com.graphParser.GraphParser;
import org.w3c.dom.ls.LSResourceResolver;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Main {
	private static final double nanoToMS =Math.pow(10,6);

	public static void main(String[] args) throws Exception {


		if(args.length!=1){
			throw new Exception("Args length must be 1. Only the path to the test directory containing .col files is accepted as an argument.");
		}
		PrintStream printStream = new PrintStream(args[0]+"/DSATURDecreasing.csv");
		System.setOut(printStream);
		//Random CSV output
		//System.out.println("Graph,Max color,Min color,Average color,Max time,Min time,Average Time");

		//One test CSV output
		System.out.println("Graph,Color,Time");
		try (Stream<Path> walk = Files.walk(Paths.get(args[0]))) {
			List<String> result = walk.map(Path::toString)
					.filter(f -> f.endsWith(".col")).collect(Collectors.toList());

			for(String fileName:result){
				Graph graph= GraphParser.getGraphFromFile(fileName);
				//Standard output
				//System.out.println("Testing on : "+fileName);

				//CSV output
				System.out.print(new File(fileName).getName()+",");
				testDsatur(graph);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void testDsatur(Graph graph){
		long startTime=System.nanoTime();
		Map<Integer, Integer> colorMap = Algorithms.dsatur(graph);
		double resultTime = (System.nanoTime()-startTime)/nanoToMS;
		int numberOfColors = Algorithms.getMaxColor(colorMap)+1;
		System.out.println(numberOfColors+","+resultTime);
	}


	private static void testDecreasing(Graph graph){
		ArrayList<Vertex> order = (ArrayList<Vertex>) graph.getListVertices().clone();
		order.sort(((o1, o2) -> o2.getDegree()-o1.getDegree()));
		long startTime=System.nanoTime();
		Map<Integer, Integer> colorMap = Algorithms.welshPowell(graph, order);
		double resultTime = (System.nanoTime()-startTime)/nanoToMS;
		int numberOfColors = Algorithms.getMaxColor(colorMap)+1;
		System.out.println(numberOfColors+","+resultTime);

	}

	private static void testIncreasing(Graph graph){
		ArrayList<Vertex> order = (ArrayList<Vertex>) graph.getListVertices().clone();
		order.sort((Comparator.comparingInt(Vertex::getDegree)));
		long startTime=System.nanoTime();
		Map<Integer, Integer> colorMap = Algorithms.welshPowell(graph, order);
		double resultTime = (System.nanoTime()-startTime)/nanoToMS;
		int numberOfColors = Algorithms.getMaxColor(colorMap)+1;
		System.out.println(numberOfColors+","+resultTime);

	}

	private static void testRandom(Graph graph){
		ArrayList<Vertex> order = (ArrayList<Vertex>) graph.getListVertices().clone();
		long[] colorTab = new long[100];
		double[] timeToExec = new double[100];
		for(int i=0;i<100;i++){
			Collections.shuffle(order);
			long startTime=System.nanoTime();
			Map<Integer, Integer> colorMap = Algorithms.welshPowell(graph, order);
			double resultTime = (System.nanoTime()-startTime)/nanoToMS;
			int numberOfColors = Algorithms.getMaxColor(colorMap)+1;
			colorTab[i]=numberOfColors;
			timeToExec[i]=resultTime;
		}

		long maxColor = Arrays.stream(colorTab).max().orElse(0);
		long minColor = Arrays.stream(colorTab).min().orElse(0);
		double averageColor = Arrays.stream(colorTab).average().orElse(0);

		double maxTime = Arrays.stream(timeToExec).max().orElse(0);
		double minTime = Arrays.stream(timeToExec).min().orElse(0);
		double averageTime = Arrays.stream(timeToExec).average().orElse(0);
		/*
		//Standard output
		System.out.println("Results of the greedy Algorithm (100 tests):");
		System.out.println("Average number of colors : "+Arrays.stream(colorTab).average().orElse(0));
		System.out.println("Max color found : "+Arrays.stream(colorTab).max().orElse(0));
		System.out.println("Min color found : "+Arrays.stream(colorTab).min().orElse(0));
		System.out.println("\nAverage time to execute the algorithm : "+Arrays.stream(timeToExec).average().orElse(0)+"ms");
		System.out.println("Max time to execute the algorithm : "+Arrays.stream(timeToExec).max().orElse(0)+"ms");
		System.out.println("Min time to execute the algorithm : "+Arrays.stream(timeToExec).min().orElse(0)+"ms\n");
		System.out.println("--------------------");*/

		//CSV output
		System.out.println(maxColor+","+minColor+","+averageColor+","+maxTime+","+minTime+","+averageTime);

	}
	private static void randomTests(Graph graph){
		System.out.println("\nGreedy :\n");

		ArrayList<Vertex> order = (ArrayList<Vertex>) graph.getListVertices().clone();
		long startTime = System.nanoTime();
		Map<Integer,Integer> colorMap = Algorithms.greedyColoring(graph,order);
		System.out.println("Time taken to execute the algorithm : "+(System.nanoTime()-startTime)/nanoToMS+"ms");

		//Algorithms.displayColoringResult(colorMap);
		System.out.println("Checking coloring : " +(Algorithms.checkColoring(graph,colorMap)?"valid":"invalid"));
		System.out.println("Max color : "+Algorithms.getMaxColor(colorMap));

		System.out.println("\nShuffling the order...\n");
		Collections.shuffle(order);
		startTime=System.nanoTime();
		colorMap= Algorithms.greedyColoring(graph,order);
		System.out.println("Time taken to execute the algorithm : "+(System.nanoTime()-startTime)/nanoToMS+"ms");

		//Algorithms.displayColoringResult(colorMap);
		System.out.println("Checking coloring : " +(Algorithms.checkColoring(graph,colorMap)?"valid":"invalid"));
		System.out.println("Max color : "+Algorithms.getMaxColor(colorMap));

		System.out.println("\nSorting the order...\n");
		order.sort((o1, o2) -> o2.getDegree()-o1.getDegree());
		System.out.println(Arrays.toString(order.toArray()));

		startTime=System.nanoTime();
		colorMap= Algorithms.greedyColoring(graph,order);
		System.out.println("Time taken to execute the algorithm : "+(System.nanoTime()-startTime)/nanoToMS+"ms");
		System.out.println("Checking coloring : " +(Algorithms.checkColoring(graph,colorMap)?"valid":"invalid"));
		System.out.println("Max color : "+Algorithms.getMaxColor(colorMap));


		System.out.println("\nWelsh Powell :\n");
		startTime=System.nanoTime();
		colorMap= Algorithms.welshPowell(graph,order);
		System.out.println("Time taken to execute the algorithm : "+(System.nanoTime()-startTime)/nanoToMS+"ms");

		//Algorithms.displayColoringResult(colorMap);
		System.out.println("Checking coloring : " +(Algorithms.checkColoring(graph,colorMap)?"valid":"invalid"));
		System.out.println("Max color : "+Algorithms.getMaxColor(colorMap));

		System.out.println("\nDSATUR :\n");
		startTime=System.nanoTime();
		colorMap= Algorithms.dsatur(graph);
		System.out.println("Time taken to execute the algorithm : "+(System.nanoTime()-startTime)/nanoToMS+"ms");
		//Algorithms.displayColoringResult(colorMap);
		System.out.println("Checking coloring : " +(Algorithms.checkColoring(graph,colorMap)?"valid":"invalid"));
		System.out.println("Max color : "+Algorithms.getMaxColor(colorMap));
	}


}
