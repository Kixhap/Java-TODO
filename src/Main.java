import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

public class Main {
    private static JFrame frame;
    private static JList<Task> taskList;
    private static DefaultListModel<Task> listModel;

    private static JTextField nameField;
    private static JTextArea descArea;
    private static JCheckBox doneCheck;

    public static void main(String[] args) {

        try (var conn = Database.getConnection();
             var stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS tasks (id INTEGER PRIMARY KEY, name TEXT, description TEXT, isDone INTEGER)");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Font uiFont = new Font("SansSerif", Font.PLAIN, 16);

        UIManager.put("Label.font", uiFont);
        UIManager.put("Button.font", uiFont);
        UIManager.put("TextField.font", uiFont);
        UIManager.put("TextArea.font", uiFont);
        UIManager.put("CheckBox.font", uiFont);
        UIManager.put("List.font", uiFont);
        UIManager.put("TitledBorder.font", uiFont);

        SwingUtilities.invokeLater(Main::initUI);
    }

    private static void initUI() {
        frame = new JFrame("To-Do List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLayout(new BorderLayout(10, 10));

        // Lewa strona - lista zadań
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Task task) {
                    setText(task.getName());
                }
                return this;
            }
        });
        taskList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Task selectedTask = taskList.getSelectedValue();
                showTaskDetails(selectedTask);
            }
        });

        JScrollPane listScroll = new JScrollPane(taskList);
        listScroll.setPreferredSize(new Dimension(250, 0));
        frame.add(listScroll, BorderLayout.WEST);

        // Prawa strona - szczegóły zadania i przyciski
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout(10, 10));

        // Panel z detalami zadania (góra)
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        detailsPanel.add(new JLabel("Nazwa:"));
        nameField = new JTextField();
        detailsPanel.add(nameField);

        detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        detailsPanel.add(new JLabel("Opis:"));
        descArea = new JTextArea(15, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descArea);
        detailsPanel.add(descScroll);

        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        doneCheck = new JCheckBox("Zrobione");
        detailsPanel.add(doneCheck);

        rightPanel.add(detailsPanel, BorderLayout.CENTER);

        // Panel z przyciskami (dół)
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        Icon saveIcon = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/saveIcon.png")));
        JButton saveButton = new JButton(saveIcon);
        saveButton.setText("Save");
        saveButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        saveButton.setHorizontalTextPosition(SwingConstants.CENTER);
        saveButton.setBorderPainted(false);
        saveButton.setContentAreaFilled(false);
        saveButton.addActionListener(_ -> saveTaskChanges());
        buttonsPanel.add(saveButton);

        Icon deleteIcon = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/deleteIcon.png")));
        JButton deleteButton = new JButton(deleteIcon);
        deleteButton.setText("Delete");
        deleteButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        deleteButton.setHorizontalTextPosition(SwingConstants.CENTER);
        deleteButton.setBorderPainted(false);
        deleteButton.setContentAreaFilled(false);
        deleteButton.addActionListener(_ -> deleteSelectedTask());
        buttonsPanel.add(deleteButton);

        Icon addIcon = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/addIcon.png")));
        JButton addButton = new JButton(addIcon);
        addButton.setText("Add");
        addButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        addButton.setHorizontalTextPosition(SwingConstants.CENTER);
        addButton.setBorderPainted(false);
        addButton.setContentAreaFilled(false);
        addButton.addActionListener(_ -> addNewTask());
        buttonsPanel.add(addButton);

        Icon quitIcon = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/exitIcon.png")));
        JButton quitButton = new JButton(quitIcon);
        quitButton.setText("Quit");
        quitButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        quitButton.setHorizontalTextPosition(SwingConstants.CENTER);
        quitButton.setBorderPainted(false);
        quitButton.setContentAreaFilled(false);
        quitButton.addActionListener(_ -> quit());
        buttonsPanel.add(quitButton);

        rightPanel.add(buttonsPanel, BorderLayout.SOUTH);

        frame.add(rightPanel, BorderLayout.CENTER);

        refreshTaskList();

        frame.setVisible(true);
    }

    private static void refreshTaskList() {
        List<Task> tasks = TaskDAO.getAllTasks();
        listModel.clear();
        for (Task t : tasks) {
            listModel.addElement(t);
        }
        clearTaskDetails();
    }

    private static void showTaskDetails(Task task) {
        if (task == null) {
            clearTaskDetails();
            return;
        }
        nameField.setText(task.getName());
        descArea.setText(task.getDescription());
        doneCheck.setSelected(task.isDone());
    }

    private static void clearTaskDetails() {
        nameField.setText("");
        descArea.setText("");
        doneCheck.setSelected(false);
        taskList.clearSelection();
    }

    private static void saveTaskChanges() {
        Task selectedTask = taskList.getSelectedValue();
        if (selectedTask == null) return;

        selectedTask.setName(nameField.getText());
        selectedTask.setDescription(descArea.getText());
        selectedTask.setDone(doneCheck.isSelected());

        TaskDAO.updateTask(selectedTask);
        refreshTaskList();

        for (int i = 0; i < listModel.size(); i++) {
            if (listModel.get(i).getId() == selectedTask.getId()) {
                taskList.setSelectedIndex(i);
                break;
            }
        }
    }

    private static void deleteSelectedTask() {
        Task selectedTask = taskList.getSelectedValue();
        if (selectedTask == null) return;

        TaskDAO.removeTask(selectedTask);
        refreshTaskList();
    }

    private static void addNewTask() {
        Task newTask = new Task(0, "Nowe zadanie", "", false);
        TaskDAO.addTask(newTask);
        refreshTaskList();

        List<Task> allTasks = TaskDAO.getAllTasks();
        Task lastTask = allTasks.getLast();
        for (int i = 0; i < listModel.size(); i++) {
            if (listModel.get(i).getId() == lastTask.getId()) {
                taskList.setSelectedIndex(i);
                break;
            }
        }
    }

    private static void quit() {
        frame.dispose();
    }
}
