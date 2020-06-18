package br.com.EdinhosPlayPark.domain;

import java.util.Date;

public class T_caixa {

	private int id;
	private int minutos;
	private long id_controle;
	private String sticket;
	private String nome;
	private String sperini;
	private String sperfim;
	private Date dperini;
	private Date dperfim;
	private String timeover;
	private Cbo_CarrinhoCompras carrinho_combo = new Cbo_CarrinhoCompras();
	private String sdatanasc;
	private String sdepend;
	private Date ddata_nasc;
	private String svalor_min;
	private String svalor_div;
	private String svalor_tot;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getId_controle() {
		return id_controle;
	}
	public void setId_controle(long id_controle) {
		this.id_controle = id_controle;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSperini() {
		return sperini;
	}
	public void setSperini(String sperini) {
		this.sperini = sperini;
	}
	public String getSperfim() {
		return sperfim;
	}
	public void setSperfim(String sperfim) {
		this.sperfim = sperfim;
	}
	public Date getDperini() {
		return dperini;
	}
	public void setDperini(Date dperini) {
		this.dperini = dperini;
	}
	public Date getDperfim() {
		return dperfim;
	}
	public void setDperfim(Date dperfim) {
		this.dperfim = dperfim;
	}
	public int getMinutos() {
		return minutos;
	}
	public void setMinutos(int minutos) {
		this.minutos = minutos;
	}
	public String getTimeover() {
		return timeover;
	}
	public void setTimeover(String timeover) {
		this.timeover = timeover;
	}
	public Cbo_CarrinhoCompras getCarrinho_combo() {
		return carrinho_combo;
	}
	public void setCarrinho_combo(Cbo_CarrinhoCompras carrinho_combo) {
		this.carrinho_combo = carrinho_combo;
	}
	public String getSdatanasc() {
		return sdatanasc;
	}
	public void setSdatanasc(String sdatanasc) {
		this.sdatanasc = sdatanasc;
	}
	public String getSdepend() {
		return sdepend;
	}
	public void setSdepend(String sdepend) {
		this.sdepend = sdepend;
	}
	public Date getDdata_nasc() {
		return ddata_nasc;
	}
	public void setDdata_nasc(Date ddata_nasc) {
		this.ddata_nasc = ddata_nasc;
	}
	public String getSticket() {
		return sticket;
	}
	public void setSticket(String sticket) {
		this.sticket = sticket;
	}
	public String getSvalor_min() {
		return svalor_min;
	}
	public void setSvalor_min(String svalor_min) {
		this.svalor_min = svalor_min;
	}
	public String getSvalor_div() {
		return svalor_div;
	}
	public void setSvalor_div(String svalor_div) {
		this.svalor_div = svalor_div;
	}
	public String getSvalor_tot() {
		return svalor_tot;
	}
	public void setSvalor_tot(String svalor_tot) {
		this.svalor_tot = svalor_tot;
	}

}
