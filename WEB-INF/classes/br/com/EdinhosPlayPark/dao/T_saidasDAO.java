package br.com.EdinhosPlayPark.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
 
import java.util.ArrayList;

import br.com.EdinhosPlayPark.domain.T_entradas;
import br.com.EdinhosPlayPark.domain.T_item;
import br.com.EdinhosPlayPark.domain.T_saidas;
import br.com.EdinhosPlayPark.factory.ConexaoFactory;

public class T_saidasDAO {
	public ArrayList<T_item> listarsaidas(T_entradas en) throws SQLException {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT ");
		sql.append("sd.id,et.id as fk_id,");
		sql.append("it.t_nome_item,");
		sql.append("replace(replace(replace(format(Case when it.t_tipo = 'MINUTAGEM' THEN ");
		sql.append("(SELECT P.VALOR FROM playpark.t_preco P WHERE P.T_MINUTOS=ET.T_MINUTOS) when it.t_tipo = 'DIVERSOS' THEN ");
		sql.append("it.t_valor  END ,2), '.','|'),',','.'),'|',',') AS VALOR ");
		sql.append("from playpark.t_saidas sd ");
		sql.append("inner join  playpark.t_itens it on (it.id = sd.fk_id_item) ");
		sql.append("inner join playpark.t_entradas et on (et.id = sd.fk_id)");
		sql.append("where et.id = "+en.getId());
		sql.append(" order by sd.id desc");
			
		Connection conexao = ConexaoFactory.conectar();

		PreparedStatement comando = conexao.prepareStatement(sql.toString());

		ResultSet resultado = comando.executeQuery();

		ArrayList<T_item> lista = new ArrayList<T_item>();

		while (resultado.next()) {

			T_item f = new T_item();

			f.setId(resultado.getInt("id"));
			f.setT_nome_item(resultado.getString("t_nome_item"));
			f.setSt_valor("R$ "+resultado.getString("valor"));
			f.setFk_id(resultado.getInt("fk_id"));	
			lista.add(f);
		}
		comando.close();
		conexao.close();

		return lista;
	}
	            
	public void excluir(T_item sa) throws SQLException  {

		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM playpark.t_saidas WHERE id = ? ");
		new ConexaoFactory();
		PreparedStatement comando = ConexaoFactory.conectar().prepareStatement(
				sql.toString());
 
		comando.setInt(1, sa.getId());
		comando.executeUpdate();
		comando.close();

	}

	public void salvar(T_entradas et) throws SQLException {
		
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO playpark.t_saidas(fk_id,fk_id_item");

		sql.append(") VALUES (?,?) ");
		
		Connection conexao = ConexaoFactory.conectar();
		new ConexaoFactory();
		PreparedStatement comando = ConexaoFactory.conectar().prepareStatement(
				sql.toString());

		comando.setInt(1, et.getId());
		comando.setInt(2, et.getCarrinho_combo().getId());

		comando.executeUpdate();
		comando.close();
		conexao.close();

		
	}


}
