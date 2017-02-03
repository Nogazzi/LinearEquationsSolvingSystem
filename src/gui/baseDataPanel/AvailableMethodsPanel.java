package gui.baseDataPanel;

import gui.calculationMethods.LinearEquationMethods;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Nogaz on 02.01.2017.
 */
public class AvailableMethodsPanel extends JPanel {
    private int prefWidth = 400;
    private int prefHeight = 450;

    ButtonGroup methodButtonsGroup;
    private String[] methods = {"Eliminacja Gaussa-Seidla", "Rozkład LU", "Jacobiego", "Gaussa-Seidla", "SOR"};
    public static final String ElimGausSeidl = "Eliminacja Gaussa-Seidla";
    public static final String LUdecomposition = "Rozkład LU";
    public static final String JacobiMethod = "Jacobiego";
    public static final String GaussaSeidla = "Gaussa-Seidla";

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
        parameterPanel.add(parameterText);
        parameterPanel.add(parameterInput);
        parameterInput.setEnabled(false);

        maxIterationNumberPanel = new JPanel();
        maxIterationText = new JLabel("Max iterations: ");
        maxIterationNumberInput = new JTextField(5);
        maxIterationNumberInput.setHorizontalAlignment(JTextField.CENTER);
        maxIterationNumberPanel.add(maxIterationText);
        maxIterationNumberPanel.add(maxIterationNumberInput);

        precisionPanel = new JPanel();
        precisionText = new JLabel("Precision:");
        precisionInput = new JTextField(5);
        precisionInput.setHorizontalAlignment(JTextField.CENTER);
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

    class AvailableMethodsActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if( methodButtonsGroup.getSelection().getActionCommand().equals("SOR") ){
                System.out.println("Aktywny " + methodButtonsGroup.getSelection().getActionCommand().toString());
                parameterInput.setEnabled(true);
            }else{
                System.out.println("Aktywny " + methodButtonsGroup.getSelection().getActionCommand().toString());
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
