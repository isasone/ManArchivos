package co.edu.uptc.view;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
/**@author Angel Santiago Bedoya Carreño
 *  */
public class Text extends JPanel {
    private JLabel jLabel;
    private JTextArea jTextArea;
    private JScrollPane jScrollPane;

    public Text(int type) {
        jTextArea = new JTextArea();
        jTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12)); 
        jScrollPane = new JScrollPane();
        setLayout(new BorderLayout());

        if (type == 1) {
            jLabel = new JLabel("Ver");
            jTextArea.setEditable(false);
        } else if (type == 2) {
            jLabel = new JLabel("Editar");
            jTextArea.setEditable(true);
        }

        jScrollPane.setViewportView(jTextArea);
        add(jLabel, BorderLayout.NORTH);
        add(jScrollPane, BorderLayout.CENTER);
    }

    public void setText(String text) {
        jTextArea.setText(text);
        jTextArea.setCaretPosition(0);
    }

    public String getText() {
        return jTextArea.getText();
    }
}