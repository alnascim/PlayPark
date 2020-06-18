package br.com.EdinhosPlayPark.util;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "MBean")
@ViewScoped
public class Bean {

    private boolean disable;

    // default constructor 
    public Bean(){
       this.disable= false;
    }

    public boolean isDisable() {
       return disable;
    }
    public void setDisable(boolean disable) {
       this.disable = disable;
    }
}