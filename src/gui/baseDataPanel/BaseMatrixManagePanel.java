package gui.baseDataPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.Integer.getInteger;

/**
 * Created by Nogaz on 02.01.2017.
 */
public class BaseMatrixManagePanel extends JPanel implements ActionListener{
    private int prefWidth = 1200;
    private int prefHeight = 450;

    String[] matrixSizeList = {"3x3", "4x4", "5x5", "6x6"};


    //matrix size
    private final String matrixSizeText = "Choose matrix size:";
    private JLabel matrixSizeLabel;
    private JComboBox<String> matrixSize;

    BaseMatrixPanel matrixPanel;


    public BaseMatrixManagePanel(){
        super();
        this.setPreferredSize(new Dimension(prefWidth, prefHeight));

        matrixPanel = new BaseMatrixPanel(3);


        matrixSizeLabel = new JLabel(matrixSizeText);
        matrixSize = new JComboBox<>(matrixSizeList);

        matrixSize.addActionListener(this);


        this.add(matrixPanel);
        this.add(matrixSizeLabel);
        this.add(matrixSize);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int selectedValueIndex = matrixSize.getSelectedIndex();
        String selectedValueName = matrixSizeList[selectedValueIndex];
        System.out.println("Matrix size changed, new value: " + matrixSizeList[selectedValueIndex]);
        String[] splittedVal = selectedValueName.split("x");
        System.out.println(splittedVal[0]);
        String strSize = splittedVal[0];
        int newSize = Integer.parseInt(strSize);
        System.out.println("Value passed to new size: " + newSize);

        matrixPanel.changeMatrixASize(newSize);
        matrixPanel.repaint();
        matrixPanel.revalidate();
    }

    public double[] getMatrixBData() {
        return matrixPanel.getMatrixBData();
    }

    public double[][] getMatrixAData() {
        return matrixPanel.getMatrixAData();
    }


}
