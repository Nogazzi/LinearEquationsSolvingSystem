package gui;


import gui.baseDataPanel.AvailableMethodsPanel;
import gui.baseDataPanel.EquationsDataPanel;
import gui.calculationMethods.LinearEquationMethods;
import gui.resultDataPanel.ResultsDataPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Nogaz on 02.01.2017.
 */
public class MainFrame extends JFrame {
    private int width = 1600;
    private int height = 900;
    private String frameTitle = "Linear equations";
    private JSplitPane splitPane;

    private EquationsDataPanel equationsDataPanel;
    private ResultsDataPanel resultsDataPanel;

    public MainFrame(){
        super();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(height, width);
        this.setTitle(frameTitle);

        splitPane = new JSplitPane();
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

        equationsDataPanel = new EquationsDataPanel();
        resultsDataPanel = new ResultsDataPanel();
        //splitPane.setDividerLocation(0.5d);
        splitPane.setResizeWeight(0.5d);
        splitPane.setTopComponent(equationsDataPanel);
        splitPane.setBottomComponent(resultsDataPanel);


        this.getContentPane().add(splitPane);

        equationsDataPanel.getStartButton().addActionListener(new StartEquationsButtonListener());


        this.pack();
        this.setVisible(true);
    }

    class StartEquationsButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedMethod = equationsDataPanel.getSelectedValue();
            System.out.println("Action command: " + selectedMethod);
            double[][] matrixA = equationsDataPanel.getMatrixAData();
            double[] matrixB = equationsDataPanel.getMatrixBData();
            LinearEquationMethods method = new LinearEquationMethods();
            int iterations;
            double precision;
            double sorParam;
            switch( selectedMethod ){
                case AvailableMethodsPanel.GaussaSeidla:
                    try {
                        iterations = equationsDataPanel.getMaxIterationsNumber();
                        precision = equationsDataPanel.getPrecisionParameter();
                        resultsDataPanel.addNewTab(method.metodaGaussaSeidla(matrixA, matrixB, precision, iterations));
                    }catch (NumberFormatException exception){

                    }
                    break;
                case AvailableMethodsPanel.JacobiMethod:
                    try {
                        iterations = equationsDataPanel.getMaxIterationsNumber();
                        precision = equationsDataPanel.getPrecisionParameter();
                        resultsDataPanel.addNewTab(method.metodaJacobiego(matrixA, matrixB, precision, iterations));
                    }catch (NumberFormatException exception){

                    }
                    break;
                case AvailableMethodsPanel.SOR:
                    try {
                        iterations = equationsDataPanel.getMaxIterationsNumber();
                        precision = equationsDataPanel.getPrecisionParameter();
                        sorParam = equationsDataPanel.getWspolczynnikSOR();
                        resultsDataPanel.addNewTab(method.metodaSOR(matrixA, matrixB, sorParam, precision, iterations));
                    }catch (NumberFormatException exception){

                    }
                    break;
                case AvailableMethodsPanel.ElimGausSeidl:
                    resultsDataPanel.addNewTab(method.metodaEliminacjiGaussa(matrixA, matrixB));
                    break;
                case AvailableMethodsPanel.LUdecomposition:
                    resultsDataPanel.addNewTab(method.metodaRozkladuLU(matrixA, matrixB));
                    break;

            }

        }
    }


}
