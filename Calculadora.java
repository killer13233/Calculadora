/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calculadoraap;

/**
 *
 * @author Usuario
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Calculadora simple en Java
 * Para usar en NetBeans: File > New Project > Java Application
 * Luego pega este código en el archivo main o crea una nueva clase.
 */
public class Calculadora extends JFrame implements ActionListener {

    // Pantalla
    private JTextField pantalla;

    // Variables de cálculo
    private double numero1 = 0;
    private String operador = "";
    private boolean nuevaEntrada = true;

    // -------------------------
    //  Constructor
    // -------------------------
    public Calculadora() {
        setTitle("Calculadora");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null); // Centra la ventana

        // Panel principal
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(45, 45, 48));

        // Pantalla
        pantalla = new JTextField("0");
        pantalla.setFont(new Font("Consolas", Font.BOLD, 28));
        pantalla.setHorizontalAlignment(JTextField.RIGHT);
        pantalla.setEditable(false);
        pantalla.setBackground(new Color(30, 30, 30));
        pantalla.setForeground(Color.WHITE);
        pantalla.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(pantalla, BorderLayout.NORTH);

        // Panel de botones
        JPanel botones = new JPanel(new GridLayout(5, 4, 5, 5));
        botones.setBackground(new Color(45, 45, 48));

        // Textos de los botones (en orden de grilla)
        String[] etiquetas = {
            "C",  "±",  "%",  "÷",
            "7",  "8",  "9",  "×",
            "4",  "5",  "6",  "-",
            "1",  "2",  "3",  "+",
            "0",  ".",  "⌫",  "="
        };

        for (String txt : etiquetas) {
            JButton btn = crearBoton(txt);
            botones.add(btn);
        }

        panel.add(botones, BorderLayout.CENTER);
        add(panel);
        setVisible(true);
    }

    // -------------------------
    //  Crear botón con estilo
    // -------------------------
    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Consolas", Font.BOLD, 18));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(this);

        // Colores según tipo de botón
        if (texto.equals("=")) {
            btn.setBackground(new Color(255, 149, 0));   // Naranja
            btn.setForeground(Color.WHITE);
        } else if (texto.equals("÷") || texto.equals("×") ||
                   texto.equals("-") || texto.equals("+")) {
            btn.setBackground(new Color(80, 80, 85));    // Gris oscuro
            btn.setForeground(new Color(255, 149, 0));
        } else if (texto.equals("C") || texto.equals("±") || texto.equals("%")) {
            btn.setBackground(new Color(100, 100, 105)); // Gris medio
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(new Color(60, 60, 65));    // Gris número
            btn.setForeground(Color.WHITE);
        }

        return btn;
    }

    // -------------------------
    //  Lógica de botones
    // -------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        switch (cmd) {

            // Dígitos y punto decimal
            case "0": case "1": case "2": case "3": case "4":
            case "5": case "6": case "7": case "8": case "9":
                if (nuevaEntrada) {
                    pantalla.setText(cmd);
                    nuevaEntrada = false;
                } else {
                    if (pantalla.getText().equals("0")) {
                        pantalla.setText(cmd);
                    } else {
                        pantalla.setText(pantalla.getText() + cmd);
                    }
                }
                break;

            case ".":
                if (nuevaEntrada) {
                    pantalla.setText("0.");
                    nuevaEntrada = false;
                } else if (!pantalla.getText().contains(".")) {
                    pantalla.setText(pantalla.getText() + ".");
                }
                break;

            // Borrar último dígito
            case "⌫":
                String actual = pantalla.getText();
                if (actual.length() > 1) {
                    pantalla.setText(actual.substring(0, actual.length() - 1));
                } else {
                    pantalla.setText("0");
                    nuevaEntrada = true;
                }
                break;

            // Limpiar todo
            case "C":
                pantalla.setText("0");
                numero1 = 0;
                operador = "";
                nuevaEntrada = true;
                break;

            // Cambiar signo
            case "±":
                double val = Double.parseDouble(pantalla.getText());
                pantalla.setText(formatear(-val));
                break;

            // Porcentaje
            case "%":
                double pct = Double.parseDouble(pantalla.getText()) / 100.0;
                pantalla.setText(formatear(pct));
                nuevaEntrada = true;
                break;

            // Operadores
            case "+": case "-": case "×": case "÷":
                numero1 = Double.parseDouble(pantalla.getText());
                operador = cmd;
                nuevaEntrada = true;
                break;

            // Igual
            case "=":
                if (!operador.isEmpty()) {
                    double numero2 = Double.parseDouble(pantalla.getText());
                    double resultado = calcular(numero1, numero2, operador);
                    pantalla.setText(formatear(resultado));
                    operador = "";
                    nuevaEntrada = true;
                }
                break;
        }
    }

    // -------------------------
    //  Operación aritmética
    // -------------------------
    private double calcular(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "×": return a * b;
            case "÷": return (b != 0) ? a / b : 0; // Evita división por cero
            default:  return b;
        }
    }

    // -------------------------
    //  Formatear resultado
    //  (evita mostrar ".0" innecesario)
    // -------------------------
    private String formatear(double num) {
        if (num == (long) num) {
            return String.valueOf((long) num);
        } else {
            return String.valueOf(num);
        }
    }

    // -------------------------
    //  Main
    // -------------------------
    public static void main(String[] args) {
        // Usa el Look & Feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(Calculadora::new);
    }
}
