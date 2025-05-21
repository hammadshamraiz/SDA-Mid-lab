import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

interface Observer {
    void update(String message, Color color);
}

interface DangerNotifier {
    void addObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers(String message, Color color);
}

class NotificationManager implements DangerNotifier {
    private final java.util.List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer o) {
        observers.add(o);
    }

    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    public void notifyObservers(String message, Color color) {
        for (Observer o : observers) {
            o.update(message, color);
        }
    }

    public void notifyDanger() {
        notifyObservers("🚨 Motion Detected! Alert sent to stakeholders.", new Color(255, 102, 102));
    }
}

class Parent implements Observer {
    private JTextPane output;

    public Parent(JTextPane output) {
        this.output = output;
    }

    public void update(String message, Color color) {
        appendMessage("👨‍👩‍👧 Parent ➜ " + message, color);
    }

    private void appendMessage(String msg, Color color) {
        StyledDocument doc = output.getStyledDocument();
        Style style = output.addStyle("Color Style", null);
        StyleConstants.setForeground(style, color);
        try {
            doc.insertString(doc.getLength(), msg + "\n", style);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

class SecurityStaff implements Observer {
    private JTextPane output;

    public SecurityStaff(JTextPane output) {
        this.output = output;
    }

    public void update(String message, Color color) {
        appendMessage("🛡️ Security ➜ " + message, color);
    }

    private void appendMessage(String msg, Color color) {
        StyledDocument doc = output.getStyledDocument();
        Style style = output.addStyle("Color Style", null);
        StyleConstants.setForeground(style, color);
        try {
            doc.insertString(doc.getLength(), msg + "\n", style);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

class DetectionEngine {
    private final NotificationManager notificationManager;

    public DetectionEngine(NotificationManager manager) {
        this.notificationManager = manager;
    }

    public void analyzeData(String data) {
        if (data.equalsIgnoreCase("motion")) {
            notificationManager.notifyDanger();
        }
    }
}

class Server {
    private final DetectionEngine engine;

    public Server(DetectionEngine engine) {
        this.engine = engine;
    }

    public void receiveData(String data) {
        engine.analyzeData(data);
    }
}

public class KidsMonitoringAppGUI extends JFrame {
    private JButton sendButton;
    private JTextPane outputPane;

    public KidsMonitoringAppGUI() {
        setTitle("👶 Kids Monitoring System");
        setSize(700, 530);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        JPanel headerPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(0, 153, 255);
                Color color2 = new Color(0, 102, 204);
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(700, 60));
        JLabel title = new JLabel("📡 Kids Danger Monitoring System");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerPanel.add(title);
        add(headerPanel, BorderLayout.NORTH);

        outputPane = new JTextPane();
        outputPane.setEditable(false);
        outputPane.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(outputPane);
        scrollPane.setBorder(BorderFactory.createTitledBorder("📋 Real-time Alerts"));
        scrollPane.setBackground(new Color(250, 250, 250));
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 250, 255));
        sendButton = new JButton("🚀 Send Motion Alert");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sendButton.setBackground(new Color(0, 153, 255));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setPreferredSize(new Dimension(250, 45));
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendButton.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2, true));
        buttonPanel.add(sendButton);
        add(buttonPanel, BorderLayout.SOUTH);

        NotificationManager manager = new NotificationManager();
        DetectionEngine engine = new DetectionEngine(manager);
        Server server = new Server(engine);

        Parent parent = new Parent(outputPane);
        SecurityStaff staff = new SecurityStaff(outputPane);

        manager.addObserver(parent);
        manager.addObserver(staff);

        sendButton.addActionListener(e -> {
            appendSystem("📶 Sensor ➜ Motion detected! Sending data...", new Color(100, 100, 100));
            server.receiveData("motion");
        });
    }

    private void appendSystem(String msg, Color color) {
        StyledDocument doc = outputPane.getStyledDocument();
        Style style = outputPane.addStyle("Sys", null);
        StyleConstants.setForeground(style, color);
        StyleConstants.setItalic(style, true);
        try {
            doc.insertString(doc.getLength(), msg + "\n", style);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KidsMonitoringAppGUI().setVisible(true));
    }
}