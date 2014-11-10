package automenta.vivisect.swing;

import javax.swing.JButton;



/**
 * Button using FontAwesome icon as a label
 */
public class AwesomeButton extends JButton {

    public AwesomeButton(char faCode) {
        super();
        setFont(Swing.FontAwesome);
        setText(String.valueOf(faCode));
    }
}
