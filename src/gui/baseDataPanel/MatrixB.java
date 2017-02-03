package gui.baseDataPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by Nogaz on 06.01.2017.
 */
public class MatrixB extends JPanel {
    double[] matrixBData;
    JTextField[] matrixTextAreas;

    TitledBorder title;

    public MatrixB(int size) {
        super();
        matrixBData = new double[size];
        matrixTextAreas = new JTextField[size];

        this.setLayout(new BorderLayout());
        title = BorderFactory.createTitledBorder("B");
        title.setTitleJustification(TitledBorder.CENTER);
        this.setBorder(title);

        setLayout(new GridLayout(size, 1, 5, 5));

        for (int i = 0; i < size; ++i) {
            matrixTextAreas[i] = new JTextField(10);
            matrixTextAreas[i].setHorizontalAlignment(JTextField.CENTER);
            //matrixTextAreas[i][j].setMaximumSize();
            matrixTextAreas[i].setText("0");
            matrixTextAreas[i].setInputVerifier(new MatrixInputVerifier());
            this.add(matrixTextAreas[i]);

        }
    }
    public double[] getMatrixBData(){
        refreshMatrixData();
        return matrixBData;
    }
    private void refreshMatrixData() {

        for( int i = 0 ; i < matrixBData.length ; ++i ){

            matrixBData[i] = Double.parseDouble(matrixTextAreas[i].getText());
        }
    }
}
