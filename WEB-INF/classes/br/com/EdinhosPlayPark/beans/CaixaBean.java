package br.com.EdinhosPlayPark.beans;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.EdinhosPlayPark.dao.T_caixaDAO;
import br.com.EdinhosPlayPark.domain.T_caixa;
import br.com.EdinhosPlayPark.util.JSFUtil;


@ManagedBean(name = "MBCaixa")
@ViewScoped
public class CaixaBean {

	private T_caixa caixa;
	private ArrayList<T_caixa> itens;
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
	
	@PostConstruct
	public void prepararPesquisa() {
		try {
			T_caixaDAO dao = new T_caixaDAO();
			itens = dao.listar();
		} catch (SQLException ex) {
			ex.printStackTrace();
			JSFUtil.adicionarMensagemErro("Erro 001 - " + ex.getMessage());
		}
	}

	
//	public void salvar() throws Exception {
//		try {
//			T_caixaDAO dao = new T_caixaDAO();
//			dao.salvar(caixa);
//			itens = dao.listar();
//			JSFUtil.adicionarMensagemSucesso("Salvo com Sucesso!");
//		} catch (SQLException ex) {
//			ex.printStackTrace();
//			JSFUtil.adicionarMensagemErro("Erro Usuário 002 - "
//					+ ex.getMessage());
//		}
//
//	}
//	public void excluir() {
//
//		try {
//			T_caixaDAO dao = new T_caixaDAO();
//			dao.excluir(caixa);
//			itens = dao.listar();
//			JSFUtil.adicionarMensagemSucesso("Excluido com Sucesso!");
//		} catch (SQLException ex) {
//			ex.printStackTrace();
//			JSFUtil.adicionarMensagemErro("Erro Usuário 002 - "
//					+ ex.getMessage());
//		}
//
//	}


}
