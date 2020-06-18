package br.com.EdinhosPlayPark.dao;

import java.awt.print.PrinterException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;

import br.com.EdinhosPlayPark.domain.T_caixa;
import br.com.EdinhosPlayPark.factory.ConexaoFactory;

public class T_caixaDAO {

	public ArrayList<T_caixa> listar_fecha() throws SQLException {

		StringBuilder sql = new StringBuilder();
		sql.append(" select f.t_tipopagto,");
		sql.append(" sum(Case when it.t_tipo = 'MINUTAGEM' THEN");
		sql.append(" (SELECT P.VALOR FROM playpark.t_preco P WHERE (");
		sql.append(" SELECT right(left(");
		sql.append(" TIMEDIFF(CURTIME(),et1.t_time),5),2) as dif");
		sql.append(" FROM playpark.t_entradas et1");
		sql.append(" where et1.id_controle=et.id_controle");
		sql.append(" ) between p.t_minutos_ini and p.t_minutos)");
		sql.append(" when it.t_tipo = 'DIVERSOS' THEN");
		sql.append(" it.t_valor  END ) AS VALOR_tot");
		sql.append(" from playpark.t_saidas sd");
		sql.append(" inner join  playpark.t_itens it on (it.id = sd.fk_id_item)");
		sql.append(" inner join playpark.t_entradas et on (et.id = sd.fk_id)");
		sql.append(" inner join playpark.t_comanda_fechada f on (f.id_controle = et.id_controle)");
		sql.append(" where date(et.t_perini)=date(now()) and f.t_date_fecha is null ");
		sql.append(" group by f.t_tipopagto");

		Connection conexao = ConexaoFactory.conectar();

		PreparedStatement comando = conexao.prepareStatement(sql.toString());

		ResultSet resultado = comando.executeQuery();

		ArrayList<T_caixa> lista = new ArrayList<T_caixa>();

		while (resultado.next()) {

			T_caixa f = new T_caixa();

			f.setNome(resultado.getString("t_tipopagto"));
			f.setSvalor_tot("R$ " + resultado.getString("VALOR_tot"));
			lista.add(f);
		}
		comando.close();
		conexao.close();

		return lista;
	}

	public ArrayList<T_caixa> listar() throws SQLException {
		StringBuilder sql = new StringBuilder();

		sql.append(" select et.id_controle,et.t_nome_depend,");
		sql.append(" sum(Case when it.t_tipo = 'MINUTAGEM' THEN");
		sql.append(" (SELECT P.VALOR FROM playpark.t_preco P WHERE (");
		sql.append(" SELECT right(left(");
		sql.append(" TIMEDIFF(CURTIME(),et1.t_time),5),2) as dif");
		sql.append(" FROM playpark.t_entradas et1");
		sql.append(" where et1.id_controle=et.id_controle");
		sql.append(" ) between p.t_minutos_ini and p.t_minutos) else 0 end ) as valor_m,");
		sql.append("  sum(Case when it.t_tipo = 'DIVERSOS' THEN");
		sql.append(" it.t_valor  else 0 END ) as valor_div,");
		sql.append(" sum(Case when it.t_tipo = 'MINUTAGEM' THEN");
		sql.append(" (SELECT P.VALOR FROM playpark.t_preco P WHERE (");
		sql.append(" SELECT right(left(");
		sql.append(" TIMEDIFF(CURTIME(),et1.t_time),5),2) as dif");
		sql.append(" FROM playpark.t_entradas et1");
		sql.append(" where et1.id_controle=et.id_controle");
		sql.append(" ) between p.t_minutos_ini and p.t_minutos)");
		sql.append(" when it.t_tipo = 'DIVERSOS' THEN");
		sql.append(" it.t_valor  END ) AS VALOR_tot");
		sql.append(" from playpark.t_saidas sd");
		sql.append(" inner join  playpark.t_itens it on (it.id = sd.fk_id_item)");
		sql.append(" inner join playpark.t_entradas et on (et.id = sd.fk_id)");
		sql.append(" inner join playpark.t_comanda_fechada f on (f.id_controle = et.id_controle)");
		sql.append(" where date(et.t_perini)=date(now()) and f.t_date_fecha is null ");
		sql.append(" group by et.id_controle,et.t_nome_depend");
		sql.append(" order by et.id_controle");
		Connection conexao = ConexaoFactory.conectar();

		PreparedStatement comando = conexao.prepareStatement(sql.toString());

		ResultSet resultado = comando.executeQuery();

		ArrayList<T_caixa> lista = new ArrayList<T_caixa>();

		while (resultado.next()) {

			T_caixa f = new T_caixa();

			f.setId_controle(resultado.getLong("id_controle"));
			f.setNome(resultado.getString("t_nome_depend"));
			f.setSvalor_min("R$ " + resultado.getString("valor_m"));
			f.setSvalor_div("R$ " + resultado.getString("valor_div"));
			f.setSvalor_tot("R$ " + resultado.getString("VALOR_tot"));
			lista.add(f);
		}
		comando.close();
		conexao.close();

		return lista;
	}

	public void print_ticket() throws SQLException, PrinterException {
		try {
			// Localiza todas as impressoras com suporte a arquivos txt
			PrintService[] servicosImpressao = PrintServiceLookup
					.lookupPrintServices(DocFlavor.INPUT_STREAM.AUTOSENSE, null);

			// Localiza a impressora padrão
			PrintService impressora = PrintServiceLookup
					.lookupDefaultPrintService();

			// Definição de atributos do conteúdo a ser impresso:
			DocFlavor docFlavor = DocFlavor.INPUT_STREAM.AUTOSENSE;

			// Atributos de impressão do documento
			HashDocAttributeSet attributes = new HashDocAttributeSet();

			Date utilDate = null;
			utilDate = new Date();
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			SimpleDateFormat formatarDate = new SimpleDateFormat("yyyyMMdd"); 
			
			String sFileName = "C:\\ArquivosSite\\FechaCaixa" + formatarDate.format(sqlDate) + ".txt";

			// InputStream apontando para o conteúdo a ser impresso
			FileOutputStream fil = new FileOutputStream(sFileName);

			PrintStream p = new PrintStream(fil);

			p.print("PlayPark Diversoes \n\b");
			p.print("---------------------------------------- \n\b");
			p.print("Fecha Caixa Dia....: " + " \n\b");

			p.print("---------------------------------------- \n\b");

			StringBuilder sql = new StringBuilder();
			sql.append(" select et.id_controle,f.t_tipopagto,");
			sql.append(" sum(Case when it.t_tipo = 'MINUTAGEM' THEN");
			sql.append(" (SELECT P.VALOR FROM playpark.t_preco P WHERE (");
			sql.append(" SELECT right(left(");
			sql.append(" TIMEDIFF(CURTIME(),et1.t_time),5),2) as dif");
			sql.append(" FROM playpark.t_entradas et1");
			sql.append(" where et1.id_controle=et.id_controle");
			sql.append(" ) between p.t_minutos_ini and p.t_minutos)");
			sql.append(" when it.t_tipo = 'DIVERSOS' THEN");
			sql.append(" it.t_valor  END ) AS VALOR_tot");
			sql.append(" from playpark.t_saidas sd");
			sql.append(" inner join  playpark.t_itens it on (it.id = sd.fk_id_item)");
			sql.append(" inner join playpark.t_entradas et on (et.id = sd.fk_id)");
			sql.append(" inner join playpark.t_comanda_fechada f on (f.id_controle = et.id_controle)");
			sql.append(" where date(et.t_perini)=date(now()) ");
			sql.append(" group by et.id_controle,f.t_tipopagto");

			Connection conexao = ConexaoFactory.conectar();

			PreparedStatement comando = conexao
					.prepareStatement(sql.toString());

			ResultSet resultado = comando.executeQuery();

			while (resultado.next()) {
				p.print("Tipo de Pagamento......: "
						+ resultado.getString("t_tipopagto") + " \n\b");
				p.print("Valor..................: " + "R$ "
						+ resultado.getString("VALOR_tot") + " \n\b");

				StringBuilder sql_upd = new StringBuilder();
				
				sql_upd.append("UPDATE playpark.t_comanda_fechada SET ");
				sql_upd.append(" t_date_fecha = date(now()) ");
				sql_upd.append(" WHERE id_controle = "+resultado.getString("id_controle"));

				Connection con_upd = ConexaoFactory.conectar();

				PreparedStatement c_upd = conexao.prepareStatement(sql_upd.toString());
				c_upd.executeUpdate();
				c_upd.close();
				con_upd.close();
			
			}
			comando.close();
			conexao.close();

			FileInputStream fi = new FileInputStream(sFileName);

			// Cria um Doc para impressão a partir do arquivo exemplo.txt
			Doc documentoTexto = new SimpleDoc(fi, docFlavor, attributes);

			// Configura o conjunto de parametros para a impressora
			PrintRequestAttributeSet printerAttributes = new HashPrintRequestAttributeSet();
			DocPrintJob printJob = impressora.createPrintJob();
			printerAttributes.add(new Copies(1));
			printJob.print(documentoTexto, printerAttributes);

		} catch (IOException e) {
			// System.out.println("ERRO IO"+e.getMessage());
		} catch (PrintException ex2) {
			ex2.getMessage();
		}

	}

	public void editar(Long id_controle,String Data) throws SQLException {
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE playpark.t_comanda_fechada SET ");
		sql.append(" t_date_fecha = " + Data);
		sql.append(" WHERE id_controle = "+id_controle);

		Connection conexao = ConexaoFactory.conectar();
		PreparedStatement comando = conexao.prepareStatement(sql.toString());
		comando.executeUpdate();
		comando.close();
		conexao.close();

	}

	
}
