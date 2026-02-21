package com.typinggame.filemanagement;

import java.io.IOException;

public abstract class FileManager {
    protected String filePath;
    
    public FileManager(String filePath) {
        this.filePath = filePath;
    }
    
    public abstract void save() throws IOException;
    public abstract void load() throws IOException;
    public abstract void createDefaultIfNotExists() throws IOException;
}