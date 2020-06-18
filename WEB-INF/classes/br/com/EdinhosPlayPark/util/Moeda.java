package br.com.EdinhosPlayPark.util;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
public class Moeda {
	private static final Locale BRAZIL = null;
	private static final DecimalFormatSymbols REAL = new DecimalFormatSymbols(BRAZIL);
	/**
	* Mascara de dinheiro para Real Brasileiro
	*/
	public static final DecimalFormat DINHEIRO_REAL = new DecimalFormat("¤ ###,###,##0.00",getReal());
	/**
	* Mascara de decinal
	*/
	public static final DecimalFormat DECIMAL = new DecimalFormat("###,###,##0.00",getReal());

	public static String mascaraDinheiro(double valor, DecimalFormat moeda){
	    return moeda.format(valor);
	}

	public static DecimalFormatSymbols getReal() {
		return REAL;
	}
}
