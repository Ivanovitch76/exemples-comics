package jpa;
import java.util.List;

import be.steformations.java_data.comics.dao.beans.GenreImpl;
import be.steformations.java_data.comics.dao.beans.PersonnageImpl;
import be.steformations.java_data.comics.interfaces.dao.beans.Genre;
import be.steformations.java_data.comics.interfaces.dao.beans.Personnage;
import be.steformations.java_data.comics.interfaces.dao.managers.GenreManager;

public class JpaDaoGenre implements GenreManager{

	private javax.persistence.EntityManager entityManager;
	
	
	
	public JpaDaoGenre(javax.persistence.EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	@Override
	public Genre getGenre(String nom) {
		GenreImpl genre = null;
		
		javax.persistence.TypedQuery<GenreImpl> query
			= this.entityManager.createNamedQuery("GetGenreByName", GenreImpl.class);
		query.setParameter(1, nom);
		
		try{
			genre = query.getSingleResult();
		} catch(javax.persistence.NoResultException e){
			
		}
		
		return genre;
	}

	@Override
	public List<? extends Personnage> getPersonnages(String nom) {
		java.util.List<PersonnageImpl> list = null;
		String jpql = "select p from Comic p where p.genre.nom = ?1";
		javax.persistence.TypedQuery<PersonnageImpl> query
			= this.entityManager.createQuery(jpql, PersonnageImpl.class);
		query.setParameter(1, nom);
		list = query.getResultList();
		
		return list;
	}

}
