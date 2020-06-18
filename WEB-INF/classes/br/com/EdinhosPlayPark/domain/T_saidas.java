package br.com.EdinhosPlayPark.domain;

import java.math.BigDecimal;

public class T_saidas {

	private int id;
	private int fk_id;
	private String t_nome_item;
	private String svalor;
	private BigDecimal valor  = BigDecimal.ZERO;
	private Cbo_CarrinhoCompras carrinho_combo = new Cbo_CarrinhoCompras();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFk_id() {
		return fk_id;
	}
	public void setFk_id(int fk_id) {
		this.fk_id = fk_id;
	}
	public String getSvalor() {
		return svalor;
	}
	public void setSvalor(String svalor) {
		this.svalor = svalor;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public Cbo_CarrinhoCompras getCarrinho_combo() {
		return carrinho_combo;
	}
	public void setCarrinho_combo(Cbo_CarrinhoCompras carrinho_combo) {
		this.carrinho_combo = carrinho_combo;
	}
	public String getT_nome_item() {
		return t_nome_item;
	}
	public void setT_nome_item(String t_nome_item) {
		this.t_nome_item = t_nome_item;
	}

	
	

}
