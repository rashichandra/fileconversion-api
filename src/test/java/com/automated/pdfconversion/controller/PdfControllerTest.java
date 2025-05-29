package com.automated.pdfconversion.controller;

import com.automated.pdfconversion.RESTService.PdfService;
import com.automated.pdfconversion.RESTConstrollers.PdfController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class PdfControllerTest {

    private PdfService pdfService;
    private PdfController pdfController;

    @BeforeEach
    void setUp() {
        pdfService = Mockito.mock(PdfService.class);
//        pdfController = new PdfController(pdfService);
    }

    @Test
    @DisplayName("convertToPdf should return a valid PDF response when a valid file is provided")
    void convertToPdfReturnsValidResponseForValidFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "dummy content".getBytes());
        byte[] pdfBytes = "PDF content".getBytes();
        when(pdfService.convertToPdf(file)).thenReturn(pdfBytes);

        ResponseEntity<Resource> response = pdfController.convertToPdf(file);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("application/pdf", response.getHeaders().getContentType().toString());
        assertNotNull(response.getBody());
        verify(pdfService, times(1)).convertToPdf(file);
    }

    @Test
    @DisplayName("convertToPdf should throw an exception when an invalid file is provided")
    void convertToPdfThrowsExceptionForInvalidFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "invalid.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", new byte[0]);
        when(pdfService.convertToPdf(file)).thenThrow(new IllegalArgumentException("Invalid file"));

        try {
            pdfController.convertToPdf(file);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid file", e.getMessage());
        }

        verify(pdfService, times(1)).convertToPdf(file);
    }

    @Test
    @DisplayName("convertToPdf should handle null file input gracefully")
    void convertToPdfHandlesNullFileInput() {
        try {
            pdfController.convertToPdf(null);
        } catch (Exception e) {
            assertEquals("Required request parameter 'file' is not present", e.getMessage());
        }

        verifyNoInteractions(pdfService);
    }
}