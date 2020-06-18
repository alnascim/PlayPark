package br.com.EdinhosPlayPark.factory;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class ConexaoFactory {

	private static final String USUARIO = "root";
	private static final String SENHA = "webbroker2016";
	private static final String URL = "jdbc:mysql://localhost:1701/playpark?useUnicode=true&characterEncoding=utf-8";

	public static Connection conectar() throws SQLException {
		DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
		return conexao;

	}


}
