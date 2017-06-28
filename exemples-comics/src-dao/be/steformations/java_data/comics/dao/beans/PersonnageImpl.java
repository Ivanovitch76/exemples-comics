package be.steformations.java_data.comics.dao.beans;

import be.steformations.java_data.comics.interfaces.dao.beans.Personnage;

public class PersonnageImpl implements Personnage {

	private Integer id;
	private String nom;
	private String prenom;
	private String aka;
	private java.util.Date ddn;
	private GenreImpl genre;
	
	public PersonnageImpl() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getAka() {
		return aka;
	}

	public void setAka(String aka) {
		this.aka = aka;
	}

	public java.util.Date getDdn() {
		return ddn;
	}

	public void setDdn(java.util.Date ddn) {
		this.ddn = ddn;
	}

	public GenreImpl getGenre() {
		return genre;
	}

	public void setGenre(GenreImpl genre) {
		this.genre = genre;
	}
	
	
	
	
	
}
