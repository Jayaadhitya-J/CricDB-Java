import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginApp extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckBox;

    public LoginApp() {
        super("Login Page");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);

        // Create components
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleShowPassword();
            }
        });

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        // Create layout
        setLayout(new GridLayout(4, 2));

        // Add components to the layout
        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(showPasswordCheckBox);
        add(new JLabel()); // Empty label for spacing
        add(loginButton);

        pack();
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if ("user".equals(username) && "pass".equals(password)) {
            // Correct credentials, open MainApp
            cricket cricket = new cricket();
            cricket.setVisible(true);
            dispose(); // Close the login window
        } else {
            // Incorrect credentials, show error message
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void toggleShowPassword() {
        passwordField.setEchoChar(showPasswordCheckBox.isSelected() ? 0 : '*');
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginApp loginApp = new LoginApp();
            loginApp.setVisible(true);
        });
    }
}
