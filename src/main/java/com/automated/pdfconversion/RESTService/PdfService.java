package com.automated.pdfconversion.RESTService;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.docx4j.Docx4J;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {


    public byte[] convertToPdf(MultipartFile file) throws Exception{
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new IllegalArgumentException("Invalid file type. Please upload a valid file.");
        }
        if(fileName.endsWith("docx")){
            return convertDocxToPdf(file);
        }
        else if(fileName.endsWith(".txt")){
            return convertTxttoPdf(file);
        }
        else {
            throw new IllegalArgumentException("Unsupported file type. Please upload a .docx or .txt file.");
        }
    }

    public byte[] convertTxttoPdf(MultipartFile file) throws Exception {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.newLineAtOffset(100, 700);

        // Preprocess the text and handle line breaks
        String text = new String(file.getBytes());
        String[] lines = text.split("\r?\n"); // Split text into lines

        for (String line : lines) {
            contentStream.showText(line);
            contentStream.newLineAtOffset(0, -15); // Move to the next line (adjust spacing as needed)
        }

        contentStream.endText();
        contentStream.close();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        document.save(out);
        document.close();
        return out.toByteArray();
    }

    public byte[] convertDocxToPdf(MultipartFile file) throws Exception{
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(file.getInputStream());
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        Docx4J.toPDF(wordMLPackage,pdfOutputStream);
        return pdfOutputStream.toByteArray();
    }


}
