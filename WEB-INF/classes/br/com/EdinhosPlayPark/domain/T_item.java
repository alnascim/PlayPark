package br.com.EdinhosPlayPark.domain;

import java.math.BigDecimal;

public class T_item {

	private int id;
	private long id_controle ;
	private int fk_id;
	private String t_nome_item;
	private String st_valor;
	private String st_valor_atual;
	private String Tipo_pagto;
	private BigDecimal dt_valor  = BigDecimal.ZERO;
	private Cbo_CarrinhoCompras carrinho_combo = new Cbo_CarrinhoCompras();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getT_nome_item() {
		return t_nome_item;
	}
	public void setT_nome_item(String t_nome_item) {
		this.t_nome_item = t_nome_item;
	}
	public String getSt_valor() {
		return st_valor;
	}
	public void setSt_valor(String st_valor) {
		this.st_valor = st_valor;
	}
	public BigDecimal getDt_valor() {
		return dt_valor;
	}
	public void setDt_valor(BigDecimal dt_valor) {
		this.dt_valor = dt_valor;
	}
	public int getFk_id() {
		return fk_id;
	}
	public void setFk_id(int fk_id) {
		this.fk_id = fk_id;
	}
	public Cbo_CarrinhoCompras getCarrinho_combo() {
		return carrinho_combo;
	}
	public void setCarrinho_combo(Cbo_CarrinhoCompras carrinho_combo) {
		this.carrinho_combo = carrinho_combo;
	}
	public long getId_controle() {
		return id_controle;
	}
	public void setId_controle(long id_controle) {
		this.id_controle = id_controle;
	}
	public String getSt_valor_atual() {
		return st_valor_atual;
	}
	public void setSt_valor_atual(String st_valor_atual) {
		this.st_valor_atual = st_valor_atual;
	}
	public String getTipo_pagto() {
		return Tipo_pagto;
	}
	public void setTipo_pagto(String tipo_pagto) {
		Tipo_pagto = tipo_pagto;
	}


}
