package gui.baseDataPanel;

import gui.calculationMethods.LinearEquationMethods;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;

/**
 * Created by Nogaz on 02.01.2017.
 */
public class AvailableMethodsPanel extends JPanel {
    private int prefWidth = 400;
    private int prefHeight = 450;

    ButtonGroup methodButtonsGroup;
    private String[] methods = {ElimGausSeidl, LUdecomposition, JacobiMethod, GaussaSeidla, SOR};
    public static final String ElimGausSeidl = "Eliminacja Gaussa";
    public static final String LUdecomposition = "Rozkład LU";
    public static final String JacobiMethod = "Metoda Jacobiego";
    public static final String GaussaSeidla = "Metoda Gaussa-Seidla";
    public static final String SOR = "Metoda SOR";

    JPanel parameterPanel;
    JLabel parameterText;
    JTextField parameterInput;

    JPanel maxIterationNumberPanel;
    JLabel maxIterationText;
    JTextField maxIterationNumberInput;

    JPanel precisionPanel;
    JLabel precisionText;
    JTextField precisionInput;

    private JButton startEquationButton;
    public AvailableMethodsPanel(){
        super();
        TitledBorder title;
        title = BorderFactory.createTitledBorder("Available methods");
        this.setBorder(title);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(prefWidth, prefHeight));

        methodButtonsGroup = new ButtonGroup();
        for (String methodName: methods) {
            JRadioButton radioButton = new JRadioButton(methodName);
            radioButton.setActionCommand(methodName);
            radioButton.addActionListener(new AvailableMethodsActionListener());
            methodButtonsGroup.add(radioButton);
            this.add(radioButton);
            if( methodButtonsGroup.getButtonCount() == 1){
                radioButton.setSelected(true);
            }
            //this.add(new JCheckBox(methodName));
        }

        //this.add(methodButtonsGroup);

        parameterPanel = new JPanel();
        parameterText = new JLabel("Parameter: ");
        parameterInput = new JTextField(5);
        parameterInput.setHorizontalAlignment(JTextField.CENTER);
        parameterInput.setInputVerifier(new MatrixInputVerifier());
        parameterInput.setText("1.1");
        parameterPanel.add(parameterText);
        parameterPanel.add(parameterInput);
        parameterInput.setEnabled(false);

        maxIterationNumberPanel = new JPanel();
        maxIterationText = new JLabel("Max iterations: ");
        maxIterationNumberInput = new JTextField(5);
        maxIterationNumberInput.setHorizontalAlignment(JTextField.CENTER);
        maxIterationNumberInput.setText("30");
        maxIterationNumberPanel.add(maxIterationText);
        maxIterationNumberPanel.add(maxIterationNumberInput);

        precisionPanel = new JPanel();
        precisionText = new JLabel("Precision:");
        precisionInput = new JTextField(5);
        precisionInput.setHorizontalAlignment(JTextField.CENTER);
        precisionInput.setInputVerifier(new MatrixInputVerifier());
        precisionInput.setText("0.002");
        precisionPanel.add(precisionText);
        precisionPanel.add(precisionInput);


        this.add(parameterPanel);
        this.add(maxIterationNumberPanel);
        this.add(precisionPanel);
        startEquationButton = new JButton("Start!");
        this.add(startEquationButton);
    }

    public JButton getStartButton() {
        return this.startEquationButton;
    }

    public double getWspolczynnikSOR() {
        try{
            double parameter = Double.parseDouble(parameterInput.getText());
            return parameter;
        }catch ( NumberFormatException e){
            throw new NumberFormatException();
        }
    }

    public double getPrecisionParameter() {
        try{
            double precisionParameter = Double.parseDouble(precisionInput.getText());
            return precisionParameter;
        }catch ( NumberFormatException e){
            throw new NumberFormatException();
        }
    }

    public int getMaxIterationsNumber()  {
        try {
            int result = Integer.parseInt(maxIterationNumberInput.getText());
            return result;
        }catch ( NumberFormatException e){
            throw new NumberFormatException();
        }
    }

    class AvailableMethodsActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if( getSelectedMethod().equals(AvailableMethodsPanel.SOR) ){
                System.out.println("Aktywny " + getSelectedMethod());
                parameterInput.setEnabled(true);

            }else{
                System.out.println("Aktywny " + getSelectedMethod());
                parameterInput.setEnabled(false);
            }


            /*
            if( e.getActionCommand().equals("SOR") ){
                //aktywuj parametr
                parameterPanel.setEnabled(true);
            }else{
                //deaktywuj parametr
                parameterPanel.setEnabled(false);
            }*/
        }
    }

    public String getSelectedMethod(){
        return methodButtonsGroup.getSelection().getActionCommand();
    }
/*
    class StartEquationsButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            switch( methodButtonsGroup.getSelection().getActionCommand() ){
                case LUdecomposition:
                    LinearEquationMethods.LUDecompositionMethod();
                    break;

            }
        }
    }*/
}
