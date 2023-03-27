/* 21067625
 *
 * Optionally, if you have any comments regarding your submission, put them here.
 * For instance, specify here if your program does not generate the proper output or does not do it in the correct manner.
 */

import java.util.*;
import java.io.*;

class Vertex {

	// Constructor: set name, chargingStation and index according to given values,
	// initilaize incidentRoads as empty array
	public Vertex(String placeName, boolean chargingStationAvailable, int idx) {
		name = placeName;
		incidentRoads = new ArrayList<Edge>();
		index = idx;
		chargingStation = chargingStationAvailable;
	}

	public String getName() {
		return name;
	}

	public boolean hasChargingStation() {
		return chargingStation;
	}

	public ArrayList<Edge> getIncidentRoads() {
		return incidentRoads;
	}

	// Add a road to the array incidentRoads
	public void addIncidentRoad(Edge road) {
		incidentRoads.add(road);
	}

	public int getIndex() {
		return index;
	}

	private String name; // Name of the place
	private ArrayList<Edge> incidentRoads; // Incident edges
	private boolean chargingStation; // Availability of charging station
	private int index; // Index of this vertex in the vertex array of the map
}

class Edge {
	public Edge(int roadLength, Vertex firstPlace, Vertex secondPlace) {
		length = roadLength;
		incidentPlaces = new Vertex[] { firstPlace, secondPlace };
	}

	public Vertex getFirstVertex() {
		return incidentPlaces[0];
	}

	public Vertex getSecondVertex() {
		return incidentPlaces[1];
	}

	public int getLength() {
		return length;
	}

	private int length;
	private Vertex[] incidentPlaces;
}

// A class that represents a sparse matrix
public class RoadMap {

	// Default constructor
	public RoadMap() {
		places = new ArrayList<Vertex>();
		roads = new ArrayList<Edge>();
	}

	// Auxiliary function that prints out the command syntax
	public static void printCommandError() {
		System.err.println("ERROR: use one of the following commands");
		System.err.println(" - Load a map and print information:");
		System.err.println("      java RoadMap -i <MapFile>");
		System.err.println(" - Load a map and determine if two places are connnected by a path with charging stations:");
		System.err.println("      java RoadMap -c <MapFile> <StartVertexIndex> <EndVertexIndex>");
		System.err.println(" - Load a map and determine the mininmum number of assistance cars required:");
		System.err.println("      java RoadMap -a <MapFile>");
	}

	public static void main(String[] args) throws Exception {
		if (args.length == 2 && args[0].equals("-i")) {
			RoadMap map = new RoadMap();
			try {
				map.loadMap(args[1]);
			} catch (Exception e) {
				System.err.println("Error in reading map file");
				System.exit(-1);
			}

			System.out.println();
			System.out.println("Read road map from " + args[1] + ":");
			map.printMap();
			System.out.println();
		}
		else if (args.length == 2 && args[0].equals("-a")) {
			RoadMap map = new RoadMap();
			try {
				map.loadMap(args[1]);
			} catch (Exception e) {
				System.err.println("Error in reading map file");
				System.exit(-1);
			}
			System.out.println();
			System.out.println("Read road map from " + args[1] + ":");
			map.printMap();

			int n = map.minNumAssistanceCars();
			System.out.println();
			System.out.println("The map requires at least " + n + " assistance car(s)");
			System.out.println();
		}
		else if (args.length == 4 && args[0].equals("-c")) {
			RoadMap map = new RoadMap();
			try {
				map.loadMap(args[1]);
			} catch (Exception e) {
				System.err.println("Error in reading map file");
				System.exit(-1);
			}
			System.out.println();
			System.out.println("Read road map from " + args[1] + ":");
			map.printMap();

			int startVertexIdx = -1, endVertexIdx = -1;
			try {
				startVertexIdx = Integer.parseInt(args[2]);
				endVertexIdx = Integer.parseInt(args[3]);
			} catch (NumberFormatException e) {
				System.err.println("Error: start vertex and end vertex must be specified using their indices");
				System.exit(-1);
			}

			if (startVertexIdx < 0 || startVertexIdx >= map.numPlaces()) {
				System.err.println("Error: invalid index for start vertex");
				System.exit(-1);
			}

			if (endVertexIdx < 0 || endVertexIdx >= map.numPlaces()) {
				System.err.println("Error: invalid index for end vertex");
				System.exit(-1);
			}
			Vertex startVertex = map.getPlace(startVertexIdx);
			Vertex endVertex = map.getPlace(endVertexIdx);
			if (!map.isConnectedWithChargingStations(startVertex, endVertex)) {
				System.out.println();
				System.out.println("There is no path connecting " + map.getPlace(startVertexIdx).getName() + " and "
						+ map.getPlace(endVertexIdx).getName() + " with charging stations");
			} else {
				System.out.println();
				System.out.println("There is at least one path connecting " + map.getPlace(startVertexIdx).getName() + " and "
						+ map.getPlace(endVertexIdx).getName() + " with charging stations");
			}
			System.out.println();

		} else {
			printCommandError();
			System.exit(-1);
		}
	}

	// Task 1: Load the map from a text file
	public void loadMap(String filename) {
		File file = new File(filename);
		places.clear();
		roads.clear();

		try {
			Scanner sc = new Scanner(file);

			// Read the first line: number of vertices and number of edges
			int numVertices = sc.nextInt();
			int numEdges = sc.nextInt();

			for (int i = 0; i < numVertices; ++i) {
				// Read the vertex name and its charging station flag
				String placeName = sc.next();
				int charginStationFlag = sc.nextInt();
				boolean hasChargingStation = (charginStationFlag == 1);

				Vertex currentVertex = new Vertex(placeName, hasChargingStation, i); // creates a vertex object with the information read from the file
				places.add(currentVertex); // adds the above vertex object to the places data structure
				// Add your code here to create a new vertex using the information above and add
				// it to places

			}

			for (int j = 0; j < numEdges; ++j) {
				// Read the edge length and the indices for its two vertices
				int vtxIndex1 = sc.nextInt();
				int vtxIndex2 = sc.nextInt();
				int length = sc.nextInt();
				Vertex vtx1 = places.get(vtxIndex1);
				Vertex vtx2 = places.get(vtxIndex2);

				Edge currentEdge = new Edge(length,vtx1,vtx2); // defines an edge object with the length and end and start vertices read from the file
				roads.add(currentEdge); // adds this edge to the roads data structure
				Edge reverseEdge = new Edge(length,vtx2,vtx1); // creates a reverse edge object which swaps the places around so that the incident roads accessed from either vertex will point to the other vertex
				vtx1.addIncidentRoad(currentEdge); // adds the first edge to the first vertices' insidentRoads attribute
				vtx2.addIncidentRoad(reverseEdge); // adds the second edge to the second vertices' incidentRoads attribute
				// Add your code here to create a new edge using the information above and add
				// it to roads
				// You should also set up incidentRoads for each vertex
			}

			sc.close();

			// Add your code here if appropriate
		} catch (Exception e) {
			e.printStackTrace();
			places.clear();
			roads.clear();
		}
	}



	// Task 2: Check if two vertices are connected by a path with charging stations on each intermediate vertex.
	// Return true if such a path exists; return false otherwise.
	// The worst-case time complexity of your algorithm should be no worse than O(v + e),
	// where v and e are the number of vertices and the number of edges in the graph.
	public boolean isConnectedWithChargingStations(Vertex startVertex, Vertex endVertex) {
		// Sanity check
		if (startVertex.getIndex() == endVertex.getIndex()) {
			return true;
		}
		ArrayList<Vertex> visited = new ArrayList<Vertex>();
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		vertices.add(startVertex);
		visited.add(startVertex);
		while (!vertices.isEmpty()){
			ArrayList<Vertex> next_vertices = new ArrayList<Vertex>();
			for (int i = 0; i < vertices.size(); ++i){
				Vertex currentVertex = vertices.get(i);
				for (int j = 0; j < currentVertex.getIncidentRoads().size(); ++j){
					Vertex currentEndVertex = currentVertex.getIncidentRoads().get(j).getSecondVertex();
					if (!visited.contains(currentEndVertex)){
						visited.add(currentEndVertex);
						if (endVertex == currentEndVertex){
							return true;
						}
						if (currentEndVertex.hasChargingStation()){
							next_vertices.add(currentEndVertex);
						}
					}
				}
			}
			vertices = next_vertices;
		}
		// The following return statement is just a placeholder.
		// Update the code to correctly determine whether the two vertices are connected by a path with charging stations
		return false;
	}

	// Task 3: Determine the mininum number of assistance cars required
	public int minNumAssistanceCars() {
		ArrayList<Vertex> visited = new ArrayList<Vertex>();
		int graphCount = 0;
		for (int i=0; i<places.size(); ++i){
			Vertex currentVertex = places.get(i);
			if (!visited.contains(currentVertex)){
				BFSHelper(currentVertex,visited);
				graphCount++;
			}
		}
		return graphCount;
	}

	public static void BFSHelper(Vertex source, ArrayList<Vertex> visited){
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		vertices.add(source);
		visited.add(source);
		while (!vertices.isEmpty()){
			ArrayList<Vertex> next_vertices = new ArrayList<Vertex>();
			for (int i = 0; i < vertices.size(); ++i){
				Vertex currentVertex = vertices.get(i);
				for (int j = 0; j < currentVertex.getIncidentRoads().size(); ++j){
					Vertex currentEndVertex = currentVertex.getIncidentRoads().get(j).getSecondVertex();
					if (!visited.contains(currentEndVertex)){
						visited.add(currentEndVertex);
						next_vertices.add(currentEndVertex);
					}
				}
			}
			vertices = next_vertices;
		}
	}

	public void printMap() {
		System.out.println("The map contains " + this.numPlaces() + " places and " + this.numRoads() + " roads");
		System.out.println();

		System.out.println("Places:");

		for (Vertex v : places) {
			System.out.println("- name: " + v.getName() + ", charging station: " + v.hasChargingStation());
		}

		System.out.println();
		System.out.println("Roads:");

		for (Edge e : roads) {
			System.out.println("- (" + e.getFirstVertex().getName() + ", " + e.getSecondVertex().getName()
					+ "), length: " + e.getLength());
		}
	}

	public void printPath(ArrayList<Vertex> path) {
		System.out.print("(  ");

		for (Vertex v : path) {
			System.out.print(v.getName() + "  ");
		}

		System.out.println(")");
	}

	public int numPlaces() {
		return places.size();
	}

	public int numRoads() {
		return roads.size();
	}

	public Vertex getPlace(int idx) {
		return places.get(idx);
	}

	private ArrayList<Vertex> places;
	private ArrayList<Edge> roads;
}
