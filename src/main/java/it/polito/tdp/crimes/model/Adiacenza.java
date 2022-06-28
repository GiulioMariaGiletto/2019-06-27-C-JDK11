package it.polito.tdp.crimes.model;

public class Adiacenza {
	public String type1;
	public String type2;
	double peso;
	public Adiacenza(String type1, String type2, double peso) {
		this.type1 = type1;
		this.type2 = type2;
		this.peso = peso;
	}
	@Override
	public String toString() {
		return "Adiacenza [type1=" + type1 + ", type2=" + type2 + ", peso=" + peso + "]";
	}
	
	

}
