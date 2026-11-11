package co.edu.uptc.model;

import java.io.Serializable;
import java.time.LocalDateTime;
/**@author Angel Santiago Bedoya Carreño
 *  */
public class FileManager implements Serializable {
    private String name;
    private LocalDateTime creation;
    private LocalDateTime save;
    private LocalDateTime edit;

    public FileManager(String name) {
        this.name=name;
    }

    

    public LocalDateTime getCreation() {
        return creation;
    }

    public void setCreation(LocalDateTime creation) {
        this.creation = creation;
    }

    public LocalDateTime getSave() {
        return save;
    }

    public void setSave(LocalDateTime save) {
        this.save = save;
    }

    public LocalDateTime getEdit() {
        return edit;
    }

    public void setEdit(LocalDateTime edit) {
        this.edit = edit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    



}
