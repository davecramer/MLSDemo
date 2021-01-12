package com.crunchydata;

import java.util.EventListener;

public interface FileChosen extends EventListener {
    public void save( String fileName);
}
