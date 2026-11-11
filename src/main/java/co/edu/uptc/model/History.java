package co.edu.uptc.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
/**@author Angel Santiago Bedoya Carreño
 *  */
public class History implements Serializable {
    private Map<String, FileManager> fileMap;
    public History(){
        fileMap=new HashMap<>();
    }
    private FileManager recordFile(String name){
        if(!fileMap.containsKey(name)){
            FileManager file=new FileManager(name);
            fileMap.put(name, file);
        }
        return fileMap.get(name);
    }
    public void registredCreated(String name){
        recordFile(name).setCreation(LocalDateTime.now());
    }
    public void registerSave(String name){
        recordFile(name).setSave(LocalDateTime.now());
    }
    public void registerEdit(String name){
        recordFile(name).setEdit(LocalDateTime.now());
    }
    public Map<String, FileManager> getFileMap() {
        return fileMap;
    }
}
