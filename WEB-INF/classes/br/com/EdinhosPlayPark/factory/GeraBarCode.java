package br.com.EdinhosPlayPark.factory;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.printing.PDFPageable;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.Barcode;
import com.lowagie.text.pdf.BarcodeEAN;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

public class GeraBarCode {

	public static void main(String[] args) throws PrinterException, IOException {

		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		String sFileNamepdf = "CodigoBarra.pdf";
		try {
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(
							sFileNamepdf));
			document.open();
			document.add(new Paragraph("Gerando PDF - Java",FontFactory.getFont(FontFactory.TIMES_ROMAN, 10)));
			document.add(new Paragraph("Gerando PDF - Java"));
			document.add(new Paragraph("Gerando PDF - Java"));
			

//			PdfContentByte cb = writer.getDirectContent();
//
//			BarcodeEAN codeEAN = new BarcodeEAN();
//
//			codeEAN.setCodeType(Barcode.EAN13);
//
//			codeEAN.setCode("9780201615883");
//
//			Image imageEAN = codeEAN.createImageWithBarcode(cb, null, null);
//
//			document.add(new Phrase(new Chunk(imageEAN, 0, 0)));

		}

		catch (Exception de) {

			de.printStackTrace();

		}

		document.close();
//		
		PDDocument documento = null;
		try {
			documento = PDDocument.load(new File(sFileNamepdf));
		} catch (InvalidPasswordException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  PrintService servico = PrintServiceLookup.lookupDefaultPrintService();

		  PrinterJob job = PrinterJob.getPrinterJob();
		  job.setPageable(new PDFPageable(documento));
		  job.setPrintService(servico);
		  job.print();
		  documento.close();
				
	}
}
