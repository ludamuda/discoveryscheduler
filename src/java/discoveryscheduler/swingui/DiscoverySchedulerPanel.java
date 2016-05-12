package discoveryscheduler.swingui;

import static org.optaplanner.examples.common.swingui.timetable.TimeTablePanel.HeaderColumnKey.HEADER_COLUMN;
import static org.optaplanner.examples.common.swingui.timetable.TimeTablePanel.HeaderColumnKey.HEADER_COLUMN_GROUP1;
import static org.optaplanner.examples.common.swingui.timetable.TimeTablePanel.HeaderRowKey.HEADER_ROW;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.examples.common.swingui.SolutionPanel;
import org.optaplanner.examples.common.swingui.TangoColorFactory;
import org.optaplanner.examples.common.swingui.components.LabeledComboBoxRenderer;
import org.optaplanner.examples.common.swingui.timetable.TimeTablePanel;


import discoveryscheduler.domain.*;


public class DiscoverySchedulerPanel extends SolutionPanel {

    public static final String LOGO_PATH = "/discoveryscheduler/swingui/logo.png";

    private final TimeTablePanel<Group, Timestamp> groupsPanel;
    private final TimeTablePanel<Instructor, Timestamp> instructorsPanel;

    public DiscoverySchedulerPanel() {
        setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        groupsPanel = new TimeTablePanel<Group, Timestamp>();
        tabbedPane.add("Groups", new JScrollPane(groupsPanel));
        instructorsPanel = new TimeTablePanel<Instructor, Timestamp>();
        tabbedPane.add("Instructors", new JScrollPane(instructorsPanel));
        add(tabbedPane, BorderLayout.CENTER);
        setPreferredSize(PREFERRED_SCROLLABLE_VIEWPORT_SIZE);
    }

    @Override
    public boolean isWrapInScrollPane() {
        return false;
    }

    @Override
    public boolean isRefreshScreenDuringSolving() {
        return true;
    }

    private Week getWeek() {
        return (Week) solutionBusiness.getSolution();
    }

    public void resetPanel(Solution solution) {
        groupsPanel.reset();
        instructorsPanel.reset();
        Week week = (Week) solution;
        defineGrid(week);
        fillCells(week);
        repaint(); // Hack to force a repaint of TimeTableLayout during "refresh screen while solving"
    }

    private void defineGrid(Week week) {
        JButton footprint = new JButton("LinLetGre1-0");
        footprint.setMargin(new Insets(0, 0, 0, 0));
        int footprintWidth = footprint.getPreferredSize().width;

        groupsPanel.defineColumnHeaderByKey(HEADER_COLUMN_GROUP1); // Day header
        groupsPanel.defineColumnHeaderByKey(HEADER_COLUMN); // Hour header
        for (Group group : week.getGroupList()) {
            groupsPanel.defineColumnHeader(group, footprintWidth);
        }
        groupsPanel.defineColumnHeader(null, footprintWidth); // Unassigned
        
        instructorsPanel.defineColumnHeaderByKey(HEADER_COLUMN_GROUP1); // Day header
        instructorsPanel.defineColumnHeaderByKey(HEADER_COLUMN); // Hour header
        for (Instructor instructor : week.getInstructorList()) {
        	instructorsPanel.defineColumnHeader(instructor, footprintWidth);
        }
        instructorsPanel.defineColumnHeader(null, footprintWidth); // Unassigned

        groupsPanel.defineRowHeaderByKey(HEADER_ROW); // Group header
        instructorsPanel.defineRowHeaderByKey(HEADER_ROW); // Group header
        for (Timestamp timestamp : week.getTimestampList()) {
            groupsPanel.defineRowHeader(timestamp);
            instructorsPanel.defineRowHeader(timestamp);
        }
        groupsPanel.defineRowHeader(null); // Unassigned period
        instructorsPanel.defineRowHeader(null); // Unassigned period
    }

    private void fillCells(Week week) {
        groupsPanel.addCornerHeader(HEADER_COLUMN_GROUP1, HEADER_ROW, createTableHeader(new JLabel("Day")));
        groupsPanel.addCornerHeader(HEADER_COLUMN, HEADER_ROW, createTableHeader(new JLabel("Hour")));
        fillGroupCells(week);
        instructorsPanel.addCornerHeader(HEADER_COLUMN_GROUP1, HEADER_ROW, createTableHeader(new JLabel("Day")));
        instructorsPanel.addCornerHeader(HEADER_COLUMN, HEADER_ROW, createTableHeader(new JLabel("Hour")));
        fillInstructorCells(week);
        fillTimestampCells(week);
        fillTaskCells(week);

    }

    private void fillGroupCells(Week week) {
        for (Group group : week.getGroupList()) {
            groupsPanel.addColumnHeader(group, HEADER_ROW,
                    createTableHeader(new JLabel("Group " + group.getLabel(), SwingConstants.CENTER)));
        }
        groupsPanel.addColumnHeader(null, HEADER_ROW,
                createTableHeader(new JLabel("Unassigned", SwingConstants.CENTER)));
    }
    
    private void fillInstructorCells(Week week) {
        for (Instructor instructor : week.getInstructorList()) {
        	instructorsPanel.addColumnHeader(instructor, HEADER_ROW,
                    createTableHeader(new JLabel(instructor.getLabel(), SwingConstants.CENTER)));
        }
        instructorsPanel.addColumnHeader(null, HEADER_ROW,
                createTableHeader(new JLabel("Unassigned", SwingConstants.CENTER)));
    }


    private void fillTimestampCells(Week week) {
        for (Day day : week.getDayList()) {
        	Timestamp dayStartTimestamp = day.getTimestampList().get(0);
            Timestamp dayEndTimestamp = day.getTimestampList().get(day.getTimestampList().size() - 1);
            groupsPanel.addRowHeader(HEADER_COLUMN_GROUP1, dayStartTimestamp, HEADER_COLUMN_GROUP1, dayEndTimestamp,
                    createTableHeader(new JLabel(day.getLabel())));
            instructorsPanel.addRowHeader(HEADER_COLUMN_GROUP1, dayStartTimestamp, HEADER_COLUMN_GROUP1, dayEndTimestamp,
                    createTableHeader(new JLabel(day.getLabel())));
            for (Timestamp timestamp : day.getTimestampList()) {
                groupsPanel.addRowHeader(HEADER_COLUMN, timestamp,
                        createTableHeader(new JLabel(timestamp.getHour().getLabel())));
                instructorsPanel.addRowHeader(HEADER_COLUMN, timestamp,
                        createTableHeader(new JLabel(timestamp.getHour().getLabel())));
            }
        }
        groupsPanel.addRowHeader(HEADER_COLUMN_GROUP1, null, HEADER_COLUMN_GROUP1, null,
                createTableHeader(new JLabel("Unassigned")));
        instructorsPanel.addRowHeader(HEADER_COLUMN_GROUP1, null, HEADER_COLUMN_GROUP1, null,
                createTableHeader(new JLabel("Unassigned")));
    }
    
    private void fillTaskCells(Week week) {
        TangoColorFactory tangoColorFactory = new TangoColorFactory();
        for (Task task : week.getTaskList()) {
            Color taskColor = tangoColorFactory.pickColor(task.getActivity());
            if(task.getStart() == null){
            	groupsPanel.addCell(task.getGroup(), task.getStart(), createButton(task, taskColor));
                instructorsPanel.addCell(task.getInstructor(), task.getStart(), createButton(task, taskColor));
            } else {
	            groupsPanel.addCell(task.getGroup(), task.getStart(), task.getGroup(), 
	            		task.getEnd(),
	                    createButton(task, taskColor));
	            instructorsPanel.addCell(task.getInstructor(), task.getStart(), task.getInstructor(), 
	            		task.getEnd(),
	                    createButton(task, taskColor));
            }
        }
    }

    private JPanel createTableHeader(JLabel label) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(label, BorderLayout.NORTH);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TangoColorFactory.ALUMINIUM_5),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        return headerPanel;
    }
    
    private JButton createButton(Task task, Color color) {
        JButton button = new JButton(new TaskAction(task));
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setBackground(color);
        return button;
    }

    private class TaskAction extends AbstractAction {

        private Task task;

        public TaskAction(Task task) {
            super(task.getLabel());
            this.task = task;
        }

        public void actionPerformed(ActionEvent e) {
            JPanel listFieldsPanel = new JPanel(new GridLayout(3, 2));
            listFieldsPanel.add(new JLabel("Starting time:"));
            List<Timestamp> timestampList = getWeek().getTimestampList();
            // Add 1 to array size to add null, which makes the entity unassigned
            JComboBox timestampListField = new JComboBox(
            		timestampList.toArray(new Object[timestampList.size() + 1]));
            timestampListField.setRenderer(new LabeledComboBoxRenderer());
            timestampListField.setSelectedItem(task.getStart());
            listFieldsPanel.add(timestampListField);
            int result = JOptionPane.showConfirmDialog(DiscoverySchedulerPanel.this.getRootPane(), listFieldsPanel,
                    "Select starting time", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                Timestamp toTimestamp = (Timestamp) timestampListField.getSelectedItem();
                if (task.getStart() != toTimestamp) {
                    solutionBusiness.doChangeMove(task, "start", toTimestamp);
                }                
                solverAndPersistenceFrame.resetScreen();
            }
        }

    }

}
