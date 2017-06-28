package be.steformations.java_data.comics.tests.jdbc;

import be.steformations.java_data.comics.interfaces.jdbc.ComicsJDBC;

public class FabriqueComicsJDBC {

	public static ComicsJDBC getComicsJDBC() {
		// TODO
		return new be.steformations.it.java_data.comics.ComicsJdbcImpl();
	}
}
