import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
public class Mock extends JFrame {

    private JTextArea outputTextArea;

    public Mock() {
        super("Cricket Database GUI");

        // Establish the database connection and execute queries
        executeDatabaseOperations();

        // Set up the JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);

        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(outputTextArea);

        add(scrollPane);

        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);

        // Display the main menu
        displayMainMenu();
    }


    private void executeDatabaseOperations() {
        Connection mycon = null;
        Statement cur = null;

        try {
            // Establishing database connection
            mycon = DriverManager.getConnection("jdbc:mysql://localhost:3306/?user=root&password=Jay1");
            cur = mycon.createStatement();

            // Check if the database 'cricket' exists
            if (!databaseExists(mycon)) {
                appendToOutput("Creating database 'cricket'");
                cur.execute("create database cricket");
            }
            cur.execute("use cricket");

            // CREATING REQUIRED TABLES
            cur.execute("create table if not exists team_stats(t_id int(3) primary key,team_name varchar(25),total_matches int(4),total_players int(3),no_of_championships int(4))");
            cur.execute("create table if not exists personal_info(p_id int(3) primary key,name varchar(25),age int(3),height_in_m float(3,1),weight_in_kg float(3,1),years_of_experience int(3),t_id int(3),date_of_join date,foreign key(t_id) references team_stats(t_id) on update cascade on delete cascade)");
            cur.execute("create table if not exists player_stats(p_id int(3),t_id int(3),runs int(7),wickets int(7),catches int(3),matches int(4),boundaries int(7),foreign key(p_id) references personal_info(p_id),foreign key(t_id) references team_stats(t_id) on update cascade on delete cascade)");
            cur.execute("create table if not exists points_table(t_id int(3),team_name varchar(25),wins int(4),loses int(10),draws int(10),total_matches int(4),total_points int(10),foreign key(t_id) references team_stats(t_id) on update cascade on delete cascade)");

            // DEFAULT VALUES
            cur.execute("insert into team_stats values(1,'The Guardians',10,53,2)");
            cur.execute("insert into team_stats values(2,'Fireballs',10,34,4)");
            cur.execute("insert into team_stats values(3,'Vandals',10,47,1)");
            cur.execute("insert into team_stats values(4,'Explorers',10,28,3)");

            cur.execute("insert into personal_info values(1,'Krish',21,1.8,88.3,2,4,'2014-12-03')");
            cur.execute("insert into personal_info values(2,'Zain',23,1.6,73.5,5,1,'2015-05-12')");
            cur.execute("insert into personal_info values(3,'Akarsh',26,1.7,82.7,7,3,'2016-09-30')");
            cur.execute("insert into personal_info values(4,'Shray',28,1.9,92.4,9,2,'2011-02-28')");

            cur.execute("insert into player_stats values(1,4,2342,54,22,51,137)");
            cur.execute("insert into player_stats values(2,1,3463,24,67,83,428)");
            cur.execute("insert into player_stats values(3,3,1942,105,14,72,32)");
            cur.execute("insert into player_stats values(4,2,5569,17,81,127,503)");

            cur.execute("insert into points_table values(1,'The Guardians',10,5,1,16,15)");
            cur.execute("insert into points_table values(2,'Fireballs',13,3,0,16,23)");
            cur.execute("insert into points_table values(3,'Vandals',16,0,0,16,32)");
            cur.execute("insert into points_table values(4,'Explorers',15,0,1,16,30)");

            System.out.println("Database and tables are created successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            appendToOutput("Error: " + e.getMessage());
        } finally {
            try {
                if (cur != null) {
                    cur.close();
                }
                if (mycon != null) {
                    mycon.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean databaseExists(Connection connection) throws SQLException {
        boolean databaseExists = false;
        String databaseName = "cricket";
        String query = "SHOW DATABASES LIKE '" + databaseName + "'";
        try (Statement statement = connection.createStatement();
             var resultSet = statement.executeQuery(query)) {
            databaseExists = resultSet.next();
        }
        return databaseExists;
    }

    private void appendToOutput(String message) {
        outputTextArea.append(message + "\n");
    }

    private void displayMainMenu() {
        while (true) {
            String[] options = {"NEW RECORDS", "UPDATE RECORDS", "RETRIEVE RECORDS", "EXIT"};
            int choice = JOptionPane.showOptionDialog(
                    this,
                    "Please choose an option to perform",
                    "CRICKET",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            switch (choice) {
                case 0:
                    // NEW RECORDS - 
                    appendToOutput("Option 1: NEW RECORDS");
                    addNewRecords();
                    break;
                case 1:
                    // UPDATE RECORDS - Add your implementation here
                    appendToOutput("Option 2: UPDATE RECORDS");
                    break;
                case 2:
                    // RETRIEVE RECORDS - Add your implementation here
                    appendToOutput("Option 3: RETRIEVE RECORDS");
                    break;
                case 3:
                    // EXIT
                    System.exit(0);
                    break;
                default:
                    // Handle unexpected choices
                    appendToOutput("Invalid option selected");
                    break;
            }
        }
    }
    private void addNewRecords() {
        String[] recordTypes = {"Player Info", "Player Stats", "Team Stats", "Points Table", "Exit"};
        int recordTypeChoice = JOptionPane.showOptionDialog(
                this,
                "Choose the record type to add",
                "CRICKET - ADD NEW RECORDS",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                recordTypes,
                recordTypes[0]
        );

        if (recordTypeChoice == 4) {
            return;  // Exit option
        }

        String[] fieldNames;
        String tableName;
        switch (recordTypeChoice) {
            case 0:
                fieldNames = new String[]{"PLAYER ID", "NAME", "AGE", "HEIGHT(in metres)", "WEIGHT(in kilograms)", "YEARS OF EXPERIENCE", "TEAM ID", "DATE OF JOIN(YYYY-MM-DD)"};
                tableName = "personal_info";
                break;
            case 1:
                fieldNames = new String[]{"PLAYER ID", "TEAM ID", "RUNS", "WICKETS", "CATCHES", "MATCHES PLAYED", "BOUNDARIES"};
                tableName = "player_stats";
                break;
            case 2:
                fieldNames = new String[]{"TEAM ID", "TEAM NAME", "TOTAL MATCHES PLAYED", "TOTAL PLAYERS IN THE TEAM", "NUMBER OF CHAMPIONSHIPS WON"};
                tableName = "team_stats";
                break;
            case 3:
                fieldNames = new String[]{"TEAM ID", "TEAM NAME", "WINS", "LOSES", "DRAWS"};
                tableName = "points_table";
                break;
            default:
                return;
        }

        String[] inputValues = new String[fieldNames.length];
        for (int i = 0; i < fieldNames.length; i++) {
            inputValues[i] = JOptionPane.showInputDialog(this, "Enter " + fieldNames[i]);
        }

        String query = "insert into " + tableName + " values(";
        for (int i = 0; i < inputValues.length; i++) {
            query += "'" + inputValues[i] + "'";
            if (i < inputValues.length - 1) {
                query += ",";
            }
        }
        query += ")";
        executeQuery(query);
    }

    private void updateRecords() {
        // Add your implementation for updating records here
        appendToOutput("Option 2: UPDATE RECORDS - Add your implementation here");
    }

    private void retrieveRecords() {
        // Add your implementation for retrieving records here
        appendToOutput("Option 3: RETRIEVE RECORDS - Add your implementation here");
    }

    private void executeQuery(String query) {
        Connection mycon = null;
        Statement cur = null;

        try {
            // Establishing database connection
            mycon = DriverManager.getConnection("jdbc:mysql://localhost:3306/?user=root&password=Jay1");
            cur = mycon.createStatement();
            cur.execute("use cricket");

            cur.execute(query);
            mycon.commit();

            appendToOutput("DATA ADDED SUCCESSFULLY");

        } catch (SQLException e) {
            e.printStackTrace();
            appendToOutput("Error: " + e.getMessage());
        } finally {
            try {
                if (cur != null) {
                    cur.close();
                }
                if (mycon != null) {
                    mycon.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Mock());
    }
}






