package gui.baseDataPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Nogaz on 02.01.2017.
 */
public class EquationsDataPanel extends JPanel{
    private int prefWidth = 1600;
    private int prefHeight = 450;

    AvailableMethodsPanel methodsPanel;
    BaseMatrixManagePanel baseMatrixManagePanel;

    public EquationsDataPanel(){
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setPreferredSize(new Dimension(prefWidth, prefHeight));

        baseMatrixManagePanel = new BaseMatrixManagePanel();
        methodsPanel = new AvailableMethodsPanel();
        this.add(baseMatrixManagePanel);
        this.add(methodsPanel);
    }


    public double[]getMatrixBData() {
        return baseMatrixManagePanel.getMatrixBData();
    }

    public double[][] getMatrixAData() {
        return baseMatrixManagePanel.getMatrixAData();
    }

    public JButton getStartButton() {
        return methodsPanel.getStartButton();
    }

    public String getSelectedValue() {
        return methodsPanel.getSelectedMethod();
    }

    public double getWspolczynnikSOR() {
        return methodsPanel.getWspolczynnikSOR();
    }

    public double getPrecisionParameter() {
        return methodsPanel.getPrecisionParameter();
    }
}
