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

            additionalInfoPanel.setLayout(new BoxLayout(additionalInfoPanel, BoxLayout.PAGE_AXIS));



            if( resultDataObject.isCompleted() ){
                //powodzenie w obliczeniach
                //wiersz czasu obliczeń
                JPanel computTimePanel = new JPanel();
                computTimePanel.setLayout(new BoxLayout(computTimePanel, BoxLayout.LINE_AXIS));
                computTimePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                computTimePanel.add(new JLabel("Czas obliczeń: "));
                computTimePanel.add(new JLabel(String.valueOf(resultDataObject.getComputationsTime()) + "ms"));

                //wiersz precyzji obliczeń
                JPanel precisionPanel = new JPanel();
                precisionPanel.setLayout(new BoxLayout(precisionPanel, BoxLayout.LINE_AXIS));
                precisionPanel.add(new JLabel("Precyzja obliczeń: "));
                precisionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                if( resultDataObject.getMethodName().equals(AvailableMethodsPanel.JacobiMethod)     ||
                        resultDataObject.getMethodName().equals(AvailableMethodsPanel.GaussaSeidla) ||
                        resultDataObject.getMethodName().equals(AvailableMethodsPanel.SOR)     ){
                    precisionPanel.add(new JLabel(String.valueOf(resultDataObject.getPrecision())));
                }else{
                    precisionPanel.add(new JLabel("Nie dotyczy"));
                }

                //wierz liczby iteracji
                JPanel iterationsPanel = new JPanel();
                iterationsPanel.setLayout(new BoxLayout(iterationsPanel, BoxLayout.LINE_AXIS));
                iterationsPanel.add(new JLabel("Liczba iteracji: "));
                iterationsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                if( resultDataObject.getMethodName().equals(AvailableMethodsPanel.JacobiMethod)     ||
                        resultDataObject.getMethodName().equals(AvailableMethodsPanel.GaussaSeidla) ||
                        resultDataObject.getMethodName().equals(AvailableMethodsPanel.SOR)     ){
                    iterationsPanel.add(new JLabel(String.valueOf(resultDataObject.getIterationsNumber())));
                }else {
                    iterationsPanel.add(new JLabel("Nie dotyczy"));
                }
                //wierz parametru sor
                JPanel sorParamPanel = new JPanel();
                sorParamPanel.setLayout(new BoxLayout(sorParamPanel, BoxLayout.LINE_AXIS));
                sorParamPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                sorParamPanel.add(new JLabel("Parametr relaksacji SOR: "));
                if( resultDataObject.getMethodName().equals(AvailableMethodsPanel.SOR) ){
                    sorParamPanel.add(new JLabel(String.valueOf(resultDataObject.getParameter())));
                }else{
                    sorParamPanel.add(new JLabel("Nie dotyczy"));
                }

                additionalInfoPanel.add(computTimePanel);
                additionalInfoPanel.add(precisionPanel);
                additionalInfoPanel.add(iterationsPanel);
                additionalInfoPanel.add(sorParamPanel);

            }else{
                //niepowodzenie w obliczeniach
                JPanel computTimePanel = new JPanel();
                computTimePanel.setLayout(new BoxLayout(computTimePanel, BoxLayout.LINE_AXIS));
                computTimePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                computTimePanel.add(new JLabel("Czas obliczeń: "));
                computTimePanel.add(new JLabel("Nie dotyczy"));

                JPanel precisionPanel = new JPanel();
                precisionPanel.setLayout(new BoxLayout(precisionPanel, BoxLayout.LINE_AXIS));
                precisionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                precisionPanel.add(new JLabel("Precyzja obliczeń: "));
                precisionPanel.add(new JLabel("Nie dotyczy"));

                JPanel iterationsPanel = new JPanel();
                iterationsPanel.setLayout(new BoxLayout(iterationsPanel, BoxLayout.LINE_AXIS));
                iterationsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                iterationsPanel.add(new JLabel("Liczba iteracji: "));
                iterationsPanel.add(new JLabel("Nie dotyczy"));

                JPanel sorParamPanel = new JPanel();
                sorParamPanel.setLayout(new BoxLayout(sorParamPanel, BoxLayout.LINE_AXIS));
                sorParamPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                sorParamPanel.add(new JLabel("Parametr relaksacji SOR: "));
                sorParamPanel.add(new JLabel("Nie dotyczy"));

                additionalInfoPanel.add(computTimePanel);
                additionalInfoPanel.add(precisionPanel);
                additionalInfoPanel.add(iterationsPanel);
                additionalInfoPanel.add(sorParamPanel);
            }


            this.add(additionalInfoPanel, BorderLayout.CENTER);
        }
    }
}
