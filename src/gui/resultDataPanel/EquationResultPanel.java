package gui.resultDataPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Nogaz on 02.01.2017.
 */
public class EquationResultPanel extends JPanel {
    String methodName;
    public EquationResultPanel(String methodName){
        super();
        this.methodName = methodName;

    }
    public String getMethodName(){
        return this.methodName;
    }
}
