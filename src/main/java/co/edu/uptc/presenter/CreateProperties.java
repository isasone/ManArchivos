package co.edu.uptc.presenter;

import co.edu.uptc.model.IOManager;
/**@author Angel Santiago Bedoya Carreño
 *  */
public class CreateProperties {

public static void main(String[] args) {
    IOManager io=new IOManager();
    io.createPropiertiesFile("resources/properties/","Properties.properties");
}
}
