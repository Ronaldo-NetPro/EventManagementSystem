package ladybugdevs.mztddp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

/**
 *
 * @author Student
 */
public class EventManagerGUI extends JFrame {

    private JTextField nameField, dateField, categoryField,locationField;
    private JTextArea displayArea;
    private JButton saveButton, loadButton, searchButton, deleteButton,clearButton;

    private ArrayList<EventClass> eventList = new ArrayList<>(); // Using ArrayList

    public EventManagerGUI() {
        setTitle("Event Management System");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the heading panel with FlowLayout centered
        JPanel headingPnl = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Event Management System");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC + Font.BOLD, 20));  // Set font size to 18
        titleLabel.setForeground(Color.BLUE);  // Set text color to blue
        titleLabel.setBorder(new BevelBorder(SoftBevelBorder.RAISED));
        headingPnl.add(titleLabel);
        add(headingPnl, BorderLayout.NORTH);
        
        // Top panel for form inputs
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the panel
        inputPanel.add(new JLabel("Event Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Event Date (yyyy-MM-dd):"));
        dateField = new JTextField();
        inputPanel.add(dateField);

        inputPanel.add(new JLabel("Category:"));
        categoryField = new JTextField();
        inputPanel.add(categoryField);
        
        inputPanel.add(new JLabel("Location:"));
        locationField = new JTextField();
        inputPanel.add(locationField);

        add(inputPanel, BorderLayout.NORTH);

        // Center panel for text area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        // Bottom panel for buttons
        //JPanel buttonPanel = new JPanel();
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));  // Add space between buttons
        
        saveButton = new JButton("Save");
        loadButton = new JButton("Load");
        searchButton = new JButton("Search");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");

        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        //setFocusable
        saveButton.setFocusable(false);
        loadButton.setFocusable(false);
        searchButton.setFocusable(false);
        deleteButton.setFocusable(false);
        clearButton.setFocusable(false);
        

        // Add listeners
        saveButton.addActionListener(e -> saveEvent());
        loadButton.addActionListener(e -> loadEvents());
        searchButton.addActionListener(e -> searchEvent());
        deleteButton.addActionListener(e -> deleteEvent());
        clearButton.addActionListener(e -> clearFields());
        
        setVisible(true);
        setResizable(false);
    }

    private void saveEvent() {
        try {
            String name = nameField.getText().trim();
            String dateText = dateField.getText().trim();
            String category = categoryField.getText().trim();
            String location = locationField.getText().trim();

            // 1. Check for empty fields
            if (name.isEmpty() || dateText.isEmpty() || category.isEmpty()|| location.isEmpty()) {
                JOptionPane.showMessageDialog(this,"Please fill in all fields.","Input Error",JOptionPane.WARNING_MESSAGE);
                      
                return;
            }

            // 2. Validate and parse date
            LocalDate date;
            try {
                date = LocalDate.parse(dateText);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,"Invalid date format. Please use yyyy-MM-dd.","Date Format Error",JOptionPane.ERROR_MESSAGE);
                      
                return;
            }

            // 3. Create event and add to list
            EventClass event = new EventClass(name, date, category, location);
            eventList.add(event);

            // 4. Save to file
            File file = new File("events.txt");
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter writer = new BufferedWriter(fw);
            
            writer.write(name + "," + date.format(DateTimeFormatter.ISO_LOCAL_DATE) + "," + category+ "," + location);
            writer.newLine();
            writer.close();

            // 5. Confirmation dialog
            JOptionPane.showMessageDialog(this,"Event saved successfully!","Success",JOptionPane.INFORMATION_MESSAGE);
             
            displayArea.append("Saved: " + event + "\n");

            // 6. Clear fields
            clearFields();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,"An error occurred while saving the event.","File Error",JOptionPane.ERROR_MESSAGE);
                  
        }
    }

    private void loadEvents() {
        try {
            eventList.clear();
            displayArea.setText("");

            File file = new File("events.txt");

            // Check if the file exists and is not empty
            if (file.exists() && file.length() > 0) {
                FileReader fr = new FileReader(file);
                BufferedReader reader = new BufferedReader(fr);

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    
                    String name = parts[0];
                    LocalDate date = LocalDate.parse(parts[1]);  // Parsing LocalDate from file data
                    String category = parts[2];
                    String location = parts[3];

                    EventClass event = new EventClass(name, date, category, location);
                    eventList.add(event);
                    displayArea.append(event.toString() + "\n");
                }
                reader.close();
            } else {
                //displayArea.append("No events to load. The file is empty.\n");
                JOptionPane.showMessageDialog(null, "No events to load. The file is empty.\n");
            }

        } catch (Exception ex) {
            displayArea.append("Error loading events.\n");
            JOptionPane.showMessageDialog(null, "Error loading events: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchEvent() {
        String keyword = JOptionPane.showInputDialog(null, "Enter event name or category:");
        displayArea.setText("");
        boolean found = false;

        for (int i = 0; i < eventList.size(); i++) {
            EventClass event = eventList.get(i);
            if (event.getEventName().equalsIgnoreCase(keyword) ||
                event.getEventCategory().equalsIgnoreCase(keyword)) {
                displayArea.append("Found: " + event.toString() + "\n");
                found = true;
            }
        }
        
        if (!found) {
        JOptionPane.showMessageDialog(this, 
            "No event found with the name or category: " + keyword,
            "Search Result", 
            JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteEvent() {
        String nameToDelete = JOptionPane.showInputDialog(null, "Enter event name to delete:");
        boolean found = false;

        ArrayList<EventClass> updatedList = new ArrayList<>();
        
        for (int i = 0; i < eventList.size(); i++) {
            EventClass event = eventList.get(i);
            if (!event.getEventName().equalsIgnoreCase(nameToDelete)) {
                updatedList.add(event);
            } else {
                found = true;
            }
        }

        if (found) {
            eventList = updatedList;

            // Rewrite file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("events.txt"))) {
                
                for (int i = 0; i < eventList.size(); i++) {
                    EventClass event = eventList.get(i);
                    writer.write(event.getEventName() + "," + event.getEventDate().format(DateTimeFormatter.ISO_LOCAL_DATE) + "," + event.getEventCategory());  // Save date as ISO format
                    writer.newLine();
                }
                
            } catch (IOException ex) {
                displayArea.append("Error updating file.\n");
            }
            JOptionPane.showMessageDialog(null,"Event '" + nameToDelete + "' was successfully deleted.","Delete Successful",JOptionPane.INFORMATION_MESSAGE);
                
                displayArea.append("Deleted event: " + nameToDelete + "\n");
        } else {
            JOptionPane.showMessageDialog(null,"Event '" + nameToDelete + "' not found.","Delete Failed",JOptionPane.WARNING_MESSAGE);
             
        }
    }
    
    private void clearFields() {
        nameField.setText("");
        dateField.setText("");
        categoryField.setText("");
        displayArea.setText("");
        locationField.setText("");
    }
}