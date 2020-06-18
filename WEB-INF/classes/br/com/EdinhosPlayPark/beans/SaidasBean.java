package br.com.EdinhosPlayPark.beans;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.EdinhosPlayPark.dao.Cbo_CarrinhoComprasDAO;
import br.com.EdinhosPlayPark.dao.T_saidasDAO;
import br.com.EdinhosPlayPark.domain.Cbo_CarrinhoCompras;
import br.com.EdinhosPlayPark.domain.T_entradas;
import br.com.EdinhosPlayPark.domain.T_item;
import br.com.EdinhosPlayPark.domain.T_preco;
import br.com.EdinhosPlayPark.domain.T_saidas;
import br.com.EdinhosPlayPark.util.JSFUtil;


@ManagedBean(name = "MBSaidas")
@ViewScoped
public class SaidasBean {

	private ArrayList<T_item> itenssaida;
	private T_entradas entradas;
	private T_item items;
	private T_saidas saidas;
	private T_preco preco;
	private ArrayList<T_item> itens;
	private ArrayList<Cbo_CarrinhoCompras> carrinhocombo;

	public T_item getItems() {
		return items;
	}

	public void setItems(T_item items) {
		this.items = items;
	}

	public ArrayList<T_item> getItenssaida() {
		return itenssaida;
	}

	public void setItenssaida(ArrayList<T_item> itenssaida) {
		this.itenssaida = itenssaida;
	}
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

	public T_preco getPreco() {
		return preco;
	}

	public void setPreco(T_preco preco) {
		this.preco = preco;
	}

	public ArrayList<T_item> getItens() {
		return itens;
	}

	public void setItens(ArrayList<T_item> itens) {
		this.itens = itens;
	}

	public ArrayList<Cbo_CarrinhoCompras> getCarrinhocombo() {
		return carrinhocombo;
	}

	public void setCarrinhocombo(ArrayList<Cbo_CarrinhoCompras> carrinhocombo) {
		this.carrinhocombo = carrinhocombo;
	}

	@PostConstruct
	public void prepararPesquisa() {
		items=new T_item();		
	}
	
//	public void prepararNovo() {
//		saidas = new T_saidas();
//		
//		Cbo_CarrinhoComprasDAO dao1 = new Cbo_CarrinhoComprasDAO();
//		try {
//			carrinhocombo = dao1.listar();
//		} catch (SQLException ex) {
//			ex.printStackTrace();
//			JSFUtil.adicionarMensagemErro(ex.getMessage());
//		}
//
//	}

	public void excluir() {
		T_saidasDAO dao = new T_saidasDAO();
		try {
			
			dao.excluir(items);

			JSFUtil.adicionarMensagemSucesso("Excluido com Sucesso!");
		} catch (SQLException ex) {
			ex.printStackTrace();
			JSFUtil.adicionarMensagemErro("Erro Usuário 002 - "
					+ ex.getMessage());
		}

	}
	
//	public void salvar_s() throws Exception {
//		
//		try {
//			T_saidasDAO dao = new T_saidasDAO();
////			dao.salvar_s(saidas,items);
//			// itens = dao.listar(entradas);
//			JSFUtil.adicionarMensagemSucesso("Salvo com Sucesso!");
//		} catch (SQLException ex) {
//			ex.printStackTrace();
//			JSFUtil.adicionarMensagemErro("Erro Usuário 002 - "
//					+ ex.getMessage());
//		}
//
//	}



}
