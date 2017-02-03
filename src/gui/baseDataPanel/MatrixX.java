package gui.baseDataPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by Nogaz on 06.01.2017.
 */
public class MatrixX extends JPanel {
    double[] matrixXData;
    JLabel[] matrixTextAreas;

    TitledBorder title;

    public MatrixX(int size) {
        super();
        matrixXData = new double[size];
        matrixTextAreas = new JLabel[size];

        this.setLayout(new BorderLayout());
        title = BorderFactory.createTitledBorder("X");
        title.setTitleJustification(TitledBorder.CENTER);
        this.setBorder(title);

        setLayout(new GridLayout(size, 1, 5, 10));

        for (int i = 0; i < size; ++i) {
            matrixTextAreas[i] = new JLabel();
            matrixTextAreas[i].setHorizontalAlignment(JTextField.CENTER);
            //matrixTextAreas[i][j].setMaximumSize();
            matrixTextAreas[i].setText("x" + ((int)i+1));
            this.add(matrixTextAreas[i]);

        }
    }

}
