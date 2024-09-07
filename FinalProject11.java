package finalProject;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class FinalProject11 extends JFrame {
    private JTable courseTable; 
    private DefaultTableModel model; 
    private JLabel totalCreditsLabel; 
    private JTable timetable; 
    private DefaultTableModel timetableModel; 
    private int totalCredits = 0; 
    private final int MAX_CREDITS = 21; 
    private ArrayList<String[]> courseList; 

    public FinalProject11() {
        setTitle("Course Registration"); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setSize(1200, 800); 
        setLayout(new BorderLayout()); 

        String[] columnNames = {"Course Name", "Credits", "Time", "Cancel"};
        model = new DefaultTableModel(columnNames, 0); 
        courseTable = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; 
            }
        };
	    
	    courseTable.getColumn("Cancel").setCellRenderer(new ButtonRenderer()); 
	    courseTable.getColumn("Cancel").setCellEditor(new ButtonEditor(new JCheckBox(), courseTable, this));

        JScrollPane courseScrollPane = new JScrollPane(courseTable); 
        totalCreditsLabel = new JLabel("Total Credits: 0");
       
        String[] timetableColumnNames = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        timetableModel = new DefaultTableModel(timetableColumnNames, 9); 
        timetable = new JTable(timetableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        for (int i = 0; i < 9; i++) {
            timetableModel.setValueAt((9 + i) + ":00", i, 0);
        }
       
        timetable.setRowHeight(80); 
        timetable.getColumnModel().getColumn(0).setPreferredWidth(50);
       
        for (int i = 1; i < timetable.getColumnModel().getColumnCount(); i++) {
            timetable.getColumnModel().getColumn(i).setPreferredWidth(200);
        }
       
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        timetable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        
        JScrollPane timetableScrollPane = new JScrollPane(timetable);
        timetableScrollPane.setPreferredSize(new Dimension(800, 800));
        
        JButton listButton = new JButton("View Course List");
        listButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showCourseList(); 
            }
        });
       
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(courseScrollPane, BorderLayout.WEST);
        mainPanel.add(totalCreditsLabel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.WEST);
        add(timetableScrollPane, BorderLayout.CENTER);
        add(listButton, BorderLayout.NORTH);
   
        courseList = new ArrayList<>();
        initializeCourseList();

        setVisible(true); 
    }
    
    private void updateTimetable(String courseName, String courseTime) {
        String[] parts = courseTime.split(" "); 
        String days = parts[0]; 
        String[] times = parts[1].split("-"); 
        String startHour = times[0]; 
        String endHour = times[1]; 
        
        for (int i = Integer.parseInt(startHour.split(":")[0]) - 9; i < Integer.parseInt(endHour.split(":")[0]) - 9; i++) {
            if (days.contains("M")) timetableModel.setValueAt(courseName, i, 1);
            if (days.contains("Tu")) timetableModel.setValueAt(courseName, i, 2);
            if (days.contains("W")) timetableModel.setValueAt(courseName, i, 3);
            if (days.contains("Th")) timetableModel.setValueAt(courseName, i, 4);
            if (days.contains("F")) timetableModel.setValueAt(courseName, i, 5);
        }
    }
    
    private boolean isTimeConflict(String courseTime) {
        String[] parts = courseTime.split(" ");
        String days = parts[0];
        String[] times = parts[1].split("-");
        String startHour = times[0];
        String endHour = times[1];
       
        for (int i = Integer.parseInt(startHour.split(":")[0]) - 9; i < Integer.parseInt(endHour.split(":")[0]) - 9; i++) {
            if (days.contains("M") && timetableModel.getValueAt(i, 1) != null) return true;
            if (days.contains("Tu") && timetableModel.getValueAt(i, 2) != null) return true;
            if (days.contains("W") && timetableModel.getValueAt(i, 3) != null) return true;
            if (days.contains("Th") && timetableModel.getValueAt(i, 4) != null) return true;
            if (days.contains("F") && timetableModel.getValueAt(i, 5) != null) return true;
        }
        return false;
    }
    
    private void showCourseList() {
        JFrame courseListFrame = new JFrame("Course List"); 
        courseListFrame.setSize(600, 400); 
        courseListFrame.setLayout(new BorderLayout()); 
        
        String[] columnNames = {"Course Name", "Credits", "Time", "Register"};
        DefaultTableModel courseListModel = new DefaultTableModel(columnNames, 0);
        JTable courseListTable = new JTable(courseListModel) {
           
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; 
            }
        };
       
        for (String[] course : courseList) {
            courseListModel.addRow(new Object[]{course[0], course[1], course[2], "Register"});
        }
        
        courseListTable.getColumn("Register").setCellRenderer(new ButtonRenderer());
        courseListTable.getColumn("Register").setCellEditor(new ButtonEditor(new JCheckBox(), courseListTable, this));

        JScrollPane courseListScrollPane = new JScrollPane(courseListTable); 
        courseListFrame.add(courseListScrollPane, BorderLayout.CENTER); 

        courseListFrame.setVisible(true); 
    }
    
    private void initializeCourseList() {
        
        courseList.add(new String[]{"Mathematics", "3", "MWF 9:00-10:00"});
        courseList.add(new String[]{"Physics", "4", "TuTh 10:00-12:00"});
        courseList.add(new String[]{"Chemistry", "3", "MWF 11:00-12:00"});
        courseList.add(new String[]{"Biology", "4", "TuTh 13:00-15:00"});
        courseList.add(new String[]{"Computer Science", "3", "MWF 14:00-15:00"});
        courseList.add(new String[]{"History", "3", "MWF 15:00-16:00"});
        courseList.add(new String[]{"Art", "2", "TuTh 9:00-10:00"});
        courseList.add(new String[]{"Music", "2", "TuTh 11:00-12:00"});
        courseList.add(new String[]{"Economics", "3", "MWF 10:00-11:00"});
        courseList.add(new String[]{"Philosophy", "3", "TuTh 12:00-13:00"});
        courseList.add(new String[]{"Sociology", "3", "MWF 12:00-13:00"});
        courseList.add(new String[]{"Literature", "3", "MWF 13:00-14:00"});
        courseList.add(new String[]{"Political Science", "3", "TuTh 15:00-16:00"});
        courseList.add(new String[]{"Psychology", "3", "MWF 16:00-17:00"});
        courseList.add(new String[]{"Anthropology", "3", "TuTh 14:00-15:00"});
        courseList.add(new String[]{"Grammar", "2", "Th 14:00-15:00"});
       
        courseList.add(new String[]{"Physics Lab", "1", "MW 14:00-15:00"});
        courseList.add(new String[]{"Chemistry Lab", "1", "TuTh 14:00-15:00"});
        courseList.add(new String[]{"Advanced Mathematics", "3", "MWF 9:00-10:00"});
        courseList.add(new String[]{"Data Structures", "3", "MWF 10:00-11:00"});
        courseList.add(new String[]{"Algorithms", "3", "TuTh 11:00-13:00"});
        courseList.add(new String[]{"Operating Systems", "3", "MWF 13:00-14:00"});
        courseList.add(new String[]{"Databases", "3", "TuTh 15:00-17:00"});
        courseList.add(new String[]{"Networks", "3", "MWF 14:00-15:00"});
        courseList.add(new String[]{"Software Engineering", "3", "TuTh 9:00-11:00"});
        courseList.add(new String[]{"AI", "3", "MWF 11:00-12:00"});
        courseList.add(new String[]{"Machine Learning", "3", "TuTh 12:00-14:00"});
        courseList.add(new String[]{"Deep Learning", "3", "MWF 15:00-16:00"});
        courseList.add(new String[]{"Cyber Security", "3", "TuTh 16:00-18:00"});
        courseList.add(new String[]{"Cloud Computing", "3", "MWF 9:00-10:00"});
    }

    public static void main(String[] args) {
        new FinalProject11(); 
    }

   
    class ButtonRenderer extends JButton implements TableCellRenderer {	
        public ButtonRenderer() {
            setOpaque(true); 
        }

       
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString()); 
            return this; 
        }
    }
    

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button; 
        private String label; 
        private boolean isPushed; 
        private JTable courseListTable; 
        private FinalProject11 mainFrame; 

       
        public ButtonEditor(JCheckBox checkBox, JTable courseListTable, FinalProject11 mainFrame) {
            super(checkBox);
           
            this.courseListTable = courseListTable;
            this.mainFrame = mainFrame;
            button = new JButton(); 
            button.setOpaque(true); 
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped(); 
                }
            });
        }

       
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) { 
            label = (value == null) ? "" : value.toString(); 
            button.setText(label); 
            isPushed = true; 
            return button; 
        }

       
        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int row = courseListTable.getSelectedRow(); 
                String courseName = (String) courseListTable.getValueAt(row, 0); 
                int credits = Integer.parseInt(courseListTable.getValueAt(row, 1).toString()); 
             
                String courseTime = (String) courseListTable.getValueAt(row, 2); 

                if (label.equals("Register")) { 
                    if (mainFrame.totalCredits + credits > MAX_CREDITS) { 
                        JOptionPane.showMessageDialog(null, "Maximum allowed credits are 21. You cannot add more courses.");
                    } else if (mainFrame.isTimeConflict(courseTime)) { 
                        JOptionPane.showMessageDialog(null, "The selected course time conflicts with an existing course.");
                    } else {
                        
                        SwingUtilities.invokeLater(() -> {
                            mainFrame.model.addRow(new Object[]{courseName, credits, courseTime, "X"}); 
                            mainFrame.totalCredits += credits;
                            mainFrame.totalCreditsLabel.setText("Total Credits: " + mainFrame.totalCredits);
                            mainFrame.updateTimetable(courseName, courseTime); 
                        });
                    }
                } else if (label.equals("X")) { 
                    int selectedRow = mainFrame.courseTable.getSelectedRow(); 
                    if (selectedRow >= 0) {
                        String removedCourseTime = (String) mainFrame.courseTable.getValueAt(selectedRow, 2); 
                        int selectedCredits = Integer.parseInt(mainFrame.courseTable.getValueAt(selectedRow, 1).toString()); 
                        
                        SwingUtilities.invokeLater(() -> {
                            mainFrame.totalCredits -= selectedCredits; 
                            mainFrame.totalCreditsLabel.setText("Total Credits: " + mainFrame.totalCredits);
                            mainFrame.model.removeRow(selectedRow); 
                            mainFrame.removeCourseFromTimetable(removedCourseTime); 
                        });
                    }
                }
            }
            isPushed = false; 
            return label; 
        }
        
        @Override
        public boolean stopCellEditing() {
            isPushed = false; 
            return super.stopCellEditing(); 
        }
        
        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped(); 
        }
    }

   
    private void removeCourseFromTimetable(String courseTime) {
        String[] parts = courseTime.split(" ");
        String days = parts[0];
        String[] times = parts[1].split("-");
        String startHour = times[0];
        String endHour = times[1];

        
        for (int i = Integer.parseInt(startHour.split(":")[0]) - 9; i < Integer.parseInt(endHour.split(":")[0]) - 9; i++) {
            if (days.contains("M")) timetableModel.setValueAt(null, i, 1);
            if (days.contains("Tu")) timetableModel.setValueAt(null, i, 2);
            if (days.contains("W")) timetableModel.setValueAt(null, i, 3);
            if (days.contains("Th")) timetableModel.setValueAt(null, i, 4);
            if (days.contains("F")) timetableModel.setValueAt(null, i, 5);
        }
    }
}
