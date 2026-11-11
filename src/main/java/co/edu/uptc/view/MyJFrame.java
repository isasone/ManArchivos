package co.edu.uptc.view;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MyJFrame extends JFrame {
    private JMenuBar menuBar;
    private Text panelSee;
    private Text panelEdit;
    private LoadFile loadItem;
    private JPanel panel;
    private GridLayout grid;
    private JMenuItem open;
    private JMenuItem edit;
    private JMenuItem load;
    private JMenuItem save;
    private JMenuItem create;
    private JMenuItem delete;
    private JMenuItem loadImage;
    private JMenuItem loadProperties;
    private JMenuItem openJson;
    private JMenu weatherMenu;
    private JMenuItem[] cityItems;
    /**@author Angel Santiago Bedoya Carreño
     *  */
    public MyJFrame() {
        setTitle("Gestor de Archivos");
        setSize(900, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        build();
        setJMenuBar(menuBar);
        setIconImage(new ImageIcon(getClass().getResource("/resources/images/files.png")).getImage());
    }

    private void build() {
        menuBar = new JMenuBar();
        grid = new GridLayout(3, 1);
        panel = new JPanel(grid);
        JMenu jMenu = new JMenu("Menú");
        JMenu actions = new JMenu("Acciones");

        open = new JMenuItem("Abrir Archivo");
        open.setActionCommand("Open");
        ImageIcon icon = new ImageIcon(getClass().getResource("/resources/images/open.jpg"));
        Image image = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        open.setIcon(new ImageIcon(image));

        edit = new JMenuItem("Editar Archivo");
        edit.setActionCommand("Edit");
        load = new JMenuItem("Cargar Archivo");
        load.setActionCommand("Load");
        loadImage = new JMenuItem("Cargar Imagen");
        loadImage.setActionCommand("LoadImage");
        loadProperties = new JMenuItem("Cargar Properties");
        loadProperties.setActionCommand("LoadProperties");
        openJson = new JMenuItem("Abrir JSON");
        openJson.setActionCommand("OpenJson");
        save = new JMenuItem("Guardar Archivo");
        save.setActionCommand("Save");
        create = new JMenuItem("Crear Archivo");
        create.setActionCommand("Create");
        delete = new JMenuItem("Borrar Archivo");
        delete.setActionCommand("Delete");

        menuBar.add(jMenu);
        menuBar.add(actions);
        weatherMenu = new JMenu("Clima");
        menuBar.add(weatherMenu);
        jMenu.add(open);
        jMenu.add(edit);
        jMenu.add(load);
        jMenu.add(loadImage);
        jMenu.add(loadProperties);
        jMenu.add(openJson);
        actions.add(create);
        actions.add(save);
        actions.add(delete);

        panelSee = new Text(1);
        panelEdit = new Text(2);
        loadItem = new LoadFile();
        panel.add(panelSee);
        panel.add(panelEdit);
        panel.add(loadItem);
        add(panel);
    }

    public void addListener(ActionListener al) {
        open.addActionListener(al);
        edit.addActionListener(al);
        load.addActionListener(al);
        loadImage.addActionListener(al);
        loadProperties.addActionListener(al);
        openJson.addActionListener(al);
        for (JMenuItem cityItem : cityItems) {
            cityItem.addActionListener(al);
        }
        save.addActionListener(al);
        create.addActionListener(al);
        delete.addActionListener(al);
    }

    public void addCities(String[] cities) {
        cityItems = new JMenuItem[cities.length];
        for (int i = 0; i < cities.length; i++) {
            cityItems[i] = new JMenuItem(cities[i]);
            cityItems[i].setActionCommand("Weather" + i);
            weatherMenu.add(cityItems[i]);
        }
    }

    public void setMenuBar(JMenuBar menuBar) {
        this.menuBar = menuBar;
    }

    public Text getPanelSee() {
        return panelSee;
    }

    public void setPanelSee(Text panelSee) {
        this.panelSee = panelSee;
    }

    public Text getPanelEdit() {
        return panelEdit;
    }

    public void setPanelEdit(Text panelEdit) {
        this.panelEdit = panelEdit;
    }

    public LoadFile getLoadItem() {
        return loadItem;
    }

    public void setLoadItem(LoadFile loadItem) {
        this.loadItem = loadItem;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public GridLayout getGrid() {
        return grid;
    }

    public void setGrid(GridLayout grid) {
        this.grid = grid;
    }
}