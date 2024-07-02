package hella;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;

public class TaskManagementGUI extends JFrame {

    private TaskSystem taskSystem;
    private JTextArea displayArea;

    public TaskManagementGUI() {
        taskSystem = new TaskSystem();
        initComponents();
    }

    private void initComponents() {
        setTitle("Task Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(3, 3, 35, 35));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(35, 35, 35, 35));

        JButton addButton = new JButton("Add Task");
        JButton addSubtaskButton = new JButton("Add Subtask");
        JButton displayPriorityButton = new JButton("Display Tasks by Priority");
        JButton viewButton = new JButton("View All Tasks");
        JButton deleteButton = new JButton("Delete Task");
        JButton deleteSubtaskButton = new JButton("Delete Subtask");
        JButton markButton = new JButton("Mark Completed");
        JButton markSubtaskButton = new JButton("Mark Subtask Completed");
        JButton searchButton = new JButton("Search Tasks");
        JButton exitButton = new JButton("Exit");

        buttonPanel.add(addButton);
        buttonPanel.add(addSubtaskButton);
        buttonPanel.add(displayPriorityButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(deleteSubtaskButton);
        buttonPanel.add(markButton);
        buttonPanel.add(markSubtaskButton);
        buttonPanel.add(searchButton); // Add search button to the panel
        buttonPanel.add(exitButton);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });

        addSubtaskButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addSubtask();
            }
        });

        displayPriorityButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayTasksByPriority();
            }
        });

        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayAllTasks();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteTask();
            }
        });

        deleteSubtaskButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteSubtask();
            }
        });

        markButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                markTaskAsCompleted();
            }
        });

        markSubtaskButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                markSubtaskAsCompleted();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchTasks(); // Handle search tasks action
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //System.exit(0);
            	setVisible(false);
            	new Admin().setVisible(true);
            }
        });
    }

    private void addTask() {
        JTextField titleField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField dueDateField = new JTextField();
        JTextField priorityField = new JTextField();
        Object[] message = {
                "Title:", titleField,
                "Description:", descriptionField,
                "Due Date (dd/MM/yyyy):", dueDateField,
                "Priority (1-3):", priorityField,
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Task", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String title = titleField.getText();
                String description = descriptionField.getText();
                Date dueDate = new SimpleDateFormat("dd/MM/yyyy").parse(dueDateField.getText());
                int priority = Integer.parseInt(priorityField.getText());

                taskSystem.createTask(title, description, dueDate, priority);
                displayArea.setText("Task added successfully!");
            } catch (ParseException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
            }
        }
    }

    private void addSubtask() {
        JTextField parentTitleField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField dueDateField = new JTextField();
        JTextField priorityField = new JTextField();
        Object[] message = {
                "Parent Task Title:", parentTitleField,
                "Subtask Title:", titleField,
                "Description:", descriptionField,
                "Due Date (dd/MM/yyyy):", dueDateField,
                "Priority (1-3):", priorityField,
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Subtask", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String parentTitle = parentTitleField.getText();
                String title = titleField.getText();
                String description = descriptionField.getText();
                Date dueDate = new SimpleDateFormat("dd/MM/yyyy").parse(dueDateField.getText());
                int priority = Integer.parseInt(priorityField.getText());

                taskSystem.addSubtask(parentTitle, title, description, dueDate, priority);
                displayArea.setText("Subtask added successfully!");
            } catch (ParseException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage());
            }
        }
    }

    private void displayTasksByPriority() {
        PriorityQueue<Task> tasksByPriority = taskSystem.getTasksByPriority();
        StringBuilder sb = new StringBuilder("Tasks by Priority:\n");
        while (!tasksByPriority.isEmpty()) {
            Task task = tasksByPriority.poll();
            sb.append(task.toString()).append("\n");
        }
        displayArea.setText(sb.toString());
    }

    private void displayAllTasks() {
        List<Task> tasks = taskSystem.getAllTasksFlat();
        StringBuilder sb = new StringBuilder("All Tasks:\n");
        for (Task task : tasks) {
            sb.append(task.toString()).append("\n");
        }
        displayArea.setText(sb.toString());
    }

    private void deleteTask() {
        String title = JOptionPane.showInputDialog(this, "Enter task title to delete:");
        if (title != null) {
            taskSystem.deleteTask(title);
            displayArea.setText("Task deleted successfully!");
        }
    }

    private void deleteSubtask() {
        JTextField parentTitleField = new JTextField();
        JTextField subtaskTitleField = new JTextField();
        Object[] message = {
                "Parent Task Title:", parentTitleField,
                "Subtask Title:", subtaskTitleField,
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Delete Subtask", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String parentTitle = parentTitleField.getText();
            String subtaskTitle = subtaskTitleField.getText();
            taskSystem.deleteSubtask(parentTitle, subtaskTitle);
            displayArea.setText("Subtask deleted successfully!");
        }
    }

    private void markTaskAsCompleted() {
        String title = JOptionPane.showInputDialog(this, "Enter task title to mark as completed:");
        if (title != null) {
            taskSystem.markTaskAsCompleted(title);
            displayArea.setText("Task marked as completed!");
        }
    }

    private void markSubtaskAsCompleted() {
        JTextField parentTitleField = new JTextField();
        JTextField subtaskTitleField = new JTextField();
        Object[] message = {
                "Parent Task Title:", parentTitleField,
                "Subtask Title:", subtaskTitleField,
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Mark Subtask Completed", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String parentTitle = parentTitleField.getText();
            String subtaskTitle = subtaskTitleField.getText();
            taskSystem.markSubtaskAsCompleted(parentTitle, subtaskTitle);
            displayArea.setText("Subtask marked as completed!");
        }
    }

    private void searchTasks() {
        String keyword = JOptionPane.showInputDialog(this, "Enter keyword to search tasks:");
        if (keyword != null) {
            List<Task> results = taskSystem.searchTasks(keyword);
            StringBuilder sb = new StringBuilder("Search Results:\n");
            for (Task task : results) {
                sb.append(task.toString()).append("\n");
            }
            displayArea.setText(sb.toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TaskManagementGUI().setVisible(true);
            }
        });
    }
}
