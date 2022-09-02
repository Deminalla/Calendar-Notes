package com.example.calendarnotes;

import com.itextpdf.html2pdf.HtmlConverter;

import java.io.*;

public class PDF2 {
    public static void main (String args[]) throws IOException{ // works with a bit of css as well
        OutputStream fileOutputStream = new FileOutputStream("src\\main\\pdfs\\T2.pdf");
        HtmlConverter.convertToPdf("<html> <!--- --->\n" +
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
                "</html>", fileOutputStream);
    }
}
