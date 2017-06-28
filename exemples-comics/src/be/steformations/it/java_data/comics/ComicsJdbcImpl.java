package be.steformations.it.java_data.comics;

import java.sql.Date;
import java.util.List;

import be.steformations.java_data.comics.interfaces.jdbc.ComicsJDBC;

public class ComicsJdbcImpl implements ComicsJDBC {

	private String url = "jdbc:postgresql://localhost:5432/comics-java";
	private String user = "postgres";
	private String pwd = "postgres";
	
	@Override
	public boolean ping() {
//		System.out.println("ping()");
//		// drivers JDBC présents dans le classpath
//		java.util.Enumeration<java.sql.Driver> drivers = java.sql.DriverManager.getDrivers();
//		while (drivers.hasMoreElements()) {
//			System.out.println(drivers.nextElement());
//		}
		
		boolean pinged = false;
		java.sql.Connection connexion = null;
		try {
			connexion = java.sql.DriverManager.getConnection(url, user, pwd);
			pinged = true;
		} catch(java.sql.SQLException e){
			e.printStackTrace();
		} finally {
			if (connexion != null){
				try{
					connexion.close();
				} catch(java.sql.SQLException e){
					// tant pis...
				}
			}
		}
		
		return pinged;
	}

	@Override
	public String selectAka(String prenom, String nom) {
		String aka = null;
		
		if (prenom != null && nom != null){
			
			String sql = "select aka from personnages where prenom = '" + prenom + "' and nom = '" + nom + "'";
			
			java.sql.Connection connexion = null;
			java.sql.Statement requete = null;
			java.sql.ResultSet resultat = null;
			
			try{
				connexion = java.sql.DriverManager.getConnection(url, user, pwd);
				requete = connexion.createStatement();
				resultat = requete.executeQuery(sql);
				
				if (resultat.next()) {//next() renvoie faux s'il n'y a pas de ligne suivante 
					aka = resultat.getString(1);
				}
				
			} catch(java.sql.SQLException e){
				e.printStackTrace();
			} finally {
				if (resultat != null){
					try{
						resultat.close();
					} catch(java.sql.SQLException e){}
				}
				if (requete != null){
					try{
						requete.close();
					} catch(java.sql.SQLException e){}
				}
				if (connexion != null){
					try{
						connexion.close();
					} catch(java.sql.SQLException e){}
				}
			}
			
		}
		
		return aka;
	}

	@Override
	public String selectGenre(String prenom, String nom) {
		String genre = null;
		
		if (prenom != null && nom != null){
			String sql = "select g.nom as nomDuGenre "
					+ " from personnages p join genres g on g.num = p.fk_genre "
					+ "where p.prenom = ? and p.nom = ?";

			try (
				// declaration et assignation d'instances de java.lang.AutoCloseable
				// Leur méthode close() est invoquée automatiquement par la machine virtuelle	
				java.sql.Connection connexion = java.sql.DriverManager.getConnection(url, user, pwd);
				java.sql.PreparedStatement requete = connexion.prepareStatement(sql)						
			){
				requete.setString(1, prenom);
				requete.setString(2, nom);
				
				try (
						java.sql.ResultSet resultat = requete.executeQuery();
				){
					if (resultat.next()){
						genre = resultat.getString("nomDuGenre");
					}
				} catch (java.sql.SQLException e){
					e.printStackTrace();
				}				
				
			} catch(java.sql.SQLException e){
				e.printStackTrace();
			}
		}
		return genre;
	}

	@Override
	public java.util.List<String> selectPrenoms(String genre) {
		java.util.List<String> prenoms = new java.util.ArrayList<String>();
		
		String sql = "select prenom from personnages join genres on fk_genre = num "
				+ "where genres.nom = ?";
		
		try(
				java.sql.Connection connexion = java.sql.DriverManager.getConnection(url, user, pwd);
				java.sql.PreparedStatement requete = connexion.prepareStatement(sql);
		){
			requete.setString(1, genre);
			
			try(
					java.sql.ResultSet resultat = requete.executeQuery(); 
			){
				while(resultat.next()){
					String prenom = resultat.getString(1);
					prenoms.add(prenom);
				}
			}
		} catch (java.sql.SQLException e){
			e.printStackTrace();
		}
		
		return prenoms;
	}

	@Override
	public int insertPersonnage(String prenom, String nom, String aka, Date ddn) {
		int id = 0;
		
		if (prenom != null && nom != null){
			
			String sqlInsert = "insert into personnages(nom, prenom, aka, ddn) "
					+ "values(?, ?, ?, ?)";
			String sqlSelect = "select pk from personnages where prenom = ? and nom = ?";
			
			try(
					java.sql.Connection connexion = java.sql.DriverManager.getConnection(url, user, pwd);					
			){
				connexion.setAutoCommit(false);
				try(
					java.sql.PreparedStatement requete = connexion.prepareStatement(sqlInsert);	
				){
					requete.setString(1, prenom);
					requete.setString(2, nom);
					if (aka != null){
						requete.setString(3, aka);
					} else {
						requete.setNull(3, java.sql.Types.VARCHAR);
					}
					if (ddn != null){
						requete.setDate(4, ddn);
					} else {
						requete.setNull(4, java.sql.Types.DATE);
					}
					
					// insert, update, delete
					requete.executeUpdate();
					connexion.commit(); //modifications sont définitivement enregistrées
				}
				try(
						java.sql.PreparedStatement requete = connexion.prepareStatement(sqlSelect);						
					){
					requete.setString(1, prenom);
					requete.setString(2, nom);
					
					try ( 
							java.sql.ResultSet resultat = requete.executeQuery();		
					){
						if(resultat.next()){
							id = resultat.getInt("pk");
						}
					}
				}
				
			} catch (java.sql.SQLException e){
				e.printStackTrace();
			}	
			
		}
		
		return id;
	}

	@Override
	public void updateAka(String prenom, String nom, String aka) {
		if (prenom != null && nom != null){
			
			String sql = "{ call updatePersonnage(?, ?, ?, ?) }";
			try(
					java.sql.Connection connexion = java.sql.DriverManager.getConnection(url, user, pwd);					
					java.sql.CallableStatement requete = connexion.prepareCall(sql);
			) {
				connexion.setAutoCommit(true); // commit automatique après chaque instruction
				
				requete.setString(1, prenom);
				requete.setString(2, nom);
				requete.setString(3, aka);
				
				requete.execute();
				
			} catch (java.sql.SQLException e){
				e.printStackTrace();				
			}
		}
		
	}

}
