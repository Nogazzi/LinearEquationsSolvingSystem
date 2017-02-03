package gui.baseDataPanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by Nogaz on 04.01.2017.
 */
public class BaseMatrixPanel extends JPanel{


    MatrixA matrixA;
    MatrixX matrixX;
    MatrixB matrixB;
    int matrixSize;

    JLabel equationSign;


    public BaseMatrixPanel(int matrixASize){
        super();
        this.matrixSize = matrixASize;

        matrixA = new MatrixA(2);
        matrixX = new MatrixX(2);
        matrixB = new MatrixB(2);
        equationSign = new JLabel("=");

        this.add(matrixA);
        this.add(matrixX);
        this.add(equationSign);
        this.add(matrixB);

    }
    public void changeMatrixASize(int newSize){
        System.out.println("BaseMatrixPanel....changeMatrixSize(" + newSize + ")" );
        this.remove(matrixA);
        this.remove(matrixX);
        this.remove(equationSign);
        this.remove(matrixB);
        matrixA = new MatrixA(newSize);
        matrixX = new MatrixX(newSize);
        equationSign = new JLabel("=");
        matrixB = new MatrixB(newSize);

        this.add(matrixA);
        this.add(matrixX);
        this.add(equationSign);
        this.add(matrixB);
        this.repaint();
        this.revalidate();
    }


    public double[][] getMatrixAData() {
        return matrixA.getMartixAData();
    }

    public double[] getMatrixBData() {
        return matrixB.getMatrixBData();
    }
}
