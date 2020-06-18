package br.com.EdinhosPlayPark.domain;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.annotation.PostConstruct;

@ManagedBean(name = "MBRadio")
@ViewScoped
public class RadioView {
	 private String console;    
	    private String city;
	    private String city2;  
	    private List<String> cities;  
	    private String color;
	     
	    @PostConstruct
	    public void init() {
	        cities = new ArrayList<String>();
	        cities.add("15 Minutos - R$ 10,00");
	        cities.add("20 Minutos - R$ 12,00");
	        cities.add("30 Minutos - R$ 15,00");
	        cities.add("40 Minutos - R$ 18,00");
	        cities.add("50 Minutos - R$ 20,00");
	        cities.add("60 Minutos - R$ 22,00");
	    }
	 
	    public String getConsole() {
	        return console;
	    }
	 
	    public void setConsole(String console) {
	        this.console = console;
	    }
	 
	    public String getCity() {
	        return city;
	    }
	 
	    public void setCity(String city) {
	        this.city = city;
	    }
	 
	    public String getCity2() {
	        return city2;
	    }
	 
	    public void setCity2(String city2) {
	        this.city2 = city2;
	    }
	 
	    public String getColor() {
	        return color;
	    }
	 
	    public void setColor(String color) {
	        this.color = color;
	    }
	 
	    public List<String> getCities() {
	        return cities;
	    }
}
