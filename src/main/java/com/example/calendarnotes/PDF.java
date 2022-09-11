package com.example.calendarnotes;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.Paragraph;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class PDF {

    FileChooser fileChooser = new FileChooser();

    void exportStringToPDF(String title, String text) throws DocumentException, FileNotFoundException {
        File file = fileChooser.showSaveDialog(new Stage());
        if(file!=null) {
            Document doc = new Document();
            PdfWriter docWriter = PdfWriter.getInstance(doc, new FileOutputStream(file + ".pdf"));
            doc.open();

            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
            Font textFont = new Font(Font.FontFamily.HELVETICA, 12);

            Paragraph titleOfText = new Paragraph();
            titleOfText.add(new Paragraph(title, titleFont));
            titleOfText.add(new Paragraph(" "));
            titleOfText.add(new Paragraph(" "));
            doc.add(titleOfText);

            Paragraph preface = new Paragraph();
            preface.add(new Paragraph(text, textFont));
            doc.add(preface);

            doc.newPage();

            if (doc != null) {
                doc.close();
            }
            if (docWriter != null) {
                docWriter.close();
            }
        }
    }

    void exportHTMLToPDF (String content) throws IOException{
        File file = fileChooser.showSaveDialog(new Stage());
        if(file!=null) {
            OutputStream fileOutputStream = new FileOutputStream(file + ".pdf");
            HtmlConverter.convertToPdf(content, fileOutputStream);
        }
    }

    public static void main (String []args) {
    }
}
