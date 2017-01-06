package gui;


import gui.baseDataPanel.EquationsDataPanel;
import gui.resultDataPanel.ResultsDataPanel;

import javax.swing.*;

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


        this.pack();
        this.setVisible(true);
    }


}
