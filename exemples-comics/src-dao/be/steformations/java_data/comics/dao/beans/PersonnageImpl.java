package be.steformations.java_data.comics.dao.beans;

import be.steformations.java_data.comics.interfaces.dao.beans.Personnage;

@javax.persistence.Entity(name="Comic") // obligatoire (nom facultatif)
@javax.persistence.Table(name="personnages")
public class PersonnageImpl implements Personnage {

	@javax.persistence.Id
	@javax.persistence.GeneratedValue(strategy=javax.persistence.GenerationType.IDENTITY)
	@javax.persistence.Column(name="pk")	
	private Integer id;
	@javax.persistence.Basic					// valeur par défaut
	@javax.persistence.Column(name="nom")	// facultatif
	private String nom;
	
	/* Basic et noms de colonne identiques */
	private String prenom;
	private String aka;
	private java.sql.Date ddn;
	
	//@javax.persistence.Transient // pas de mappage
	@javax.persistence.ManyToOne
	@javax.persistence.JoinColumn(name="fk_genre") // clef étrangère dans la table correspondant à la classe courante
	private GenreImpl genre;
	
	@javax.persistence.ManyToMany
	@javax.persistence.JoinTable(
			name="liens_personnages_aventures",
			joinColumns=@javax.persistence.JoinColumn(name="fk_personnage"),
			inverseJoinColumns=@javax.persistence.JoinColumn(name="fk_aventure")
			)
	private java.util.List<AventureImpl> histoires;
	
	public PersonnageImpl() {
		super();
	}

	public java.util.List<AventureImpl> getHistoires(){
		if (this.histoires == null){
			this.histoires = new java.util.ArrayList<>();
		}
		
		return this.histoires;
		
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

	public void setDdn(java.sql.Date ddn) {
		this.ddn = ddn;
	}

	public GenreImpl getGenre() {
		return genre;
	}

	public void setGenre(GenreImpl genre) {
		this.genre = genre;
	}
	
	
	
	
	
}
