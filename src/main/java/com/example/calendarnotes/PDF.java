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

            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0)); // random font
            Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);

            Paragraph titleOfText = new Paragraph();
            titleOfText.add(new Paragraph(title, bfBold12));
            titleOfText.add(new Paragraph(" "));
            titleOfText.add(new Paragraph(" "));
            doc.add(titleOfText);

            Paragraph preface = new Paragraph();
            preface.add(new Paragraph(text, bf12));
            doc.add(preface);

            doc.newPage();

            if (doc != null) {
                doc.close(); //close the document
            }
            if (docWriter != null) {
                docWriter.close(); //close the writer
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

    /*
    void exportToPDF(String title, String text) {
        try{
            String fileLocation = "src\\main\\pdfs\\" + title + ".pdf"; // problem when the title ends with lets say a ?, it wont accept yes?.pdf
            OutputStream file = new FileOutputStream(new File(fileLocation)); // problem when the title ends with lets say a ?, it wont accept yes?.pdf

            Document document = new Document();
            PdfWriter.getInstance(document, file);
            document.open();
            HTMLWorker htmlWorker = new HTMLWorker(document);
            htmlWorker.parse(new StringReader(text));
            document.close();
            file.close();}
        catch (Exception e) {
            e.printStackTrace();
        }
    }
     */

    public static void main (String []args) {
    }
}
