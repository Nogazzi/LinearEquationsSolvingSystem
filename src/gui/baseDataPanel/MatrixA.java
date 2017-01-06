package gui.baseDataPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Nogaz on 06.01.2017.
 */
public class MatrixA extends JPanel {
    double[][] martixAData;
    JTextField[][] matrixTextAreas;

    public MatrixA(int size){
        super();
        martixAData = new double[size][size];
        matrixTextAreas = new JTextField[size][size];
        setLayout(new GridLayout(size, size, 5, 5));

        for( int i = 0 ; i < size ; ++i ){
            for( int j = 0 ; j < size ; ++j ){
                matrixTextAreas[i][j] = new JTextField(10);
                matrixTextAreas[i][j].setHorizontalAlignment(JTextField.CENTER);
                //matrixTextAreas[i][j].setMaximumSize();
                matrixTextAreas[i][j].setText("0");
                matrixTextAreas[i][j].setInputVerifier(new MatrixInputVerifier());
                this.add(matrixTextAreas[i][j]);
            }
        }

    }

    public double[][] getMartixData(){
        return this.martixAData;
    }
}
