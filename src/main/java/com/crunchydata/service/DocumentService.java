package com.crunchydata.service;

import com.crunchydata.model.Document;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class DocumentService {

    public void updateTsVector(Document document, String service) throws Exception {
        QueryExecutor queryExecutor = QueryExecutor.getQueryExecutor(service);
        AutoDetectParser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        InputStream is = new ByteArrayInputStream(document.getFileBytes());
        parser.parse(is, handler, metadata);
        queryExecutor.updateTsVector(document, handler.toString());
    }

    public Document insertDocument(String documentPath , String service) throws Exception {
        String fileSeperator = File.separator;
        // this is a bit tricky here.... lastIndexOf will return -1 if not found
        // so addding 1 gets us to 0, otherwise it is 1 past the last file separator
        int lastSeparator = documentPath.lastIndexOf(fileSeperator)+1;
        String documentName = documentPath.substring( lastSeparator );
        InputStream stream = new FileInputStream(documentPath);

        byte []bytes = new byte[stream.available()];
        stream.read(bytes);
        Document document = new Document();
        document.setName(documentName);
        document.setFileBytes(bytes);
        QueryExecutor queryExecutor = QueryExecutor.getQueryExecutor(service);
        if (queryExecutor.updateDoc(document) == 1) {
            return document;
        }
        return null;
    }
}
