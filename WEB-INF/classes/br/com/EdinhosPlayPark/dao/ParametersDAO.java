package br.com.EdinhosPlayPark.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.EdinhosPlayPark.domain.Mail_parameters;
import br.com.EdinhosPlayPark.factory.ConexaoFactory;

public class ParametersDAO {
	public ArrayList<Mail_parameters> listar() throws SQLException {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT ");
		sql.append("id,mail_sender,mail_password,mail_send,mail_destination ");
		sql.append("from playpark.t_parameters ");
			
		Connection conexao = ConexaoFactory.conectar();

		PreparedStatement comando = conexao.prepareStatement(sql.toString());

		ResultSet resultado = comando.executeQuery();

		ArrayList<Mail_parameters> lista = new ArrayList<Mail_parameters>();

		while (resultado.next()) {

			Mail_parameters f = new Mail_parameters();

			f.setId(resultado.getLong("id"));
			f.setMail_destination(resultado.getString("mail_destination"));
			f.setMail_password(resultado.getString("mail_password"));
			f.setMail_send(resultado.getString("mail_send"));	
			f.setMail_sender(resultado.getString("mail_sender"));
			lista.add(f);
		}
		comando.close();
		conexao.close();

		return lista;
	}
	            
	public void salvar(Mail_parameters t) throws SQLException {
		
		
		StringBuilder sql = new StringBuilder();
		sql.append("update playpark.t_parameters set mail_sender = ?,mail_password = ? , mail_send = ? ,mail_destination= ? where id = ?");

		Connection conexao = ConexaoFactory.conectar();
		new ConexaoFactory();
		PreparedStatement comando = ConexaoFactory.conectar().prepareStatement(
				sql.toString());

		comando.setString(1, t.getMail_sender());
		comando.setString(2, t.getMail_password());
		comando.setString(3, t.getMail_send());
		comando.setString(4, t.getMail_destination());
		comando.setLong(5, t.getId());

		comando.executeUpdate();
		comando.close();
		conexao.close();

	}

}
