package br.com.EdinhosPlayPark.beans;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.EdinhosPlayPark.dao.T_itemDAO;
import br.com.EdinhosPlayPark.domain.T_item;
import br.com.EdinhosPlayPark.util.JSFUtil;

@ManagedBean(name = "MBItens")
@ViewScoped
public class T_itemBean {

	private T_item novositens;
	private ArrayList<T_item> itens;
	private ArrayList<T_item> itenssaida;
 

	public ArrayList<T_item> getItenssaida() {
		return itenssaida;
	}

	public void setItenssaida(ArrayList<T_item> itenssaida) {
		this.itenssaida = itenssaida;
	}

	public T_item getNovositens() {
		return novositens;
	}

	public void setNovositens(T_item novositens) {
		this.novositens = novositens;
	}

	public ArrayList<T_item> getItens() {
		return itens;
	}

	public void setItens(ArrayList<T_item> itens) {
		this.itens = itens;
	}

	@PostConstruct
	public void prepararPesquisa() {
		try {
			T_itemDAO dao = new T_itemDAO();
			itens = dao.listar();
		} catch (SQLException ex) {
			ex.printStackTrace();
			JSFUtil.adicionarMensagemErro("Erro 001 - " + ex.getMessage());
		}
	}

	public void prepararNovo() {
		novositens = new T_item();
	}

	public void excluir() {
		try {
			T_itemDAO dao = new T_itemDAO();
			dao.excluir(novositens);
			itens = dao.listar();
			JSFUtil.adicionarMensagemSucesso("Excluido com Sucesso!");
		} catch (SQLException ex) {
			ex.printStackTrace();
			JSFUtil.adicionarMensagemErro("Erro Usuário 002 - "
					+ ex.getMessage());
		}

	}

//	public void excluir_s() {
//
//		try {
//			T_itemDAO dao = new T_itemDAO();
//			dao.excluir_s(novositens);
//
//			T_entradasDAO dao1 = new T_entradasDAO();
//			itenssaida = dao1.listarsaidas(entradas);
//
//			JSFUtil.adicionarMensagemSucesso("Excluido com Sucesso!");
//		} catch (SQLException ex) {
//			ex.printStackTrace();
//			JSFUtil.adicionarMensagemErro("Erro Usuário 002 - "
//					+ ex.getMessage());
//		}
//
//	}

	public void salvar() throws Exception {

		try {
			T_itemDAO dao = new T_itemDAO();
			dao.salvar(novositens);
			dao.salvar_minut();
			itens = dao.listar();
			JSFUtil.adicionarMensagemSucesso("Salvo com Sucesso!");
		} catch (SQLException ex) {
			ex.printStackTrace();
			JSFUtil.adicionarMensagemErro("Erro Usuário 002 - "
					+ ex.getMessage());
		}

	}

}
