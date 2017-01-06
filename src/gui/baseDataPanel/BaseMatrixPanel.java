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
    int matrixASize;
    TitledBorder title;

    public BaseMatrixPanel(int matrixASize){
        super();
        this.matrixASize = matrixASize;
        this.setLayout(new BorderLayout());
        title = BorderFactory.createTitledBorder("A");
        title.setTitleJustification(TitledBorder.CENTER);
        this.setBorder(title);
        matrixA = new MatrixA(matrixASize);
        this.add(matrixA);

    }
    public void changeMatrixASize(int newSize){
        System.out.println("BaseMatrixPanel....changeMatrixSize(" + newSize + ")" );
        matrixA = new MatrixA(newSize);
        this.remove(matrixA);
        this.add(matrixA);
    this.repaint();
    }


}
