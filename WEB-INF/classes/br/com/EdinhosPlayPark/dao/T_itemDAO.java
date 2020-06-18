package br.com.EdinhosPlayPark.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.jasper.tagplugins.jstl.core.If;

import br.com.EdinhosPlayPark.domain.T_entradas;
import br.com.EdinhosPlayPark.domain.T_item;
import br.com.EdinhosPlayPark.factory.ConexaoFactory;

public class T_itemDAO {

	private String smail_sender;
	private String smail_password;
	private Integer smail_send;
	private String smail_destination;
	private Connection con;

	public void fechar_comanda(T_entradas f) throws SQLException {
		StringBuilder stringBuilder = new StringBuilder();
		StringBuilder sqlsel = new StringBuilder();
		StringBuilder sqlmail = new StringBuilder();
		// ***********************************Soma valor total da comanda

		StringBuilder sqlsoma = new StringBuilder();
		sqlsoma.append("select ");
		sqlsoma.append("Sum(Case when it.t_tipo = 'MINUTAGEM' THEN ");
		sqlsoma.append("(SELECT P.VALOR FROM playpark.t_preco P WHERE ( ");
		sqlsoma.append("SELECT right(left( ");
		sqlsoma.append("TIMEDIFF(CURTIME(),et1.t_time),5),2) as dif ");
		sqlsoma.append("FROM playpark.t_entradas et1 ");
		sqlsoma.append("where et1.id_controle=et.id_controle ");
		sqlsoma.append(") between t_minutos_ini and t_minutos) ");
		sqlsoma.append("when it.t_tipo = 'DIVERSOS' THEN  ");
		sqlsoma.append("it.t_valor  END) AS VALOR ");
		sqlsoma.append("from playpark.t_saidas sd ");
		sqlsoma.append("inner join  playpark.t_itens it on (it.id = sd.fk_id_item) ");
		sqlsoma.append("inner join playpark.t_entradas et on (et.id = sd.fk_id) ");
		sqlsoma.append("where et.id_controle = " + f.getId_controle());

		Connection con_1 = ConexaoFactory.conectar();
		new ConexaoFactory();
		PreparedStatement com_1 = ConexaoFactory.conectar().prepareStatement(
				sqlsoma.toString());

		ResultSet res_1 = com_1.executeQuery();

		String svalor = null;

		while (res_1.next()) {

			f.setsTotal(res_1.getString("VALOR"));

		}
		com_1.close();
		con_1.close();

		// ***********************************Insere Valor para fechar a comanda
		StringBuilder sqlins = new StringBuilder();

		sqlins.append("INSERT INTO playpark.t_comanda_fechada(id_controle,t_valor,t_tipopagto");
		sqlins.append(") VALUES (?,?,?) ");

		Connection con_2 = ConexaoFactory.conectar();
		new ConexaoFactory();
		PreparedStatement com_2 = ConexaoFactory.conectar().prepareStatement(
				sqlins.toString());

		com_2.setLong(1, f.getId_controle());
		com_2.setString(2, f.getsTotal());
		com_2.setString(3, f.getTipo_pagto());

		com_2.executeUpdate();
		com_2.close();
		con_2.close();

		// ***********************************

		sqlsel.append("select et.id_controle,it.t_nome_item, ");
		sqlsel.append("replace(replace(replace(format(Case when it.t_tipo = 'MINUTAGEM' THEN ");
		sqlsel.append("(SELECT P.VALOR FROM playpark.t_preco P WHERE ( ");
		sqlsel.append("SELECT right(left( ");
		sqlsel.append("TIMEDIFF(CURTIME(),et1.t_time),5),2) as dif ");
		sqlsel.append("FROM playpark.t_entradas et1 ");
		sqlsel.append("where et1.id_controle=et.id_controle ");
		sqlsel.append(") between t_minutos_ini and t_minutos) ");
		sqlsel.append("when it.t_tipo = 'DIVERSOS' THEN  ");
		sqlsel.append("it.t_valor  END ,2), '.','|'),',','.'),'|',',') AS VALOR ");
		sqlsel.append("from playpark.t_saidas sd ");
		sqlsel.append("inner join  playpark.t_itens it on (it.id = sd.fk_id_item) ");
		sqlsel.append("inner join playpark.t_entradas et on (et.id = sd.fk_id) ");

		sqlsel.append("where et.id_controle = " + f.getId_controle());

		Connection con_3 = ConexaoFactory.conectar();
		new ConexaoFactory();
		PreparedStatement com_3 = ConexaoFactory.conectar().prepareStatement(
				sqlsel.toString());

		ResultSet resultado = com_3.executeQuery();
		stringBuilder.append("Ticket Nro  : " + f.getId_controle() + "\n\n\n");

		while (resultado.next()) {
			stringBuilder.append("Item Consumido   : "
					+ resultado.getString("t_nome_item") + "    Valor R$ "
					+ resultado.getString("valor") + "\n");
		}
		com_3.close();
		con_3.close();

		// // Recuperar Parametros do email *********************************
		//
		sqlmail.append("SELECT ");
		sqlmail.append("id,mail_sender,mail_password,case when mail_send= 'S' then 1 else 0 end as mail_send,mail_destination  ");
		sqlmail.append("from playpark.t_parameters ");

		Connection con_mail = ConexaoFactory.conectar();
		new ConexaoFactory();
		PreparedStatement com = ConexaoFactory.conectar().prepareStatement(
				sqlmail.toString());

		ResultSet r = com.executeQuery();

		while (r.next()) {

			smail_sender = r.getString("mail_sender");
			smail_password = r.getString("mail_password");
			smail_send=r.getInt("mail_send");
			smail_destination = r.getString("mail_destination");

		}
		com.close();
		con_mail.close();
		//
		// // ****************************************************************
		//
		//
		//
		// Inicia Envio do Email
		Properties props = new Properties();
		// /** Parâmetros de conexão com servidor Gmail */
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "2525");

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(smail_sender,
								smail_password);
					}
				});

		/** Ativa Debug para sessão */
		session.setDebug(true);

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(smail_sender)); // Remetente

			Address[] toUser = InternetAddress // Destinatário(s)
					.parse(smail_destination);

			message.setRecipients(Message.RecipientType.TO, toUser);
			message.setSubject("Fechamento da Comanda Nro "
					+ f.getId_controle());// Assunto

			message.setText(stringBuilder.toString());
			/** Método para enviar a mensagem criada */

			if (smail_send == 1) {
				Transport.send(message);
			}

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public ArrayList<T_item> listarconsumo(T_item en) throws SQLException {

		StringBuilder sql = new StringBuilder();

		// System.out.println(en.getId_controle());

		sql.append("SELECT ");
		sql.append("sd.id,et.id as fk_id,et.id_controle ,");
		sql.append("it.t_nome_item,");
		sql.append("replace(replace(replace(format(Case when it.t_tipo = 'MINUTAGEM' THEN ");
		sql.append("(SELECT P.VALOR FROM playpark.t_preco P WHERE P.T_MINUTOS=ET.T_MINUTOS) when it.t_tipo = 'DIVERSOS' THEN ");
		sql.append("it.t_valor  END ,2), '.','|'),',','.'),'|',',') AS VALOR, ");

		sql.append("replace(replace(replace(format(Case when it.t_tipo = 'MINUTAGEM' THEN ");
		sql.append("(SELECT P.VALOR FROM playpark.t_preco P WHERE ( ");
		sql.append("SELECT right(left( ");
		sql.append("TIMEDIFF(CURTIME(),et1.t_time),5),2) as dif ");
		sql.append("FROM playpark.t_entradas et1 ");
		sql.append("where et1.id_controle=et.id_controle ");
		sql.append(") between t_minutos_ini and t_minutos) ");
		sql.append("when it.t_tipo = 'DIVERSOS' THEN  ");
		sql.append("it.t_valor  END ,2), '.','|'),',','.'),'|',',') AS VALOR_ATUAL ");

		sql.append("from playpark.t_saidas sd ");
		sql.append("inner join  playpark.t_itens it on (it.id = sd.fk_id_item) ");
		sql.append("inner join playpark.t_entradas et on (et.id = sd.fk_id)");
		sql.append("where et.id_controle = " + en.getId_controle());
		sql.append(" order by sd.id desc");

		Connection conexao = ConexaoFactory.conectar();

		PreparedStatement comando = conexao.prepareStatement(sql.toString());

		ResultSet resultado = comando.executeQuery();

		ArrayList<T_item> lista = new ArrayList<T_item>();

		while (resultado.next()) {

			T_item f = new T_item();

			f.setId(resultado.getInt("id"));
			f.setT_nome_item(resultado.getString("t_nome_item"));
			f.setSt_valor("R$ " + resultado.getString("valor"));
			f.setSt_valor_atual("R$ " + resultado.getString("VALOR_ATUAL"));

			f.setFk_id(resultado.getInt("fk_id"));
			f.setId_controle(resultado.getLong("id_controle"));
			lista.add(f);
		}
		comando.close();
		conexao.close();

		return lista;
	}

	public ArrayList<T_item> listar() throws SQLException {
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT ");
		sql.append("a.id,");
		sql.append("a.t_nome_item,");
		sql.append("replace(replace(replace(format(a.t_valor ,2), '.','|'),',','.'),'|',',') as valor ");

		sql.append("from playpark.t_itens a");

		Connection conexao = ConexaoFactory.conectar();

		PreparedStatement comando = conexao.prepareStatement(sql.toString());

		ResultSet resultado = comando.executeQuery();

		ArrayList<T_item> lista = new ArrayList<T_item>();

		while (resultado.next()) {

			T_item f = new T_item();

			f.setId(resultado.getInt("id"));
			f.setT_nome_item(resultado.getString("t_nome_item"));
			f.setSt_valor("R$ " + resultado.getString("valor"));

			lista.add(f);
		}
		comando.close();
		conexao.close();

		return lista;
	}

	public void excluir(T_item f) throws SQLException {

		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM playpark.t_itens WHERE id = ? ");
		new ConexaoFactory();
		PreparedStatement comando = ConexaoFactory.conectar().prepareStatement(
				sql.toString());

		comando.setInt(1, f.getId());
		comando.executeUpdate();
		comando.close();

	}

	public void salvar_minut() throws SQLException {

		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO playpark.t_itens (t_nome_item,t_valor,t_tipo) ");
		sql.append("SELECT 'Minutagem','0.00','MINUTAGEM' FROM DUAL ");
		sql.append("WHERE NOT EXISTS (SELECT t_tipo FROM playpark.t_itens WHERE t_tipo = 'MINUTAGEM')");

		Connection conexao = ConexaoFactory.conectar();
		new ConexaoFactory();
		PreparedStatement comando = ConexaoFactory.conectar().prepareStatement(
				sql.toString());

		comando.executeUpdate();
		comando.close();
		conexao.close();

	}

	public void salvar(T_item f) throws SQLException {

		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO playpark.t_itens(t_nome_item,t_valor,t_tipo");

		sql.append(") VALUES (?,?,?) ");

		Connection conexao = ConexaoFactory.conectar();
		new ConexaoFactory();
		PreparedStatement comando = ConexaoFactory.conectar().prepareStatement(
				sql.toString());

		comando.setString(1, f.getT_nome_item());
		comando.setString(2, f.getSt_valor());
		comando.setString(3, "DIVERSOS");

		comando.executeUpdate();
		comando.close();
		conexao.close();

	}


}
