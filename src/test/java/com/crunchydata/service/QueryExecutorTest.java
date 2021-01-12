package com.crunchydata.service;

import com.crunchydata.model.Document;
import com.crunchydata.postgres.PGServiceFile;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;

import static org.junit.Assert.*;

public class QueryExecutorTest {
    PGServiceFile pgServiceFile;

    @Before
    public void before() throws  Exception{
        pgServiceFile = PGServiceFile.load();
    }

    @Test
    public void getConnection() throws Exception{
        QueryExecutor queryExecutor = QueryExecutor.getQueryExecutor(pgServiceFile.getService("test"));
        try(Connection connection = queryExecutor.getConnection()){
            assertNotNull(connection);
        }
    }

    @Test
    public void updateDoc() throws Exception {
        InputStream stream = QueryExecutorTest.class.getResourceAsStream("/Warranty_Book_Full.pdf");
        byte []bytes = new byte[stream.available()];
        stream.read(bytes);
        Document document = new Document();
        document.setName("WarrantyBook.pdf");
        document.setFileBytes(bytes);
        QueryExecutor queryExecutor = QueryExecutor.getQueryExecutor(pgServiceFile.getService("test"));
        queryExecutor.updateDoc(document);

    }

    @Test
    public void getDocumentById() throws Exception {
        QueryExecutor queryExecutor = QueryExecutor.getQueryExecutor(pgServiceFile.getService("test"));
        Document document = queryExecutor.getDocumentById(1);
        assertNotNull(document);
    }
    @Test
    public void updateDocumentById() throws Exception {
        QueryExecutor queryExecutor = QueryExecutor.getQueryExecutor(pgServiceFile.getService("test"));
        Document document = queryExecutor.getDocumentById(1);
        assertNotNull(document);
        document.setName("WarrantyBook2.pdf");
        queryExecutor.updateDoc(document);
        document = queryExecutor.getDocumentById(document.getId());
        assertEquals("WarrantyBook2.pdf", document.getName());
    }


}