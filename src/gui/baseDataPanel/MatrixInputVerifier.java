package gui.baseDataPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Nogaz on 06.01.2017.
 */
public class MatrixInputVerifier extends InputVerifier{
    @Override
    public boolean verify(JComponent input) {
        JTextField tf = (JTextField)input;
        double number;
        try {
            number = Double.parseDouble(tf.getText());
            System.out.println("Current string is double");
            tf.setBackground(Color.white);

        }catch (NumberFormatException e){
            System.out.println("Current string is not a double");
            tf.setBackground(Color.red);
            return false;
        }
        return true;
    }
}
