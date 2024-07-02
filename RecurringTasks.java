package hella;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.mail.*;
import javax.mail.internet.*;
//import javax.mail.internet.*;


class RecurringTask {
    String name;
    int totalDays;
    int hoursPerDay;
    LocalDate startDate;

    RecurringTask(String name, int totalDays, int hoursPerDay) {
        this.name = name;
        this.totalDays = totalDays;
        this.hoursPerDay = hoursPerDay;
        this.startDate = LocalDate.now();
    }

    int daysCompleted() {
        return (int) ChronoUnit.DAYS.between(startDate, LocalDate.now());
    }

    int daysRemaining() {
        return totalDays - daysCompleted();
    }

    boolean isCompleted() {
        return daysCompleted() >= totalDays;
    }

    @Override
    public String toString() {
        return (isCompleted() ? "[Completed] Task: " : "[Pending] Task: ") + name + "  \n   " + totalDays + " total days\n   " + hoursPerDay + " hours/day\n   " + daysCompleted() + " days completed\n   " + daysRemaining() + " days remaining";
    }
}

public class RecurringTasks extends JFrame {
    static TreeMap<Integer, RecurringTask> recurringTasks = new TreeMap<>();
    private static int recurringTaskCounter = 0;
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static LocalTime notificationTime = LocalTime.of(9, 0); // default notification time is 9 AM

    private JTextArea recurringTaskArea;
    private static String email;
    public RecurringTasks() {
        setTitle("Task Management System");
        setSize(800, 600);  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        recurringTaskArea = new JTextArea();
        recurringTaskArea.setEditable(false);
        recurringTaskArea.setFont(new Font("Times New Roman", Font.BOLD, 16));  

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 35, 35)); 

        JButton viewRecurringTasksButton = new JButton("View Recurring Tasks");
        viewRecurringTasksButton.addActionListener(e -> viewRecurringTasks());

        JButton removeRecurringTaskButton = new JButton("Remove Recurring Task");
        removeRecurringTaskButton.addActionListener(e -> removeRecurringTask());

        JButton changeNotificationTimeButton = new JButton("Change Notification Time");
        changeNotificationTimeButton.addActionListener(e -> changeNotificationTime());

        JButton goBackButton = new JButton("Main Menu");
        goBackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new Admin().setVisible(true);
            }
        });

        buttonPanel.add(viewRecurringTasksButton);
        buttonPanel.add(removeRecurringTaskButton);
        buttonPanel.add(changeNotificationTimeButton);
        buttonPanel.add(goBackButton);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 2, 10, 10));  // 3 rows, 2 columns, 10px padding

        formPanel.add(new JLabel("               Recurring Task Name:"));
        JTextField taskNameField = new JTextField();
        formPanel.add(taskNameField);

        formPanel.add(new JLabel("               No of Days:"));
        JTextField daysField = new JTextField();
        formPanel.add(daysField);

        formPanel.add(new JLabel("               No of Hours per Day:"));
        JTextField hoursField = new JTextField();
        formPanel.add(hoursField);

        JPanel saveButtonPanel = new JPanel(new BorderLayout());
        JButton saveTaskButton = new JButton("Save Task");

        saveButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 175, 20, 175));
        saveButtonPanel.add(saveTaskButton, BorderLayout.CENTER);

        saveTaskButton.addActionListener(e -> {
            String taskName = taskNameField.getText();
            try {
                int totalDays = Integer.parseInt(daysField.getText());
                int hoursPerDay = Integer.parseInt(hoursField.getText());
                if (totalDays > 0 && hoursPerDay > 0) {
                    recurringTasks.put(++recurringTaskCounter, new RecurringTask(taskName, totalDays, hoursPerDay));
                    refreshRecurringTaskList();
                    taskNameField.setText("");
                    daysField.setText("");
                    hoursField.setText("");
                }
            } catch (NumberFormatException f) {
                JOptionPane.showMessageDialog(null, "Please enter numbers in the days and hours fields.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                taskNameField.setText("");
                daysField.setText("");
                hoursField.setText("");
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(saveButtonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.NORTH);
        add(new JScrollPane(recurringTaskArea), BorderLayout.CENTER);

        scheduler.scheduleAtFixedRate(() -> {
			try {
				checkNotificationTime();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}, 0, 1, TimeUnit.MINUTES);

        setVisible(true);
        refreshRecurringTaskList();
    }

    private void removeRecurringTask() {
        String taskNumberStr = JOptionPane.showInputDialog(this, "Enter recurring task number to remove:");
        if (taskNumberStr != null && !taskNumberStr.trim().isEmpty()) {
            int taskIndex = Integer.parseInt(taskNumberStr);
            if (recurringTasks.containsKey(taskIndex)) {
                recurringTasks.remove(taskIndex);
                refreshRecurringTaskList();
            }
        }
    }

    private void viewRecurringTasks() {
        if (recurringTasks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Recurring tasks entered!", "Recurring Tasks", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<Integer, RecurringTask> entry : recurringTasks.entrySet()) {
                sb.append(entry.getKey()).append(". ").append(entry.getValue()).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "Recurring Tasks", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void changeNotificationTime() {
        int hour = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter notification hour (0-23):"));
        int minute = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter notification minute (0-59):"));
        if (hour >= 0 && hour < 24 && minute >= 0 && minute < 60) {
            notificationTime = LocalTime.of(hour, minute);
            JOptionPane.showMessageDialog(this, "Notification time changed to " + notificationTime);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid time entered.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        email=JOptionPane.showInputDialog(this, "Enter your mail id for Mail Notification");
    }

    private static void checkNotificationTime() throws Exception {
        LocalTime currentTime = LocalTime.now();
        if (currentTime.getHour() == notificationTime.getHour() && currentTime.getMinute() == notificationTime.getMinute()) {
            sendReminder();
        }
    }

    private static void sendReminder() throws Exception {
        StringBuilder sb = new StringBuilder("Reminder: It's " + notificationTime + "! Here are your pending recurring tasks:\n");
        for (Map.Entry<Integer, RecurringTask> entry : recurringTasks.entrySet()) {
            RecurringTask recurringTask = entry.getValue();
            if (!recurringTask.isCompleted()) {
                sb.append(entry.getKey()).append(". ").append(recurringTask).append("\n");
            }
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Task Reminder", JOptionPane.INFORMATION_MESSAGE);
        sendEmail(email, sb.toString());
    }

    private static void sendEmail(String recipient, String content)throws Exception {
    	Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true); // Enable STARTTLS
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("sumeetjha932@gmail.com", "jypvemfngnlvghxj");
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("sumeetjha932@gmail.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject("Task Pending");

        MimeMultipart multipart = new MimeMultipart();

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(content, "text/html");
        multipart.addBodyPart(messageBodyPart);

        message.setContent(multipart);

        Transport.send(message);
        }


    private void refreshRecurringTaskList() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, RecurringTask> entry : recurringTasks.entrySet()) {
            sb.append(entry.getKey()).append(". ").append(entry.getValue()).append("\n");
        }
        recurringTaskArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RecurringTasks::new);
    }
}
