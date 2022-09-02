package com.example.calendarnotes;

import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.Paragraph;

import java.io.*;

public class PDF {
    void exportToPDF(String title, String text) throws DocumentException, FileNotFoundException {
        String file = "src\\main\\pdfs\\" + title + ".pdf"; // problem when the title ends with lets say a ?, it wont accept yes?.pdf
        Document doc = new Document();
        PdfWriter docWriter = PdfWriter.getInstance(doc , new FileOutputStream(file));
        doc.open();

        Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0)); // random font
        Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);

        Paragraph titleOfText = new Paragraph();
        titleOfText.add(new Paragraph(title, bfBold12));
        addEmptyLine(titleOfText, 2);
        doc.add(titleOfText);

        Paragraph preface = new Paragraph();
        preface.add(new Paragraph(text, bf12));
        doc.add(preface);

        doc.newPage();

        if (doc != null){
            doc.close(); //close the document
        }
        if (docWriter != null){
            docWriter.close(); //close the writer
        }
    }
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
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

    public static void main (String args[]) {
        try{
            String fileLocation = "src\\main\\pdfs\\T2.pdf"; // problem when the title ends with lets say a ?, it wont accept yes?.pdf
            OutputStream file = new FileOutputStream(new File(fileLocation)); // problem when the title ends with lets say a ?, it wont accept yes?.pdf

            Document document = new Document();
            PdfWriter.getInstance(document, file);
            document.open();
            HTMLWorker htmlWorker = new HTMLWorker(document);
            htmlWorker.parse(new StringReader("<html> <!--- --->\n" +
                    "<head>\n" +
                    "    <title> Shop name </title>\n" +
                    "    <!--- puslapio/tab pavadinimas --->\n" +
                    "</head>\n" +
                    "\n" +
                    "<body style=\"background-color:#f8da9f;\"> <!--- you can choose by holding on top of the color boxixe--->\n" +
                    "    <p style=\"text-align:center; font-size: 40px\" > Shop name </p>  <!--- font size can only be used for p --->\n" +
                    "    <!--- header (didelis pastraipos pavadinimas),    style=\"text-align:center\" yra css --->\n" +
                    "    <h3 style=\"text-align:left\"> Hope you enjoy ~♥ </h3>\n" +
                    "    <p style=\"margin-left: 15em\"> <!--- pats išsirenki kokio dydžio em --->\n" +
                    "    <br> <!--- newline/break --->\n" +
                    "    </p>\n" +
                    "</body>\n" +
                    "\n" +
                    "</html>"));
            document.close();
            file.close();}
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
