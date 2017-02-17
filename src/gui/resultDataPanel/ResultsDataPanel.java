package gui.resultDataPanel;

import gui.resultDataPanel.EquationResultPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Nogaz on 02.01.2017.
 */
public class ResultsDataPanel extends JTabbedPane {
    private int prefWidth = 1600;
    private int prefHeight = 450;
    private ArrayList<EquationResultPanel> resultPanels;
    public ResultsDataPanel(){
        super();
        this.setPreferredSize(new Dimension(prefWidth, prefHeight));

        resultPanels = new ArrayList<>();

        EquationResultPanel firstEquationDataPanel = new FirstEquationResultPanel();
        resultPanels.add(new EquationResultPanel("Metoda Jacobiego"));
        for (EquationResultPanel panel: resultPanels) {
            this.addTab(panel.getMethodName(), panel);

        }
    }

    public void addNewTab(ResultDataObject resultDataObject){
        EquationResultPanel newPanel = new EquationResultPanel(resultDataObject);
        String tabName = "#";
        tabName += (resultPanels.size() + 1) + " ";
        tabName += newPanel.getMethodName();
        resultPanels.add(newPanel);
        this.add(tabName, newPanel);
    }

    public void removeTab(){

    }
}
