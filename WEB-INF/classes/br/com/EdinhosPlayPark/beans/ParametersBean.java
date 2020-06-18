package br.com.EdinhosPlayPark.beans;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.EdinhosPlayPark.dao.ParametersDAO;
import br.com.EdinhosPlayPark.domain.Mail_parameters;
import br.com.EdinhosPlayPark.util.JSFUtil;

@ManagedBean(name = "MBParam")
@ViewScoped
public class ParametersBean {

	private Mail_parameters parameters;
	private ArrayList<Mail_parameters> itens;
	public Mail_parameters getParameters() {
		return parameters;
	}

	public void setParameters(Mail_parameters parameters) {
		this.parameters = parameters;
	}

	public ArrayList<Mail_parameters> getItens() {
		return itens;
	}

	public void setItens(ArrayList<Mail_parameters> itens) {
		this.itens = itens;
	}

	@PostConstruct
	public void prepararPesquisa() {
		try {
			ParametersDAO dao = new ParametersDAO();
			itens = dao.listar();
		} catch (SQLException ex) {
			ex.printStackTrace();
			JSFUtil.adicionarMensagemErro("Erro 001 - " + ex.getMessage());
		}
	}

	public void salvar() throws Exception {

		try {
			ParametersDAO dao = new ParametersDAO();
			dao.salvar(parameters);
			itens = dao.listar();
			JSFUtil.adicionarMensagemSucesso("Salvo com Sucesso!");
		} catch (SQLException ex) {
			ex.printStackTrace();
			JSFUtil.adicionarMensagemErro("Erro Usuário 002 - "
					+ ex.getMessage());
		}

	}


}
