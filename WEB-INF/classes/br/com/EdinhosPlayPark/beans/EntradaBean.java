package br.com.EdinhosPlayPark.beans;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.EdinhosPlayPark.dao.Cbo_CarrinhoComprasDAO;
import br.com.EdinhosPlayPark.dao.T_entradasDAO;
import br.com.EdinhosPlayPark.dao.T_itemDAO;
import br.com.EdinhosPlayPark.dao.T_saidasDAO;
import br.com.EdinhosPlayPark.domain.Cbo_CarrinhoCompras;
import br.com.EdinhosPlayPark.domain.T_entradas;
import br.com.EdinhosPlayPark.domain.T_item;
import br.com.EdinhosPlayPark.domain.T_preco;
import br.com.EdinhosPlayPark.domain.T_saidas;
import br.com.EdinhosPlayPark.util.JSFUtil;

@ManagedBean(name = "MBEntradas")
@ViewScoped
public class EntradaBean {

	
	private T_item item;	
	private T_saidas saidas;
	private T_entradas entradas;
	private T_preco preco;
	private ArrayList<T_entradas> itens;
	private ArrayList<T_item> itenssaida;
	private ArrayList<T_preco> list_values;
	private ArrayList<Cbo_CarrinhoCompras> carrinhocombo;

	public ArrayList<Cbo_CarrinhoCompras> getCarrinhocombo() {
		return carrinhocombo;
	}

	public void setCarrinhocombo(ArrayList<Cbo_CarrinhoCompras> carrinhocombo) {
		this.carrinhocombo = carrinhocombo;
	}

	public T_saidas getSaidas() {
		return saidas;
	}

	public void setSaidas(T_saidas saidas) {
		this.saidas = saidas;
	}

	public ArrayList<T_item> getItenssaida() {
		return itenssaida;
	}

	public void setItenssaida(ArrayList<T_item> itenssaida) {
		this.itenssaida = itenssaida;
	}

	public T_preco getPreco() {
		return preco;
	}

	public void setPreco(T_preco preco) {
		this.preco = preco;
	}

	public ArrayList<T_preco> getList_values() {
		return list_values;
	}

	public void setList_values(ArrayList<T_preco> list_values) {
		this.list_values = list_values;
	}

	public T_entradas getEntradas() {
		return entradas;
	}

	public void setEntradas(T_entradas entradas) {
		this.entradas = entradas;
	}

	public ArrayList<T_entradas> getItens() {
		return itens;
	}

	public void setItens(ArrayList<T_entradas> itens) {
		this.itens = itens;
	}

	@PostConstruct
	public void prepararPesquisa() {
		try {
			T_entradasDAO dao = new T_entradasDAO();
			itens = dao.listar();
			list_values = dao.list_values();
			entradas = new T_entradas();
			dao.recuperar(entradas);
		} catch (SQLException ex) {
			ex.printStackTrace();
			JSFUtil.adicionarMensagemErro("Erro 001 - " + ex.getMessage());
		}
	}
	public void fechar_comanda() throws Exception {

		try {
			T_itemDAO dao = new T_itemDAO ();
			dao.fechar_comanda(entradas);
		} catch (SQLException ex) {
			ex.printStackTrace();
			JSFUtil.adicionarMensagemErro("Erro  001 - " + ex.getMessage());
		}

	}
	
	public void prepararNovo() {
		saidas = new T_saidas();
		Cbo_CarrinhoComprasDAO dao1 = new Cbo_CarrinhoComprasDAO();
		try {
			carrinhocombo = dao1.listar(entradas,saidas);
		} catch (SQLException ex) {
			ex.printStackTrace();
			JSFUtil.adicionarMensagemErro(ex.getMessage());
		}
	}
	public void listarsaidas() {

		try {
			T_saidasDAO dao = new T_saidasDAO();
			itenssaida = dao.listarsaidas(entradas);

		} catch (SQLException ex) {
			ex.printStackTrace();
			JSFUtil.adicionarMensagemErro("Erro 001 - " + ex.getMessage());
		}
	}

	public void setminutagem() {
		try {
			T_entradasDAO dao = new T_entradasDAO();
			dao.setminutagem(entradas, preco);
		} catch (SQLException ex) {
			ex.printStackTrace();
			JSFUtil.adicionarMensagemErro("Erro 001 - " + ex.getMessage());
		}
	}

	public void salvar() throws Exception {
		try {
			T_entradasDAO dao = new T_entradasDAO();
			dao.salvar(entradas);
			dao.update_time(entradas);
			dao.salvar_saidas(entradas);
			itens = dao.listar();
			dao.print_barcode(entradas);
			dao.print_ticket(entradas);
			JSFUtil.adicionarMensagemSucesso("Salvo com Sucesso!");
		} catch (SQLException ex) {
			ex.printStackTrace();
			JSFUtil.adicionarMensagemErro("Erro Usuário 002 - "
					+ ex.getMessage());
		}

	}
	public void salvar_saidas() throws Exception {
		try {
			T_entradasDAO dao = new T_entradasDAO();
			T_saidasDAO dao1 = new T_saidasDAO();
			dao1.salvar(entradas);
		
			itens = dao.listar();
			
			JSFUtil.adicionarMensagemSucesso("Salvo com Sucesso!");
		} catch (SQLException ex) {
			ex.printStackTrace();
			JSFUtil.adicionarMensagemErro("Erro Usuário 002 - "
					+ ex.getMessage());
		}

	}
	public void excluir() {

		try {
			T_entradasDAO dao = new T_entradasDAO();
			dao.excluir(entradas);
			itens = dao.listar();
			JSFUtil.adicionarMensagemSucesso("Excluido com Sucesso!");
		} catch (SQLException ex) {
			ex.printStackTrace();
			JSFUtil.adicionarMensagemErro("Erro Usuário 002 - "
					+ ex.getMessage());
		}

	}

}
