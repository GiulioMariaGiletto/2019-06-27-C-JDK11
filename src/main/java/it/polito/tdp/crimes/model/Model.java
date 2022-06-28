package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	EventsDao dao;
	Graph<String,DefaultWeightedEdge> grafo;
	String partenza;
	String arrivo;
	List<String> best;
	
	
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
	
	public Set<Adiacenza> allArch(){
		Set<Adiacenza> result=new HashSet<>();
		for(DefaultWeightedEdge e:this.grafo.edgeSet()) {
			double p=this.grafo.getEdgeWeight(e);
			String par=this.grafo.getEdgeSource(e);
			String arr=this.grafo.getEdgeTarget(e);
			result.add(new Adiacenza(par,arr,p));
		}
		
		return result;
	}
	
	public List<String> ricorsione(String partenza,String arrivo){
		List<String> parziale=new ArrayList<>();
		this.partenza=partenza;
		this.best=new LinkedList<>(this.grafo.vertexSet());
		parziale.add(partenza);
		this.arrivo=arrivo;
		cerca(parziale);
		return best;		
	}

	private void cerca(List<String> parziale) {
		if(parziale.size()==this.grafo.vertexSet().size() && parziale.get(parziale.size()-1).equals(this.arrivo)) {
			if(parziale.size()<best.size()+1) {
				best=new ArrayList<>(parziale);
				return;
			}
		}
		String corr=parziale.get(parziale.size()-1);
		List<String> adiac=Graphs.neighborListOf(this.grafo, corr);
		for(String a:adiac) {
			if(!parziale.contains(a)) {
				parziale.add(a);
				cerca(parziale);
				parziale.remove(parziale.size()-1);
			}
			
			
		}
		
		
	}

	
}
