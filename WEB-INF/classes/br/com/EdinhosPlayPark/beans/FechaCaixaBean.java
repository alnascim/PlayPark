package br.com.EdinhosPlayPark.beans;

import java.awt.print.PrinterException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.EdinhosPlayPark.dao.T_caixaDAO;
import br.com.EdinhosPlayPark.domain.T_caixa;
import br.com.EdinhosPlayPark.util.JSFUtil;


@ManagedBean(name = "MBFechaCaixa")
@ViewScoped
public class FechaCaixaBean {

	private T_caixa caixa;
	private ArrayList<T_caixa> itens;
	private ArrayList<T_caixa> itens_fecha;

	public ArrayList<T_caixa> getItens_fecha() {
		return itens_fecha;
	}

	public void setItens_fecha(ArrayList<T_caixa> itens_fecha) {
		this.itens_fecha = itens_fecha;
	}

	public T_caixa getCaixa() {
		return caixa;
	}

	public void setCaixa(T_caixa caixa) {
		this.caixa = caixa;
	}

	public ArrayList<T_caixa> getItens() {
		return itens;
	}

	public void setItens(ArrayList<T_caixa> itens) {
		this.itens = itens;
	}
	
	public void prepararNovo() {
		T_caixaDAO dao = new T_caixaDAO();
		try {
			itens_fecha=dao.listar_fecha();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void salvar() throws Exception {
		try {
			T_caixaDAO dao = new T_caixaDAO();
//			dao.salvar(caixa);
			itens = dao.listar();
			JSFUtil.adicionarMensagemSucesso("Salvo com Sucesso!");
		} catch (SQLException ex) {
			ex.printStackTrace();
			JSFUtil.adicionarMensagemErro("Erro Usuário 002 - "
					+ ex.getMessage());
		}

	}
	public void print_ticket() throws PrinterException {

		try {
			T_caixaDAO dao = new T_caixaDAO();
			dao.print_ticket();
		} catch (SQLException ex) {
			ex.printStackTrace();
			JSFUtil.adicionarMensagemErro("Erro Usuário 002 - "
					+ ex.getMessage());
		}

	}



}
