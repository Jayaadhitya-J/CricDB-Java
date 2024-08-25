import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuDrivenProgram extends JFrame implements ActionListener {
    private JButton newRecordsButton;
    private JButton updateRecordsButton;
    private JButton retrieveRecordsButton;
    private JButton exitButton;

    public MenuDrivenProgram() {
        setTitle("Menu Driven Program");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        newRecordsButton = new JButton("New Records");
        newRecordsButton.addActionListener(this);
        add(newRecordsButton);

        updateRecordsButton = new JButton("Update Records");
        updateRecordsButton.addActionListener(this);
        add(updateRecordsButton);

        retrieveRecordsButton = new JButton("Retrieve Records");
        retrieveRecordsButton.addActionListener(this);
        add(retrieveRecordsButton);

        exitButton = new JButton("Exit");
        exitButton.addActionListener(this);
        add(exitButton);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newRecordsButton) {
            JOptionPane.showMessageDialog(this, "New Records option selected");
        } else if (e.getSource() == updateRecordsButton) {
            JOptionPane.showMessageDialog(this, "Update Records option selected");
        } else if (e.getSource() == retrieveRecordsButton) {
            JOptionPane.showMessageDialog(this, "Retrieve Records option selected");
        } else if (e.getSource() == exitButton) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?");
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MenuDrivenProgram();
            }
        });
    }
}
