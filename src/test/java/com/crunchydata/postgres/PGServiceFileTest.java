package com.crunchydata.postgres;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class PGServiceFileTest {

    PGServiceFile pgServiceFile;

    @Before
    public void before() throws  Exception{
        pgServiceFile = PGServiceFile.load();
    }


    @Test
    public void getService()throws Exception {
        Map map = pgServiceFile.getService("low");
        assertNotNull(map);
    }
    @Test
    public void getServices() throws  Exception {
        Map allServices = pgServiceFile.getSections();
        assertNotNull(allServices);
    }
}