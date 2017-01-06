package gui.baseDataPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by Nogaz on 02.01.2017.
 */
public class AvailableMethodsPanel extends JPanel {
    private int prefWidth = 400;
    private int prefHeight = 450;
    private String[] methods = {"Eliminacja Gaussa-Seidla", "Rozkład LU", "Jacobiego", "Gaussa-Seidla", "SOR"};
    private JButton startEquationButton;
    public AvailableMethodsPanel(){
        super();
        TitledBorder title;
        title = BorderFactory.createTitledBorder("Available methods");
        this.setBorder(title);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(prefWidth, prefHeight));
        for (String methodName: methods) {
            this.add(new JCheckBox(methodName));
        }
        startEquationButton = new JButton("Start!");
        this.add(startEquationButton);
    }
}
