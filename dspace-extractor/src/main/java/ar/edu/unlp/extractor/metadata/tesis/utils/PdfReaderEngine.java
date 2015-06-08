package ar.edu.unlp.extractor.metadata.tesis.utils;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.pdfbox.exceptions.CryptographyException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.PDFTextStripperByArea;

public class PdfReaderEngine {

	/**
	 * Metodo que extrae el texto de la primer pagina de un PDF
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String extractPdfFirstPageText(File file) throws IOException {
		PDDocument pdf = PDDocument.load(file);
		PDFTextStripper stripper = new PDFTextStripper("UTF-8");
		stripper.setStartPage(1);
		stripper.setEndPage(1);

		String plainText = stripper.getText(pdf);
		pdf.close();
		return plainText;
	}

	/**
	 * Metodo que extrae las lineas del texto dela primera pagina de PDF
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static List<String> extractPdfFirstPageTextByLines(File file) throws IOException {
		PDDocument pdf = PDDocument.load(file);
		PDFTextStripper stripper = new PDFTextStripper("UTF-8");
		stripper.setStartPage(1);
		stripper.setEndPage(1);
		String plainText = stripper.getText(pdf);
		List<String> ans = Arrays.asList(plainText.split("\r\n"));
		pdf.close();
		return ans;
	}

	/**
	 * Metodo que extra un area de texto sobre un PDF
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws CryptographyException
	 */
	public static String extractTextByArea(File file) throws IOException, CryptographyException {

		PDDocument document = null;
		try {
			document = PDDocument.load(file);
			if (document.isEncrypted()) {
				document.decrypt("");
			}
			PDFTextStripperByArea stripper = new PDFTextStripperByArea();
			stripper.setSortByPosition(true);
			Rectangle rect = new Rectangle(100, 100, 400, 200);
			stripper.addRegion("class1", rect);
			List allPages = document.getDocumentCatalog().getAllPages();
			PDPage firstPage = (PDPage) allPages.get(0);
			stripper.extractRegions(firstPage);

			return stripper.getTextForRegion("class1");

		} finally {
			if (document != null) {
				document.close();
			}
		}
	}
}
