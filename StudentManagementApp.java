import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

// Student class to represent individual students
class Student implements Serializable {
    private String name;
    private int rollNumber;
    private String grade;

    public Student(String name, int rollNumber, String grade) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
    }

    public String getName() { return name; }
    public int getRollNumber() { return rollNumber; }
    public String getGrade() { return grade; }

    public void setName(String name) { this.name = name; }
    public void setGrade(String grade) { this.grade = grade; }

    @Override
    public String toString() {
        return "Roll No: " + rollNumber + ", Name: " + name + ", Grade: " + grade;
    }
}

// Student Management System class
class StudentManagementSystem {
    private List<Student> students;
    private static final String FILE_NAME = "students.txt";

    public StudentManagementSystem() {
        students = new ArrayList<>();
        loadStudents();
    }

    // Add a student
    public void addStudent(Student student) {
        students.add(student);
        saveStudents();
    }

    // Remove a student by roll number
    public boolean removeStudent(int rollNumber) {
        for (Student student : students) {
            if (student.getRollNumber() == rollNumber) {
                students.remove(student);
                saveStudents();
                return true;
            }
        }
        return false;
    }

    // Search for a student by roll number
    public Student searchStudent(int rollNumber) {
        for (Student student : students) {
            if (student.getRollNumber() == rollNumber) {
                return student;
            }
        }
        return null;
    }

    // Display all students
    public List<Student> getAllStudents() {
        return students;
    }

    // Save students to file
    private void saveStudents() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(students);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load students from file
    private void loadStudents() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            students = (List<Student>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            students = new ArrayList<>();
        }
    }
}

// GUI Class for Student Management System
class StudentManagementGUI extends JFrame {
    private StudentManagementSystem sms;
    private JTextField nameField, rollNumberField, gradeField;
    private JTable studentTable;
    private DefaultTableModel tableModel;

    public StudentManagementGUI() {
        sms = new StudentManagementSystem();
        setTitle("Student Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 10, 10));

        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Roll Number:"));
        rollNumberField = new JTextField();
        panel.add(rollNumberField);

        panel.add(new JLabel("Grade:"));
        gradeField = new JTextField();
        panel.add(gradeField);

        JButton addButton = new JButton("Add Student");
        addButton.addActionListener(e -> addStudent());
        panel.add(addButton);

        JButton searchButton = new JButton("Search Student");
        searchButton.addActionListener(e -> searchStudent());
        panel.add(searchButton);

        JButton removeButton = new JButton("Remove Student");
        removeButton.addActionListener(e -> removeStudent());
        panel.add(removeButton);

        JButton viewAllButton = new JButton("View All Students");
viewAllButton.addActionListener(e -> displayAllStudents());
panel.add(viewAllButton);


        // Table for displaying students
        tableModel = new DefaultTableModel(new String[]{"Roll Number", "Name", "Grade"}, 0);
        studentTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(studentTable);

        add(panel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    // Add student to the system
    // Add student to the system
    private void addStudent() {
        String name = nameField.getText().trim();
        String rollText = rollNumberField.getText().trim();
        String grade = gradeField.getText().trim();
    
        if (name.isEmpty() || rollText.isEmpty() || grade.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        try {
            int rollNumber = Integer.parseInt(rollText);
            Student newStudent = new Student(name, rollNumber, grade);
            sms.addStudent(newStudent);
            JOptionPane.showMessageDialog(this, "Student added successfully.");
            clearFields();
            // Don't clear the table, instead add the new student directly to the table
            tableModel.addRow(new Object[]{newStudent.getRollNumber(), newStudent.getName(), newStudent.getGrade()});
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Roll number must be a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    // Search student by roll number
    private void searchStudent() {
        String rollText = rollNumberField.getText().trim();

        if (rollText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a roll number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int rollNumber = Integer.parseInt(rollText);
            Student student = sms.searchStudent(rollNumber);
            if (student != null) {
                JOptionPane.showMessageDialog(this, "Student Found:\n" + student);
            } else {
                JOptionPane.showMessageDialog(this, "Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid roll number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Remove student by roll number
    private void removeStudent() {
        String rollText = rollNumberField.getText().trim();

        if (rollText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a roll number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int rollNumber = Integer.parseInt(rollText);
            if (sms.removeStudent(rollNumber)) {
                JOptionPane.showMessageDialog(this, "Student removed successfully.");
                clearFields();
                displayAllStudents();  // Refresh table after removing student
            } else {
                JOptionPane.showMessageDialog(this, "Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid roll number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Display all students in table
    private void displayAllStudents() {
        List<Student> students = sms.getAllStudents();
        tableModel.setRowCount(0);  // Clear the table before updating
        for (Student student : students) {
            tableModel.addRow(new Object[]{student.getRollNumber(), student.getName(), student.getGrade()});
        }
    }
    

    // Clear input fields
    private void clearFields() {
        nameField.setText("");
        rollNumberField.setText("");
        gradeField.setText("");
    }
}

// Main class to run the GUI
public class StudentManagementApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentManagementGUI());
    }
}
