package br.com.EdinhosPlayPark.domain;

public class Mail_parameters {
	private Long id;
	private String mail_sender;
	private String mail_password;
	private String mail_send;
	private String mail_destination;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMail_sender() {
		return mail_sender;
	}
	public void setMail_sender(String mail_sender) {
		this.mail_sender = mail_sender;
	}
	public String getMail_password() {
		return mail_password;
	}
	public void setMail_password(String mail_password) {
		this.mail_password = mail_password;
	}
	public String getMail_send() {
		return mail_send;
	}
	public void setMail_send(String mail_send) {
		this.mail_send = mail_send;
	}
	public String getMail_destination() {
		return mail_destination;
	}
	public void setMail_destination(String mail_destination) {
		this.mail_destination = mail_destination;
	}

}
