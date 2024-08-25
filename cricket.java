import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class cricket extends JFrame {

    private JTextField nameField, ageField, runsField, wicketsField, experienceField, teamNameField;

    public cricket() {
        super("CRICKET");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Create components
        nameField = new JTextField(20);
        ageField = new JTextField(5);
        runsField = new JTextField(5);
        wicketsField = new JTextField(5);
        experienceField = new JTextField(10);
        teamNameField = new JTextField(20);

        JButton insertButton = new JButton("Insert");
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertData();
            }
        });

        JButton viewButton = new JButton("View");
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewData();
            }
        });

        JButton modifyButton = new JButton("Modify");
        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyData();
            }
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteData();
            }
        });

        // Create layout
        setLayout(new GridLayout(9, 2));

        // Add components to the layout
        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Age:"));
        add(ageField);
        add(new JLabel("Runs:"));
        add(runsField);
        add(new JLabel("Weickets"));
        add(wicketsField);
        add(new JLabel("Experience:"));
        add(experienceField);
        add(new JLabel("Team Name:"));
        add(teamNameField);
        add(insertButton);
        add(viewButton);
        add(modifyButton);
        add(deleteButton);

        pack();
    }

    private void insertData() {
        String url = "jdbc:mysql://localhost:3306/fms";
        String username = "root";
        String password = "Jay1";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "INSERT INTO cricket (Pname, age, runs, wickets, experience, team_name) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, nameField.getText());
                preparedStatement.setInt(2, Integer.parseInt(ageField.getText()));
                preparedStatement.setDouble(3, Double.parseDouble(runsField.getText()));
                preparedStatement.setDouble(4, Double.parseDouble(wicketsField.getText()));
                preparedStatement.setString(5, experienceField.getText());
                preparedStatement.setString(6, teamNameField.getText());

                preparedStatement.executeUpdate();

                JOptionPane.showMessageDialog(this, "Data inserted successfully!");
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void viewData() {
        String url = "jdbc:mysql://localhost:3306/fms";
        String username = "root";
        String password = "Jay1";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT Pname, age, runs, wickets, experience, team_name FROM cricket";
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(query);

                // Create a new JFrame for displaying the data in a table
                JFrame viewFrame = new JFrame("Player Data");
                JTable table = new JTable(buildTableModel(resultSet));

                // Add the JTable to the viewFrame
                JScrollPane scrollPane = new JScrollPane(table);
                viewFrame.add(scrollPane);

                // Set properties for the viewFrame
                viewFrame.setSize(600, 400);
                viewFrame.setLocationRelativeTo(null);
                viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                viewFrame.setVisible(true);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void modifyData() {
        String url = "jdbc:mysql://localhost:3306/fms";
        String username = "root";
        String password = "Jay1";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String nameToUpdate = JOptionPane.showInputDialog(this, "Enter the name to modify:");
            String query = "SELECT * FROM cricket WHERE Pname = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, nameToUpdate);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // Player found, allow modification
                    String newName = JOptionPane.showInputDialog(this, "Enter the new name:", resultSet.getString("Pname"));
                    int newAge = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter the new age:", resultSet.getInt("age")));
                    double newrunst = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter the new runs:", resultSet.getDouble("runs")));
                    double newWickets = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter the new wickets:", resultSet.getDouble("wickets")));
                    String newExperience = JOptionPane.showInputDialog(this, "Enter the new experience:", resultSet.getString("experience"));
                    String newTeamName = JOptionPane.showInputDialog(this, "Enter the new team name:", resultSet.getString("team_name"));

                    // Update the record
                    String updateQuery = "UPDATE cricket SET Pname=?, age=?, runs=?, wickets=?, experience=?, team_name=? WHERE Pname=?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        updateStatement.setString(1, newName);
                        updateStatement.setInt(2, newAge);
                        updateStatement.setDouble(3, newrunst);
                        updateStatement.setDouble(4, newWickets);
                        updateStatement.setString(5, newExperience);
                        updateStatement.setString(6, newTeamName);
                        updateStatement.setString(7, nameToUpdate);

                        updateStatement.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Data modified successfully!");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Player not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deleteData() {
        String url = "jdbc:mysql://localhost:3306/fms";
        String username = "root";
        String password = "Jay1";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String nameToDelete = JOptionPane.showInputDialog(this, "Enter the name to delete:");
            String query = "SELECT * FROM cricket WHERE Pname = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, nameToDelete);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // Player found, allow deletion
                    int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + nameToDelete + "?");
                    if (option == JOptionPane.YES_OPTION) {
                        // Delete the record
                        String deleteQuery = "DELETE FROM cricket WHERE Pname=?";
                        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                            deleteStatement.setString(1, nameToDelete);
                            deleteStatement.executeUpdate();
                            JOptionPane.showMessageDialog(this, "Data deleted successfully!");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Player not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Convert ResultSet to DefaultTableModel for JTable
    private static DefaultTableModel buildTableModel(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();

        // Create column names
        int columnCount = metaData.getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }

        // Create data for JTable
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        while (resultSet.next()) {
            Object[] rowData = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                rowData[i - 1] = resultSet.getObject(i);
            }
            tableModel.addRow(rowData);
        }

        return tableModel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            cricket app = new cricket();
            app.setVisible(true);
        });
    }
}
