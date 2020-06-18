package br.com.EdinhosPlayPark.beans;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.EdinhosPlayPark.dao.Cbo_CarrinhoComprasDAO;
import br.com.EdinhosPlayPark.dao.T_itemDAO;
import br.com.EdinhosPlayPark.dao.T_saidasDAO;
import br.com.EdinhosPlayPark.domain.Cbo_CarrinhoCompras;
import br.com.EdinhosPlayPark.domain.T_entradas;
import br.com.EdinhosPlayPark.domain.T_item;
import br.com.EdinhosPlayPark.domain.T_saidas;
import br.com.EdinhosPlayPark.util.JSFUtil;

@ManagedBean(name = "MBConsumo")
@ViewScoped
public class T_consumoItemBean {

	private T_entradas entradas;
	private T_item item;
	private T_saidas saidas;
	private ArrayList<T_item> itens;
	private ArrayList<T_item> itenssaida;
	private ArrayList<Cbo_CarrinhoCompras> carrinhocombo;
	
	public T_entradas getEntradas() {
		return entradas;
	}

	public void setEntradas(T_entradas entradas) {
		this.entradas = entradas;
	}
	
	public T_saidas getSaidas() {
		return saidas;
	}

	public void setSaidas(T_saidas saidas) {
		this.saidas = saidas;
	}

	public ArrayList<Cbo_CarrinhoCompras> getCarrinhocombo() {
		return carrinhocombo;
	}

	public void setCarrinhocombo(ArrayList<Cbo_CarrinhoCompras> carrinhocombo) {
		this.carrinhocombo = carrinhocombo;
	}

	public T_item getItem() {
		return item;
	}

	public void setItem(T_item item) {
		this.item = item;
	}

	public ArrayList<T_item> getItens() {
		return itens;
	}

	public void setItens(ArrayList<T_item> itens) {
		this.itens = itens;
	}

	public ArrayList<T_item> getItenssaida() {
		return itenssaida;
	}

	public void setItenssaida(ArrayList<T_item> itenssaida) {
		this.itenssaida = itenssaida;
	}
 

	@PostConstruct
	public void prepararPesquisa() {
		item = new T_item();
	
	}

	public void listarconsumo() {
		try {
			T_itemDAO dao = new T_itemDAO ();
			itens = dao.listarconsumo(item);
		} catch (SQLException ex) {
			ex.printStackTrace();
			JSFUtil.adicionarMensagemErro("Erro  001 - " + ex.getMessage());
		}
	}
	
	
	public void excluir() {
		try {
			T_saidasDAO dao = new T_saidasDAO();
			dao.excluir(item);
			
			T_itemDAO dao1 = new T_itemDAO ();
			itens = dao1.listarconsumo(item);
			
			JSFUtil.adicionarMensagemSucesso("Excluido com Sucesso!");
		} catch (SQLException ex) {
			ex.printStackTrace();
			JSFUtil.adicionarMensagemErro("Erro Usuário 002 - "
					+ ex.getMessage());
		}

	}


	public void salvar() throws Exception {

		try {
			T_saidasDAO dao = new T_saidasDAO();
			dao.salvar(entradas);
			
			T_itemDAO dao1 = new T_itemDAO ();
			itens = dao1.listarconsumo(item);
			
			JSFUtil.adicionarMensagemSucesso("Salvo com Sucesso!");
		} catch (SQLException ex) {
			ex.printStackTrace();
			JSFUtil.adicionarMensagemErro("Erro Usuário 002 - "
					+ ex.getMessage());
		}

	}



}
