package com.example.calendarnotes;

import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.Paragraph;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

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

    public static void main (String args[]) {
    }
}
