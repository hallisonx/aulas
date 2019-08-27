package estudo;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class CalcularPonto extends JFrame{
    
    public static void main(String[] args) {
        
    	JFileChooser jfc = new JFileChooser("C:\\Users\\hallison\\Documents\\Processing\\ponto\\");
		int returnValue = jfc.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jfc.getSelectedFile();
			System.out.println(selectedFile.getAbsolutePath());
			FormatandoDatas.calcularPonto(selectedFile.getAbsolutePath());
		}
 
    }

}
