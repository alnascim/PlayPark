package br.com.EdinhosPlayPark.domain;

import java.math.BigDecimal;

public class T_preco {

	private int id;
	private int minutos;
	private String t_tempo;
	private String svalor;
	private BigDecimal valor  = BigDecimal.ZERO;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getT_tempo() {
		return t_tempo;
	}
	public void setT_tempo(String t_tempo) {
		this.t_tempo = t_tempo;
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
	public int getMinutos() {
		return minutos;
	}
	public void setMinutos(int minutos) {
		this.minutos = minutos;
	}
	
	

}
