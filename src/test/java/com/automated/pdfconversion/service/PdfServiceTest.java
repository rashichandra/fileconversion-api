package com.automated.pdfconversion.service;

import com.automated.pdfconversion.RESTService.PdfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PdfServiceTest {

    private PdfService pdfService;

    @BeforeEach
    void setUp() {
        pdfService = new PdfService();
    }

    @Test
    @DisplayName("convertToPdf should return PDF bytes for valid .docx file")
    void convertToPdfReturnsPdfBytesForValidDocxFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "dummy content".getBytes());
        byte[] result = pdfService.convertToPdf(file);
        assertEquals("PDF content".getBytes().length, result.length);
    }

    @Test
    @DisplayName("convertToPdf should return PDF bytes for valid .txt file")
    void convertToPdfReturnsPdfBytesForValidTxtFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "dummy content".getBytes());
        byte[] result = pdfService.convertToPdf(file);
        assertEquals("PDF content".getBytes().length, result.length);
    }

    @Test
    @DisplayName("convertToPdf should throw exception for unsupported file type")
    void convertToPdfThrowsExceptionForUnsupportedFileType() {
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "dummy content".getBytes());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> pdfService.convertToPdf(file));
        assertEquals("Unsupported file type. Please upload a .docx or .txt file.", exception.getMessage());
    }

    @Test
    @DisplayName("convertToPdf should throw exception for null file name")
    void convertToPdfThrowsExceptionForNullFileName() {
        MockMultipartFile file = new MockMultipartFile("file", null, "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "dummy content".getBytes());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> pdfService.convertToPdf(file));
        assertEquals("Invalid file type. Please upload a valid file.", exception.getMessage());
    }
    @DisplayName("convertTxttoPdf should return PDF bytes for valid .txt file with multiple lines")
    void convertTxttoPdfReturnsPdfBytesForValidTxtFileWithMultipleLines() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "line1\nline2\nline3".getBytes());
        byte[] result = pdfService.convertTxttoPdf(file);
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @DisplayName("convertTxttoPdf should handle empty .txt file gracefully")
    void convertTxttoPdfHandlesEmptyTxtFileGracefully() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "empty.txt", "text/plain", "".getBytes());
        byte[] result = pdfService.convertTxttoPdf(file);
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @DisplayName("convertTxttoPdf should throw exception for null file input")
    void convertTxttoPdfThrowsExceptionForNullFileInput() {
        Exception exception = assertThrows(NullPointerException.class, () -> pdfService.convertTxttoPdf(null));
        assertEquals("file is marked non-null but is null", exception.getMessage());
    }

    @DisplayName("convertTxttoPdf should handle .txt file with only whitespace")
    void convertTxttoPdfHandlesTxtFileWithOnlyWhitespace() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "whitespace.txt", "text/plain", "   \n   \n".getBytes());
        byte[] result = pdfService.convertTxttoPdf(file);
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @DisplayName("convertTxttoPdf should handle .txt file with special characters")
    void convertTxttoPdfHandlesTxtFileWithSpecialCharacters() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "special.txt", "text/plain", "line1\n!@#$%^&*()\nline3".getBytes());
        byte[] result = pdfService.convertTxttoPdf(file);
        assertNotNull(result);
        assertTrue(result.length > 0);
    }
    @DisplayName("convertDocxToPdf should return PDF bytes for valid .docx file")
    void convertDocxToPdfReturnsPdfBytesForValidDocxFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "dummy content".getBytes());
        byte[] result = pdfService.convertDocxToPdf(file);
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @DisplayName("convertDocxToPdf should throw exception for invalid .docx file")
    void convertDocxToPdfThrowsExceptionForInvalidDocxFile() {
        MockMultipartFile file = new MockMultipartFile("file", "invalid.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", new byte[0]);
        Exception exception = assertThrows(Exception.class, () -> pdfService.convertDocxToPdf(file));
        assertEquals("Error processing .docx file", exception.getMessage());
    }

    @DisplayName("convertDocxToPdf should throw exception for null file input")
    void convertDocxToPdfThrowsExceptionForNullFileInput() {
        Exception exception = assertThrows(NullPointerException.class, () -> pdfService.convertDocxToPdf(null));
        assertEquals("file is marked non-null but is null", exception.getMessage());
    }

    @DisplayName("convertDocxToPdf should handle .docx file with special characters in content")
    void convertDocxToPdfHandlesDocxFileWithSpecialCharacters() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "special.docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "Content with special characters: !@#$%^&*()".getBytes());
        byte[] result = pdfService.convertDocxToPdf(file);
        assertNotNull(result);
        assertTrue(result.length > 0);
    }
}