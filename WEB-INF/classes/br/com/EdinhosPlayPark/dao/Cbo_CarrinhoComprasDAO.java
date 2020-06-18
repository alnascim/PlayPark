package br.com.EdinhosPlayPark.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.EdinhosPlayPark.domain.Cbo_CarrinhoCompras;
import br.com.EdinhosPlayPark.domain.T_entradas;
import br.com.EdinhosPlayPark.domain.T_item;
import br.com.EdinhosPlayPark.domain.T_saidas;
import br.com.EdinhosPlayPark.factory.ConexaoFactory;
public class Cbo_CarrinhoComprasDAO {
	
	public ArrayList<Cbo_CarrinhoCompras> listar(T_entradas t,T_saidas s ) throws SQLException {
	
//
////		***********************************************************************
////		Recupera a FK da tabela t_entradas para relacionar com tabela T_saidas
//		StringBuilder sqlrec = new StringBuilder();
//		int id_fk = 0;
//		Connection con = ConexaoFactory.conectar();
//		System.out.println(t.getId_controle());
//		sqlrec.append("SELECT id FROM playpark.t_entradas where id_controle = " + t.getId_controle());
//		PreparedStatement com = con.prepareStatement(sqlrec.toString());
//
//		ResultSet res = com.executeQuery();
//
//		while (res.next()) {
//
//			id_fk = res.getInt("id");
//		}
//
//		com.close();
//		con.close();	
////		***********************************************************************
//		
//		s.setFk_id(t.getId());
				
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT ");
		sql.append("id,t_nome_item,replace(replace(replace(format(a.t_valor ,2), '.','|'),',','.'),'|',',') as valor ");
		sql.append(" from playpark.t_itens a ");

		Connection conexao = ConexaoFactory.conectar();

		PreparedStatement comando = conexao.prepareStatement(sql.toString());

		ResultSet resultado = comando.executeQuery();
		
		ArrayList<Cbo_CarrinhoCompras> lista = new ArrayList<Cbo_CarrinhoCompras>();

		while (resultado.next()) {
			
			Cbo_CarrinhoCompras f = new Cbo_CarrinhoCompras();
			f.setId(resultado.getInt("id"));
			f.setT_nome_item(resultado.getString("t_nome_item"));
			f.setSt_valor(resultado.getString("valor"));
			
			lista.add(f);

		}
		comando.close();
		conexao.close();
		return lista;
	}
	
}
