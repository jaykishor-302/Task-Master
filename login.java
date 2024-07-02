package hella;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class login extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField username_field;
    private JPasswordField pass_field;
    public int count = 0;
    private HashMap<String, String> userCredentials;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    login frame = new login();
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public login() {
        // Initialize user credentials
        userCredentials = new HashMap<>();
        userCredentials.put("Admin", "admin");
        userCredentials.put("User1", "password1"); // Add more users as needed
        userCredentials.put("User2", "password2");

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 640, 412);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("New label");
        lblNewLabel.setIcon(new ImageIcon("D:\\dsa_project\\login_image.jpg"));
        lblNewLabel.setBounds(10, 23, 285, 321);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("LOGIN");
        lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_1.setForeground(new Color(0, 0, 255));
        lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 22));
        lblNewLabel_1.setBounds(284, 10, 285, 45);
        contentPane.add(lblNewLabel_1);

        JButton admin_btn = new JButton("Admin");
        admin_btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                count = 1;
                admin_btn.setForeground(Color.RED);
            }
        });
        admin_btn.setForeground(Color.BLUE);
        admin_btn.setFont(new Font("Times New Roman", Font.BOLD, 18));
        admin_btn.setBackground(Color.WHITE);
        admin_btn.setBounds(390, 65, 101, 44);
        contentPane.add(admin_btn);

        JLabel user_label = new JLabel("Username");
        user_label.setFont(new Font("Times New Roman", Font.BOLD, 16));
        user_label.setBounds(318, 119, 111, 27);
        contentPane.add(user_label);

        username_field = new JTextField();
        username_field.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        username_field.setColumns(10);
        username_field.setBounds(318, 156, 237, 35);
        contentPane.add(username_field);

        JLabel pass_label = new JLabel("Password");
        pass_label.setFont(new Font("Times New Roman", Font.BOLD, 16));
        pass_label.setBounds(318, 207, 111, 27);
        contentPane.add(pass_label);

        pass_field = new JPasswordField();
        pass_field.setBounds(318, 234, 237, 35);
        contentPane.add(pass_field);

        JButton login_btn = new JButton("Login");
        login_btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = username_field.getText();
                String password = new String(pass_field.getPassword());

                if (userCredentials.containsKey(username) && userCredentials.get(username).equals(password)) {
                    JOptionPane.showMessageDialog(null, "Login successful");
                    setVisible(false);
                    new Admin().setVisible(true); // Assuming Admin class exists and is another JFrame
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password");
                    username_field.setText("");
                    pass_field.setText("");
                }
            }
        });
        login_btn.setForeground(Color.BLUE);
        login_btn.setFont(new Font("Times New Roman", Font.BOLD, 18));
        login_btn.setBackground(Color.WHITE);
        login_btn.setBounds(390, 288, 103, 45);
        contentPane.add(login_btn);
    }
}