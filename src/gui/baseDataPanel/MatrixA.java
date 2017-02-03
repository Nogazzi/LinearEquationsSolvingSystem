package gui.baseDataPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.geom.Arc2D;

/**
 * Created by Nogaz on 06.01.2017.
 */
public class MatrixA extends JPanel {
    double[][] matrixAData;
    JTextField[][] matrixTextAreas;
    TitledBorder title;

    public MatrixA(int size){
        super();
        matrixAData = new double[size][size];
        matrixTextAreas = new JTextField[size][size];

        this.setLayout(new BorderLayout());
        title = BorderFactory.createTitledBorder("A");
        title.setTitleJustification(TitledBorder.CENTER);
        this.setBorder(title);

        setLayout(new GridLayout(size, size, 5, 5));

        for( int i = 0 ; i < size ; ++i ){
            for( int j = 0 ; j < size ; ++j ){
                matrixTextAreas[i][j] = new JTextField(10);
                matrixTextAreas[i][j].setHorizontalAlignment(JTextField.CENTER);
                //matrixTextAreas[i][j].setMaximumSize();
                matrixTextAreas[i][j].setText("0");
                matrixTextAreas[i][j].setInputVerifier(new MatrixInputVerifier());

                //matrixTextAreas[i][j] = setTitleBorder(matrixTextAreas[i][j], i, j);
                this.add(matrixTextAreas[i][j]);

            }
        }

    }

    public double[][] getMartixAData(){
        refreshMatrixData();
        return this.matrixAData;
    }

    private void refreshMatrixData() {
        for( int i = 0 ; i < matrixAData.length ; ++i ){
            for( int j = 0 ; j < matrixAData[0].length ; ++j ) {
                matrixAData[i][j] = Double.parseDouble(matrixTextAreas[i][j].getText());
            }
        }
    }

    public JTextField setTitleBorder(JTextField cell, int i , int j){
        String cellTitle = "["+i+"]"+"["+j+"]";
        TitledBorder cellBorder = BorderFactory.createTitledBorder(cellTitle);
        cell.setBorder(cellBorder);
        return cell;
    }

}
