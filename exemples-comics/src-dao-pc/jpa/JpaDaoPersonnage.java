package jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import be.steformations.java_data.comics.interfaces.dao.beans.Aventure;
import be.steformations.java_data.comics.interfaces.dao.beans.Genre;
import be.steformations.java_data.comics.interfaces.dao.beans.Personnage;
import be.steformations.java_data.comics.interfaces.dao.managers.PersonnageManager;
import be.steformations.java_data.comics.dao.beans.AventureImpl;
import be.steformations.java_data.comics.dao.beans.GenreImpl;
import be.steformations.java_data.comics.dao.beans.PersonnageImpl;

public class JpaDaoPersonnage implements PersonnageManager {

	private javax.persistence.EntityManager entityManager;	
	
	public JpaDaoPersonnage(javax.persistence.EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	@Override
	public PersonnageImpl getPersonnage(String prenom, String nom) {
		PersonnageImpl personnage = null;
		
		String jpql = "select o from Comic o where lower(o.prenom) = lower(?1) and upper(o.nom) = upper(?2)";
		javax.persistence.TypedQuery<PersonnageImpl> query
			= this.entityManager.createQuery(jpql, PersonnageImpl.class);
		query.setParameter(1, prenom);
		query.setParameter(2, nom);
		
		try{
			personnage = query.getSingleResult(); // recherche d'une seule instance
		} catch(javax.persistence.NoResultException e){
			// pas de correspondance dans la db
		}
		
		return personnage;
	}

	@Override
	public PersonnageImpl getPersonnage(int id) {
		PersonnageImpl personnage = null;
		//uniquement les recherches sur l'Id de l'entité
		personnage = this.entityManager.find(PersonnageImpl.class, id);
		
		return personnage;
	}

	@Override
	public List<? extends Aventure> getAventures(int id) {
		java.util.List<AventureImpl> aventures = null;
		PersonnageImpl personnage = this.getPersonnage(id);
		if(personnage != null){
			aventures= personnage.getHistoires();
		} else {
			aventures = java.util.Collections.emptyList();
		}
		
		return aventures;
	}

	@Override
	public Personnage creerPersonnage(String prenom, String nom, String aka, Date ddn, Genre genre) {
		PersonnageImpl p = null;
		if(prenom != null && nom != null){
			p = this.getPersonnage(prenom, nom);
			if (p == null){
				p = new PersonnageImpl();
				p.setPrenom(prenom);
				p.setNom(nom);
				p.setAka(aka);
				if (ddn != null){
					p.setDdn(new java.sql.Date(ddn.getTime()));
				}
				if (genre != null){
					p.setGenre((GenreImpl) genre);
				}
				this.entityManager.getTransaction().begin();
				this.entityManager.persist(p);
				this.entityManager.getTransaction().commit();
			}
		}
		
		return p;
	}

	@Override
	public void modifierPersonnage(int id, String aka) {
		PersonnageImpl p = this.getPersonnage(id);
		if (p != null){
			p.setAka(aka);
			this.entityManager.getTransaction().begin();
			this.entityManager.persist(p);
			this.entityManager.getTransaction().commit();
		}
		
	}

	@Override
	public void supprimerPersonnage(int id) {
		PersonnageImpl p = this.getPersonnage(id);
		if (p != null){
			this.entityManager.getTransaction().begin();
			this.entityManager.remove(p);
			this.entityManager.getTransaction().commit();
		}
		
	}

}
