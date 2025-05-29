package com.automated.pdfconversion.RESTConstrollers;

import com.automated.pdfconversion.RESTService.PdfService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/fileconversion/pdf")
public class PdfController {


    private final PdfService pdfService;

    public PdfController(PdfService pdfService){
        this.pdfService = pdfService;
    }
    @PostMapping("/convert")
    public ResponseEntity<Resource> convertToPdf(@RequestParam("file")MultipartFile file) throws  Exception{
        byte[] pdfBytes = pdfService.convertToPdf(file);
        ByteArrayResource resource=new ByteArrayResource(pdfBytes);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"converted-pdf.pdf\"").
                contentType(MediaType.APPLICATION_PDF).body(resource);
    }

}
