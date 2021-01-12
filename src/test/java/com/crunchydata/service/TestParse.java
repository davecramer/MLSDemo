package com.crunchydata.service;

import com.crunchydata.model.Document;
import com.crunchydata.postgres.PGServiceFile;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

public class TestParse {
    PGServiceFile pgServiceFile;

    @Before
    public void before() throws  Exception{
        pgServiceFile = PGServiceFile.load();
    }
    @Test
    public void testStoreTsVector() throws Exception {
        QueryExecutor queryExecutor = QueryExecutor.getQueryExecutor(pgServiceFile.getService("test"));
        AutoDetectParser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();

        try (InputStream stream = TestParse.class.getResourceAsStream("/Warranty_Book_Full.pdf")) {
            parser.parse(stream, handler, metadata);
        }
        Document document = queryExecutor.getDocumentById(1);
        queryExecutor.updateTsVector(document, handler.toString());
    }
}
