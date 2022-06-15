package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	EventsDao dao;
	Graph<String,DefaultWeightedEdge> grafo; 
	
	public Model() {
		this.dao= new EventsDao();		
	}
	
	public List<String> cat(){
		return dao.listAllCate();
	}
	public List<Integer> day(){
		return dao.listAllDay();
	}
	public void creaGrafo(String cate,int day) {
		this.grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class); 
		//aggiungo i vertici 
		Graphs.addAllVertices(this.grafo, dao.vert(cate, day));
		//aggiungo archi
		for(Adiacenza a:dao.Arch(cate, day)) {
			Graphs.addEdge(this.grafo, a.type1, a.type2, a.peso);
		}
	}
	
	public int nvert() {
		return this.grafo.vertexSet().size();
	}
	public int nArch() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Adiacenza> getMin(){
		List<Adiacenza> result=new ArrayList<>();
		double max=0.0;
		double min=10000.0;
		for(DefaultWeightedEdge e:this.grafo.edgeSet()) {
			double p=this.grafo.getEdgeWeight(e);
			if(p>max) {
				max=p;
			}
			else if(p<min) {
				min=p;
			}
			
		}
		double me=(max+min)/2;
		for(DefaultWeightedEdge e:this.grafo.edgeSet()) {
			double p=this.grafo.getEdgeWeight(e);
			if(me>p) {
				String pe=this.grafo.getEdgeSource(e);
				String arr=this.grafo.getEdgeTarget(e);
				result.add(new Adiacenza(pe,arr,p));
			}
		}
		
		return result;
	}
	
}
