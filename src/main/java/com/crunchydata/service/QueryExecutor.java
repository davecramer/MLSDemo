package com.crunchydata.service;

import com.crunchydata.model.Document;
import com.crunchydata.postgres.PGServiceFile;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QueryExecutor {

    private static final String DEFAULT_USER = "davec";
    private static final String DEFAULT_PASSWORD = "password";

    private static final Logger logger = Logger.getLogger(QueryExecutor.class.getName());
    Connection connection;

    Map<String, String> service;
    Properties connectionProperties;
    private static Map<String, QueryExecutor> executorMap = new HashMap<>();
    private static  Map<String, Map<String, String>> services = PGServiceFile.load().getSections();

    private QueryExecutor(){

    }
    public static QueryExecutor getQueryExecutor( Map <String, String> serviceMap ) throws SQLException {
        QueryExecutor queryExecutor = new QueryExecutor();
        queryExecutor.setService(serviceMap);
        return queryExecutor;
    }

    public static QueryExecutor getQueryExecutor( String service ) throws Exception {
        if ( executorMap.containsKey( service ) ) {
            return executorMap.get( service );
        } else {
            QueryExecutor queryExecutor = new QueryExecutor();
            Map pgService = services.get( service );
            if ( pgService == null ) {
                throw new Exception("Unable to find service " + service);
            }
            queryExecutor.setService( pgService );
            executorMap.put( service, queryExecutor );
            return queryExecutor;
        }
    }

    public Connection getConnection() throws SQLException {
        //TODO: configurable database and user
        if ( connection == null ) {
            connection = DriverManager.getConnection(createUrl(), connectionProperties);
        }
        return connection;
    }
    String createUrl() {
        String URL = "jdbc:postgresql://"+connectionProperties.getProperty("host")+'/'+connectionProperties.getProperty("dbname");
        return URL;
    }
    /**
     *
     * @param service
     * a map containing everything in the service file
     * sslrootcert=/Users/davec/projects/mlstest/certs/rootCA.pem
     * sslcert=/Users/davec/projects/mlstest/certs/low-client.pem
     * sslkey=/Users/davec/projects/mlstest/certs/low-client-key.pem
     * sslmode=allow
     * dbname=postgres
     * host=low.selinux-el8-primary.com
     * hostaddr=172.16.50.100
     */
    public void setService(Map <String, String> service) throws SQLException{
        this.service = service;
        connectionProperties = new Properties();
        connectionProperties.putAll(service);
        connection = getConnection();
    }

    public int updateDoc(Document document){
        int result = 0;
        try {
            if ( document.getId() != null ) {
                // update
                try (PreparedStatement pstmt= connection.prepareStatement("update docs set name=? where id=?") ) {
                    pstmt.setString(1, document.getName());
                    pstmt.setInt(2, document.getId());
                    result =  pstmt.executeUpdate();
                }
            } else {
                // insert
                try (PreparedStatement pstmt= connection.prepareStatement("insert into docs (name, b) values (?,?) ", Statement.RETURN_GENERATED_KEYS) ) {
                    pstmt.setString(1, document.getName());
                    pstmt.setBytes(2, document.getFileBytes());
                    result =  pstmt.executeUpdate();
                    ResultSet rs = pstmt.getGeneratedKeys();
                    if (rs.next()){
                        document.setId(rs.getInt(1));
                    }
                }
            }

        } catch (Exception ex ){
            logger.log(Level.FINE, ex.getMessage());
        }
        return result;
    }

    public int updateTsVector(Document document, String documentText) throws  Exception{
        try {
            if (document.getId() != null) {
                // update
                try (PreparedStatement pstmt = connection.prepareStatement("update docs set tsv=to_tsvector('english',?) where id=?")) {
                    pstmt.setString(1, documentText);
                    pstmt.setInt(2, document.getId());
                    return pstmt.executeUpdate();
                }
            }
        }catch (Exception ex ){
            logger.log(Level.FINE, ex.getMessage());
        }
        return 0;
    }

    public Document getDocumentById(Integer id){
        Document document = null;
        try (PreparedStatement pstmt = connection.prepareStatement("select id, name, b from docs where id = ?")) {
            pstmt.setInt(1, id);
            try ( ResultSet rs = pstmt.executeQuery() ) {
               if (rs.next()) {
                   document = new Document();
                   document.setId(rs.getInt(1));
                   document.setName(rs.getString(2));
                   document.setFileBytes(rs.getBytes(3));
                   return document;
               }
            }
        } catch ( Exception ex ){
            logger.log(Level.FINE, ex.getMessage());
        }
        return document;
    }
    public Document getDocumentByName(String name){
        Document document = null;
        try ( PreparedStatement pstmt = connection.prepareStatement("select id, name, b from docs where name = ?")) {
            pstmt.setString(1, name);
            try ( ResultSet rs = pstmt.executeQuery() ) {
                if (rs.next()) {
                    document = new Document();
                    document.setId(rs.getInt(1));
                    document.setName(rs.getString(2));
                    document.setFileBytes(rs.getBytes(3));
                    return document;
                }
            }
        } catch ( Exception ex ){
            logger.log(Level.FINE, ex.getMessage());
        }
        return document;
    }
    public List<Document> getDocuments() {
        List<Document> documentList = new ArrayList<Document>();
        try (PreparedStatement pstmt = connection.prepareStatement("select id, name from docs order by id") ) {
            try ( ResultSet rs = pstmt.executeQuery() ) {
                while (rs.next()){
                    Document document = new Document();
                    document.setId(rs.getInt("id"));
                    document.setName(rs.getString("name"));
                    documentList.add(document);
                }
            }
        } catch (Exception ex) {
            logger.log(Level.FINE, ex.getMessage());
        }
        return documentList;
    }
}
