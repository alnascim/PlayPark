package br.com.EdinhosPlayPark.dao;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import br.com.EdinhosPlayPark.domain.T_entradas;
import br.com.EdinhosPlayPark.domain.T_preco;
import br.com.EdinhosPlayPark.factory.ConexaoFactory;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.Barcode;
import com.lowagie.text.pdf.BarcodeEAN;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

public class T_entradasDAO {

	public String getvalor(Long Id_Controle) throws SQLException {
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT T.ID,T.ID_CONTROLE,T.PERINI,T.PERFIM,T.TIMEOVER,T.t_nome,T.dif,T.depend, ");

		sql.append("replace(replace(replace(format((SELECT P.VALOR FROM playpark.t_preco P WHERE ( ");
		sql.append("SELECT right(left( ");
		sql.append("TIMEDIFF(CURTIME(),et1.t_time),5),2) as dif ");
		sql.append("FROM playpark.t_entradas et1  ");
		sql.append("where et1.id_controle=t.id_controle ");
		sql.append(") between p.t_minutos_ini and p.t_minutos),2), '.','|'),',','.'),'|',',') AS VALOR FROM ( ");

		sql.append("SELECT ");
		sql.append("a.id,");
		sql.append("a.id_controle,");
		sql.append("a.t_nome,right(t_perini,8) as perini,right(t_perfim,8) as perfim, ");
		sql.append("Case when t_Perfim < now() then 'S' Else 'N' end as timeover, ");

		sql.append("TIME_TO_SEC((TIMEDIFF(CURTIME(), date_format(t_perfim,'%h:%m:%s'))) )/60 as dif,a.t_nome_depend as depend ");

		sql.append("from playpark.t_entradas a where t_Perfim < now() ");

		sql.append("and not exists (select aa.id_controle from playpark.t_comanda_fechada aa where aa.id_controle = a.id_controle ) ");

		sql.append(" union ");

		sql.append("SELECT ");
		sql.append("a.id,");
		sql.append("a.id_controle,");
		sql.append("a.t_nome,right(t_perini,8) as perini,right(t_perfim,8) as perfim, ");
		sql.append("Case when t_Perfim < now() then 'S' Else 'N' end as timeover,");

		sql.append("TIME_TO_SEC((TIMEDIFF(CURTIME(), date_format(t_perfim,'%h:%m:%s'))) )/60 as dif,a.t_nome_depend as depend ");

		sql.append(" from playpark.t_entradas a where t_Perfim >= now() and not exists (select aa.id_controle from playpark.t_comanda_fechada aa where aa.id_controle = a.id_controle )) AS T ");
		sql.append(" inner join playpark.t_saidas sd on (sd.fk_id = t.id) ");

		sql.append(" where T.ID_CONTROLE = " + Id_Controle);

		Connection conexao = ConexaoFactory.conectar();

		PreparedStatement comando = conexao.prepareStatement(sql.toString());

		ResultSet resultado = comando.executeQuery();

		String sgetvalor = null;
		while (resultado.next()) {

			sgetvalor = "R$ " + resultado.getString("valor");
		}
		comando.close();
		conexao.close();

		return sgetvalor;
	}

	public ArrayList<T_entradas> listar() throws SQLException {
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT t.t_minutos,T.ID,T.ID_CONTROLE,T.PERINI,T.PERFIM,T.TIMEOVER,T.t_nome,T.dif,T.depend, ");

		sql.append("replace(replace(replace(format(SUM(Case when it.t_tipo = 'MINUTAGEM' THEN  ");
		sql.append("(SELECT P.VALOR FROM playpark.t_preco P WHERE ( ");
		sql.append("SELECT right(left( ");
		sql.append("TIMEDIFF(CURTIME(),et1.t_time),5),2) as dif ");
		sql.append("FROM playpark.t_entradas et1  ");
		sql.append("where et1.id_controle=t.id_controle ");
		sql.append(") between p.t_minutos_ini and p.t_minutos) else 0 end),2), '.','|'),',','.'),'|',',') AS VALOR_M,");
		sql.append("replace(replace(replace(format(SUM(Case when it.t_tipo = 'DIVERSOS' THEN it.t_valor  else 0 END),2), '.','|'),',','.'),'|',',') as VALOR_DIV,");

		sql.append("replace(replace(replace(format(SUM(Case when it.t_tipo = 'MINUTAGEM' THEN  ");
		sql.append("(SELECT P.VALOR FROM playpark.t_preco P WHERE ( ");
		sql.append("SELECT right(left( ");
		sql.append("TIMEDIFF(CURTIME(),et1.t_time),5),2) as dif ");
		sql.append("FROM playpark.t_entradas et1  ");
		sql.append("where et1.id_controle=t.id_controle ");
		sql.append(") between p.t_minutos_ini and p.t_minutos) else 0 end) +");
		sql.append("SUM(Case when it.t_tipo = 'DIVERSOS' THEN it.t_valor  else 0 END),2), '.','|'),',','.'),'|',',') as VALOR_TOT ");

		sql.append(" FROM (  SELECT ");
		sql.append("a.id,a.t_minutos,");
		sql.append("a.id_controle,");
		sql.append("a.t_nome,right(t_perini,8) as perini,right(t_perfim,8) as perfim, ");
		sql.append("Case when t_Perfim < now() then 'S' Else 'N' end as timeover, ");

		sql.append("TIME_TO_SEC((TIMEDIFF(CURTIME(), date_format(t_perfim,'%h:%m:%s'))) )/60 as dif,a.t_nome_depend as depend ");

		sql.append("from playpark.t_entradas a where t_Perfim < now() ");

		sql.append("and not exists (select aa.id_controle from playpark.t_comanda_fechada aa where aa.id_controle = a.id_controle ) ");

		sql.append(" union ");

		sql.append("SELECT ");
		sql.append("a.id,a.t_minutos,");
		sql.append("a.id_controle,");
		sql.append("a.t_nome,right(t_perini,8) as perini,right(t_perfim,8) as perfim, ");
		sql.append("Case when t_Perfim < now() then 'S' Else 'N' end as timeover,");

		sql.append("TIME_TO_SEC((TIMEDIFF(CURTIME(), date_format(t_perfim,'%h:%m:%s'))) )/60 as dif,a.t_nome_depend as depend ");

		sql.append(" from playpark.t_entradas a where t_Perfim >= now() and not exists (select aa.id_controle from playpark.t_comanda_fechada aa where aa.id_controle = a.id_controle )) AS T ");
		sql.append(" inner join playpark.t_saidas sd on (sd.fk_id = t.id) ");
		sql.append(" inner join  playpark.t_itens it on (it.id = sd.fk_id_item) ");

		sql.append(" GROUP BY T.ID,T.ID_CONTROLE,T.PERINI,T.PERFIM,T.TIMEOVER,T.t_nome,T.dif,T.depend ");
		sql.append("ORDER BY t.TIMEOVER DESC,T.ID_CONTROLE ASC ");

		Connection conexao = ConexaoFactory.conectar();

		PreparedStatement comando = conexao.prepareStatement(sql.toString());

		ResultSet resultado = comando.executeQuery();

		ArrayList<T_entradas> lista = new ArrayList<T_entradas>();

		while (resultado.next()) {

			T_entradas f = new T_entradas();

			f.setId(resultado.getInt("id"));
			f.setId_controle(resultado.getLong("id_controle"));
			f.setNome(resultado.getString("t_nome"));

			f.setSperini(resultado.getString("perini"));
			f.setSperfim(resultado.getString("perfim"));

			f.setTimeover(resultado.getString("timeover"));
			f.setSdepend(resultado.getString("depend"));
			f.setSticket(resultado.getString("id_controle"));

			f.setSvalor_m("R$ " + resultado.getString("VALOR_M"));
			f.setSvalor_d("R$ " + resultado.getString("VALOR_DIV"));
			f.setsTotal("R$ " + resultado.getString("VALOR_TOT"));

			f.setMinutos(resultado.getInt("t_minutos"));

			lista.add(f);
		}
		comando.close();
		conexao.close();

		return lista;
	}

	public ArrayList<T_preco> list_values() throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append("a.id,");
		sql.append("A.t_minutos as minutos,");
		sql.append("a.t_tempo,");
		sql.append("replace(replace(replace(format(a.valor ,2), '.','|'),',','.'),'|',',') as valor ");
		sql.append("from playpark.t_preco a");
		Connection conexao = ConexaoFactory.conectar();

		PreparedStatement comando = conexao.prepareStatement(sql.toString());

		ResultSet resultado = comando.executeQuery();

		ArrayList<T_preco> lista = new ArrayList<T_preco>();

		while (resultado.next()) {

			T_preco f = new T_preco();

			f.setId(resultado.getInt("id"));
			f.setT_tempo(resultado.getString("t_tempo"));
			f.setSvalor("R$ " + resultado.getString("valor"));
			f.setMinutos(resultado.getInt("minutos"));
			lista.add(f);
		}
		comando.close();
		conexao.close();

		return lista;
	}

	public void salvar(T_entradas f) throws SQLException {

		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO playpark.t_entradas(id_controle,t_nome,t_PerIni,t_Perfim,t_minutos,t_nome_depend,t_data_nasc");

		sql.append(") VALUES (?,?,?,?,?,?,?) ");

		Connection conexao = ConexaoFactory.conectar();
		new ConexaoFactory();
		PreparedStatement comando = ConexaoFactory.conectar().prepareStatement(
				sql.toString());

		comando.setLong(1, f.getId_controle());
		comando.setString(2, f.getNome());
		comando.setString(3, f.getSperini());
		comando.setString(4, f.getSperfim());
		comando.setInt(5, f.getMinutos());
		comando.setString(6, f.getSdepend());

		Date utilDate1 = null;
		try {
			utilDate1 = f.getDdata_nasc();
			java.sql.Date sqlDate1 = new java.sql.Date(utilDate1.getTime());
			comando.setDate(7, sqlDate1);
		} catch (Exception e) {

			comando.setDate(7, null);
		}

		comando.executeUpdate();
		comando.close();
		conexao.close();

	}

	public void update_time(T_entradas f) throws SQLException {

		StringBuilder sql = new StringBuilder();
		sql.append("update playpark.t_entradas set t_time = right(t_perfim,8) where id_controle = ? ");
		new ConexaoFactory();
		PreparedStatement comando = ConexaoFactory.conectar().prepareStatement(
				sql.toString());

		comando.setLong(1, f.getId_controle());
		comando.executeUpdate();
		comando.close();
	}

	public void print_barcode(T_entradas f) throws PrinterException,
			IOException {
		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		String sFileNamepdf = "C:\\ArquivosSite\\CodigoBarra.pdf";
		try {

			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(sFileNamepdf));
			document.open();

			PdfContentByte cb = writer.getDirectContent();
			BarcodeEAN codeEAN = new BarcodeEAN();
			codeEAN.setCodeType(Barcode.EAN13);
			codeEAN.setCode(f.getSticket());
			Image imageEAN = codeEAN.createImageWithBarcode(cb, null, null);

			document.add(new Phrase(new Chunk(imageEAN, 0, 0)));
		}

		catch (Exception de) {

			de.printStackTrace();

		}

		document.close();
	}

	public void reprint_ticket(T_entradas f) throws SQLException,
			PrinterException {
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

			String sFileName = "C:\\ArquivosSite\\Ticket" + f.getId_controle()
					+ ".txt";

			// InputStream apontando para o conteúdo a ser impresso
			FileOutputStream fil = new FileOutputStream(sFileName);

			PrintStream p = new PrintStream(fil);

			p.print("PlayPark Diversoes \n\b");
			p.print("---------------------------------------- \n\b");
			p.print("Ticket Nro......: " + f.getId_controle() + " \n\b");
			p.print("Valor Inicial...: " + getvalor(f.getId_controle())
					+ " \n\b");

			p.print("---------------------------------------- \n\b");

			p.print("Nome do Responsavel    : " + f.getNome() + " \n\b");
			p.print("Nome da Crianca        : " + f.getSdepend() + " \n\b");
			p.print("Data Nascimento        : " + f.getDdata_nasc() + " \n\b");
			p.print("Tempo de Recreacao     : " + f.getMinutos()
					+ " Minutos \n\b");
			p.print("Horario de Entrada     : " + f.getSperini() + " \n\b");
			p.print("Horario de Saida       : " + f.getSperfim() + " \n\b");
			p.print("---------------------------------------- \n\b");

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

	public void print_ticket(T_entradas f) throws SQLException,
			PrinterException {
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

			String sFileName = "C:\\ArquivosSite\\Ticket" + f.getId_controle()
					+ ".txt";

			// InputStream apontando para o conteúdo a ser impresso
			FileOutputStream fil = new FileOutputStream(sFileName);

			PrintStream p = new PrintStream(fil);

			p.print("PlayPark Diversoes \n\b");
			p.print("---------------------------------------- \n\b");
			p.print("Ticket Nro......: " + f.getId_controle() + " \n\b");
			p.print("Valor Inicial...: " + getvalor(f.getId_controle())
					+ " \n\b");

			p.print("---------------------------------------- \n\b");

			p.print("Nome do Responsavel    : " + f.getNome() + " \n\b");
			p.print("Nome da Crianca        : " + f.getSdepend() + " \n\b");
			p.print("Data Nascimento        : " + f.getDdata_nasc() + " \n\b");
			p.print("Tempo de Recreacao     : " + f.getMinutos()
					+ " Minutos \n\b");
			p.print("Horario de Entrada     : " + f.getSperini() + " \n\b");
			p.print("Horario de Saida       : " + f.getSperfim() + " \n\b");
			p.print("---------------------------------------- \n\b");

			FileInputStream fi = new FileInputStream(sFileName);

			// Cria um Doc para impressão a partir do arquivo exemplo.txt
			Doc documentoTexto = new SimpleDoc(fi, docFlavor, attributes);

			// Configura o conjunto de parametros para a impressora
			PrintRequestAttributeSet printerAttributes = new HashPrintRequestAttributeSet();
			DocPrintJob printJob = impressora.createPrintJob();
			printerAttributes.add(new Copies(1));
			printJob.print(documentoTexto, printerAttributes);

			String sFileNamepdf = "C:\\ArquivosSite\\CodigoBarra.pdf";
			PDDocument documento = null;
			documento = PDDocument.load(new File(sFileNamepdf));
			PrintService servico = PrintServiceLookup
					.lookupDefaultPrintService();

			PrinterJob job = PrinterJob.getPrinterJob();
			job.setPageable(new PDFPageable(documento));
			job.setPrintService(servico);
			job.print();
			documento.close();

		} catch (IOException e) {
			// System.out.println("ERRO IO"+e.getMessage());
		} catch (PrintException ex2) {
			ex2.getMessage();
		}

	}

	public void salvar_saidas(T_entradas f) throws SQLException {

		// ***********************************************************************
		// Recupera a FK da tabela t_itens para relacionar com tabela T_saidas
		StringBuilder sqlitem = new StringBuilder();
		int id_fk_item = 0;
		Connection conitem = ConexaoFactory.conectar();

		sqlitem.append("SELECT id FROM playpark.t_itens where t_tipo = 'MINUTAGEM'");
		PreparedStatement comitem = conitem
				.prepareStatement(sqlitem.toString());

		ResultSet resitem = comitem.executeQuery();

		while (resitem.next()) {

			id_fk_item = resitem.getInt("id");
		}

		comitem.close();
		conitem.close();
		// ***********************************************************************

		// ***********************************************************************
		// Recupera a FK da tabela t_entradas para relacionar com tabela
		// T_saidas
		StringBuilder sqlrec = new StringBuilder();
		int id_fk = 0;
		Connection con = ConexaoFactory.conectar();

		sqlrec.append("SELECT id FROM playpark.t_entradas where id_controle = "
				+ f.getId_controle());
		PreparedStatement com = con.prepareStatement(sqlrec.toString());

		ResultSet res = com.executeQuery();

		while (res.next()) {

			id_fk = res.getInt("id");
		}

		com.close();
		con.close();
		// ***********************************************************************

		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO playpark.t_saidas(fk_id,fk_id_item");

		sql.append(") VALUES (?,?) ");

		Connection conexao = ConexaoFactory.conectar();
		PreparedStatement comando = ConexaoFactory.conectar().prepareStatement(
				sql.toString());

		comando.setLong(1, id_fk);
		comando.setLong(2, id_fk_item);

		comando.executeUpdate();
		comando.close();
		conexao.close();

	}

	public void recuperar(T_entradas f) throws SQLException {
		StringBuilder sql = new StringBuilder();

		Connection conexao = ConexaoFactory.conectar();

//		sql.append("select CONCAT(LEFT(REPLACE(REPLACE(REPLACE(CURRENT_TIMESTAMP(),'-',''),':',''),' ',''),12),RIGHT(REPLACE(REPLACE(REPLACE(CURRENT_TIMESTAMP(),'-',''),':',''),' ',''),1)) AS id_controle");

		
		sql.append("select Case when (select count(*) from playpark.t_entradas) = 0 then CONCAT(LEFT(REPLACE(REPLACE(REPLACE(CURRENT_TIMESTAMP(),'-',''),':',''),' ',''),12),RIGHT(REPLACE(REPLACE(REPLACE(CURRENT_TIMESTAMP(),'-',''),':',''),' ',''),1)) else (select max(id_controle)+1 from playpark.t_entradas)  end as id_controle "); 
		
		
		PreparedStatement comando = conexao.prepareStatement(sql.toString());

		ResultSet resultado = comando.executeQuery();

		while (resultado.next()) {

			f.setId_controle(resultado.getLong("id_controle"));
			f.setSticket(resultado.getString("id_controle"));
		}

		comando.close();
		conexao.close();

	}

	public void setminutagem(T_entradas f, T_preco p) throws SQLException {

		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		f.setSperini(sdf.format(gc.getTime()));
		gc.add(Calendar.MINUTE, p.getMinutos());
		f.setSperfim(sdf.format(gc.getTime()));
		f.setMinutos(p.getMinutos());

	}

	public void excluir(T_entradas f) throws SQLException {

		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM playpark.t_entradas WHERE id = ? ");
		new ConexaoFactory();
		PreparedStatement comando = ConexaoFactory.conectar().prepareStatement(
				sql.toString());

		comando.setInt(1, f.getId());
		comando.executeUpdate();
		comando.close();

	}

}
