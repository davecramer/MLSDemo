package com.crunchydata.service;

import com.crunchydata.model.Document;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;
;import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class ParseDoc {

    public BodyContentHandler parseDoc(QueryExecutor queryExecutor, Integer id) throws Exception{
        AutoDetectParser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();

        Document document = queryExecutor.getDocumentById(id);

        return parseDoc(document);
    }

    public BodyContentHandler parseDoc(Document document) throws Exception {
        AutoDetectParser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        parser.parse(new ByteArrayInputStream(document.getFileBytes()), handler, metadata);
        return handler;
    }
    public BodyContentHandler parseDoc(String fileName){
        AutoDetectParser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            parser.parse(fis, handler, metadata);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException saxException) {
            saxException.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        }
        return handler;
    }

}

