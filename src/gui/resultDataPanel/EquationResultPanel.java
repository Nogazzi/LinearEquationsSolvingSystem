package gui.resultDataPanel;

import gui.baseDataPanel.AvailableMethodsPanel;
import gui.calculationMethods.LinearEquationMethods;
import org.omg.PortableInterceptor.INACTIVE;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by Nogaz on 02.01.2017.
 */
public class EquationResultPanel extends JPanel {
    String methodName;
    ResultDataObject resultDataObject;
    Label chosenMetodHead;
    Label[][] matrixA;
    Label[] matrixX;
    Label[] matrixB;

    Label answerHead;
    Label[] matrixXresult;
    Label notCompletedText;
    Label notCompletedReason;

    Label additionalInformationsHead;
    Label calculationsDurationText;
    Label calculationsDurationValue;
    Label precicionText;
    Label precisionValue;
    Label iterationNumberText;
    Label iterationNumberValue;
    Label parameterSORtext;
    Label parameterSORvalue;

    public EquationResultPanel(){


    }
    public EquationResultPanel(String methodName){
        super();
        this.methodName = methodName;
    }
    public EquationResultPanel(ResultDataObject resultDataObject){
        this.resultDataObject = resultDataObject;
        this.methodName = resultDataObject.getMethodName();
        this.setLayout(new BorderLayout());
        JLabel headPanel = new JLabel(resultDataObject.getMethodName());
        JPanel panelWynikow = new JPanel();
        panelWynikow.setLayout(new GridLayout(1, 3, 0, 0));

        this.add(headPanel, BorderLayout.PAGE_START);
        this.add(panelWynikow, BorderLayout.CENTER);

        panelWynikow.add(new InputDataPanel(), BorderLayout.WEST);
        panelWynikow.add(new ResultsInfoDataPanel(), BorderLayout.CENTER);
        panelWynikow.add(new AdditionalInfo(), BorderLayout.EAST);

    }
    public String getMethodName(){
        return this.methodName;
    }

    class InputDataPanel extends JPanel{

        public InputDataPanel(){
            this.setLayout(new BorderLayout(5, 5));


            JPanel matrixPanel = new JPanel();

            TitledBorder matrixABorder = new TitledBorder("A");
            int n = resultDataObject.getMatrixLevel();
            JPanel matrixAPanel = new JPanel();
            matrixAPanel.setLayout(new GridLayout(n, n, 5, 5));
            double[][] matrixAdata = resultDataObject.getMatrixA();
            for( int i = 0 ; i < n ; ++i ){
                for( int j = 0 ; j < n ; ++j ){
                    matrixAPanel.add(new Label(String.valueOf(matrixAdata[i][j])));
                }
            }
            matrixAPanel.setBorder(matrixABorder);
            matrixPanel.add(matrixAPanel);


            TitledBorder matrixXBorder = new TitledBorder("X");
            JPanel matrixXPanel = new JPanel();
            matrixXPanel.setLayout(new GridLayout(n, 1, 5, 10));
            for( int i = 0 ; i < n ; ++i){
                String label = "x";
                label += String.valueOf(i+1);
                matrixXPanel.add(new Label(label));
            }
            matrixXPanel.setBorder(matrixXBorder);
            matrixPanel.add(matrixXPanel);

            TitledBorder matrixBBorder = new TitledBorder("B");
            JPanel matrixBPanel = new JPanel();
            matrixBPanel.setLayout(new GridLayout(n, 1, 5, 5));
            double[] matrixBdata = resultDataObject.getMatrixB();
            for( int i = 0 ; i < n ; ++i){
                matrixBPanel.add(new Label(String.valueOf(matrixBdata[i])));
            }
            matrixBPanel.setBorder(matrixBBorder);
            matrixPanel.add(matrixBPanel);
            this.add(matrixPanel, BorderLayout.CENTER);
        }
    }
    class ResultsInfoDataPanel extends JPanel{
        public ResultsInfoDataPanel(){
            this.setLayout(new BorderLayout(5,5));
            this.add(new JLabel("Rozwiązanie:"), BorderLayout.PAGE_START);
            JPanel rozwiazaniePanel = new JPanel();
            int n = resultDataObject.getMatrixLevel();
            if( resultDataObject.completed ){
                //rozwiazaniePanel.setLayout(new GridLayout(1,3,5,5));
                rozwiazaniePanel.setLayout(new FlowLayout());
                TitledBorder matrixXBorder = new TitledBorder("X");
                JPanel matrixXPanel = new JPanel();
                //matrixXPanel.setLayout(new GridLayout(n, 1, 5, 10));
                matrixXPanel.setLayout(new BoxLayout(matrixXPanel, BoxLayout.PAGE_AXIS));
                System.out.println("Wisywanie elementow wyniku:");
                for( int i = 0 ; i < n ; ++i){
                    String label = "x";
                    label += String.valueOf(i+1);
                    System.out.println(label);
                    matrixXPanel.add(new Label(label));
                }
                matrixXPanel.setBorder(matrixXBorder);
                rozwiazaniePanel.add(matrixXPanel);

                JLabel equationSign = new JLabel("=");
                rozwiazaniePanel.add(equationSign);

                TitledBorder matrixXresultBorder = new TitledBorder("X");
                JPanel matrixXResultPanel = new JPanel();
                //matrixXResultPanel.setLayout(new GridLayout(n, 1, 5, 10));
                matrixXResultPanel.setLayout(new BoxLayout(matrixXResultPanel, BoxLayout.PAGE_AXIS));
                double[] matrixXResultData = resultDataObject.getResultX();
                System.out.println("Wisywanie elementow wyniku:");
                for( int i = 0 ; i < n ; ++i){
                    String label = String.valueOf(matrixXResultData[i]);
                    System.out.println(label);
                    matrixXResultPanel.add(new Label(label));
                }
                matrixXResultPanel.setBorder(matrixXresultBorder);
                rozwiazaniePanel.add(matrixXResultPanel);
            }else{
                rozwiazaniePanel.setLayout(new GridLayout(2,2,5,5));

                JPanel resultInfoPanel = new JPanel();
                resultInfoPanel.setLayout(new GridLayout(2, 2, 5, 10));

                resultInfoPanel.add(new JLabel("Ostateczne rozwiązanie: "));
                resultInfoPanel.add(new Label("Błąd w obliczeniach"));
                resultInfoPanel.add(new JLabel("Przyczyna: "));
                resultInfoPanel.add(new Label(resultDataObject.getReason()));

                rozwiazaniePanel.add(resultInfoPanel);
            }
            this.add(rozwiazaniePanel, BorderLayout.CENTER);
        }
    }
    class AdditionalInfo extends JPanel{

        public AdditionalInfo(){
            this.setLayout(new BorderLayout(5,5));
            this.add(new JLabel("Dodatkowe informacje"), BorderLayout.PAGE_START);
            JPanel additionalInfoPanel = new JPanel();
            int infoRows;

            additionalInfoPanel.setLayout(new GridLayout(4, 2, 5, 10));



            if( resultDataObject.isCompleted() ){
                //powodzenie w obliczeniach
                additionalInfoPanel.add(new JLabel("Czas obliczeń: "));
                additionalInfoPanel.add(new JLabel(String.valueOf(resultDataObject.getComputationsTime())));
                additionalInfoPanel.add(new JLabel("Precyzja obliczeń: "));
                if( resultDataObject.getMethodName().equals(AvailableMethodsPanel.JacobiMethod)     ||
                        resultDataObject.getMethodName().equals(AvailableMethodsPanel.GaussaSeidla) ||
                        resultDataObject.getMethodName().equals(AvailableMethodsPanel.SOR)     ){
                    additionalInfoPanel.add(new JLabel(String.valueOf(resultDataObject.getPrecision())));
                }else{
                    additionalInfoPanel.add(new JLabel("Nie dotyczy"));
                }
                additionalInfoPanel.add(new JLabel("Liczba iteracji: "));
                if( resultDataObject.getMethodName().equals(AvailableMethodsPanel.JacobiMethod)     ||
                        resultDataObject.getMethodName().equals(AvailableMethodsPanel.GaussaSeidla) ||
                        resultDataObject.getMethodName().equals(AvailableMethodsPanel.SOR)     ){
                    additionalInfoPanel.add(new JLabel(String.valueOf(resultDataObject.getIterationsNumber())));
                }
                additionalInfoPanel.add(new JLabel("Nie dotyczy"));


                additionalInfoPanel.add(new JLabel("Parametr relaksacji SOR: "));
                if( resultDataObject.getMethodName().equals(AvailableMethodsPanel.SOR) ){
                    additionalInfoPanel.add(new JLabel(String.valueOf(resultDataObject.getParameter())));
                }else{
                    additionalInfoPanel.add(new JLabel("Nie dotyczy"));
                }

            }else{
                //niepowodzenie w obliczeniach
                additionalInfoPanel.add(new JLabel("Czas obliczeń: "));
                additionalInfoPanel.add(new JLabel("Nie dotyczy"));
                additionalInfoPanel.add(new JLabel("Precyzja obliczeń: "));
                additionalInfoPanel.add(new JLabel("Nie dotyczy"));
                additionalInfoPanel.add(new JLabel("Liczba iteracji: "));
                additionalInfoPanel.add(new JLabel("Nie dotyczy"));
                additionalInfoPanel.add(new JLabel("Parametr relaksacji SOR: "));
                additionalInfoPanel.add(new JLabel("Nie dotyczy"));


            }


            this.add(additionalInfoPanel, BorderLayout.CENTER);
        }
    }
}
