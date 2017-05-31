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
import org.optaplanner.examples.common.swingui.components.Labeled;
import org.optaplanner.examples.common.swingui.components.LabeledComboBoxRenderer;
import org.optaplanner.examples.common.swingui.timetable.TimeTablePanel;

import discoveryscheduler.domain.*;

public class DiscoverySchedulerPanel extends SolutionPanel {

    public static final String LOGO_PATH = "/discoveryscheduler/swingui/logo.png";

    
    private final TimeTablePanel<Group, Integer> groupsPanel;
    private final TimeTablePanel<Instructor, Integer> instructorsPanel;
    private final TimeTablePanel<Location, Integer> locationsPanel;
    private final TimeTablePanel<Group, Timestamp> DEVPanel;
    

    public DiscoverySchedulerPanel() {
    	

        setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        groupsPanel = new TimeTablePanel<Group, Integer>();
        tabbedPane.add("Groups", new JScrollPane(groupsPanel));
        instructorsPanel = new TimeTablePanel<Instructor, Integer>();
        tabbedPane.add("Instructors", new JScrollPane(instructorsPanel));
        locationsPanel = new TimeTablePanel<Location, Integer>();
        tabbedPane.add("Locations", new JScrollPane(locationsPanel));
        DEVPanel = new TimeTablePanel<Group, Timestamp>();
        tabbedPane.add("DEV", new JScrollPane(DEVPanel));
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
        locationsPanel.reset();
        DEVPanel.reset();
        Week week = (Week) solution;
        defineGrid(week);
        fillCells(week);
        repaint(); // Hack to force a repaint of TimeTableLayout during "refresh screen while solving"
    }
    
    private void defineColumnHeaders(TimeTablePanel panel, List list, int footprintWidth ){
    	panel.defineColumnHeaderByKey(HEADER_COLUMN_GROUP1); // Day header
        panel.defineColumnHeaderByKey(HEADER_COLUMN); // Hour header
        for (Object group : list) {
            panel.defineColumnHeader(group, footprintWidth);
        }
        panel.defineColumnHeader(null, footprintWidth); // Unassigned
        
           
    }
    private void defineRowHeaders(TimeTablePanel panel, Week week, int rowsPerDay){
        panel.defineRowHeaderByKey(HEADER_ROW); // Group header 
        if(rowsPerDay > 3){
        	for (Timestamp timestamp : week.getTimestampList()) {
        		panel.defineRowHeader(timestamp);
        	}
        } else {
        	for (int i = 1; i <= week.getDayList().size() * rowsPerDay; i++){
            	panel.defineRowHeader(i);
            }
        }
        panel.defineRowHeader(null); // Unassigned period
    }
    
    /**
     * Defining the panels and grid layout
     * @param week
     */
    private void defineGrid(Week week) {
        JButton footprint = new JButton("LinLetGre1-0");
        footprint.setMargin(new Insets(0, 0, 0, 0));
        int footprintWidth = footprint.getPreferredSize().width;
        
        defineColumnHeaders(groupsPanel, week.getGroupList(), footprintWidth);
        defineColumnHeaders(instructorsPanel, week.getInstructorList(), footprintWidth);
        defineColumnHeaders(locationsPanel, week.getLocationList(), footprintWidth);
        defineColumnHeaders(DEVPanel, week.getGroupList(), footprintWidth);
        
        defineRowHeaders(groupsPanel, week, 2);
        defineRowHeaders(instructorsPanel, week, 3);
        defineRowHeaders(locationsPanel, week, 3);
        defineRowHeaders(DEVPanel, week, week.getTimestampList().size()/week.getDayList().size());
    }
    
    /**
     * Filling all the text into all the cells (handler)
     * @param week
     */
    private void fillCells(Week week) {
        fillGroupCells(week);
        fillInstructorCells(week);
        fillLocationCells(week);
        fillDEVCells(week);
        fillTimestampCells(week);
        fillTaskCells(week);

    }
     
    /**
     * filling in the column headers descriptions
     * @param week
     */
    private void fillGroupCells(Week week) {
    	groupsPanel.addCornerHeader(HEADER_COLUMN_GROUP1, HEADER_ROW, createTableHeader(new JLabel("Day")));
        groupsPanel.addCornerHeader(HEADER_COLUMN, HEADER_ROW, createTableHeader(new JLabel("Hour")));
        for (Group group : week.getGroupList()) {
            groupsPanel.addColumnHeader(group, HEADER_ROW,
                    createTableHeader(new JLabel(group.getLabel(), SwingConstants.CENTER)));
        }
        groupsPanel.addColumnHeader(null, HEADER_ROW,
                createTableHeader(new JLabel("Unassigned", SwingConstants.CENTER)));
    }
    
    /**
     * filling in the column headers descriptions
     * @param week
     */
    private void fillInstructorCells(Week week) {
    	instructorsPanel.addCornerHeader(HEADER_COLUMN_GROUP1, HEADER_ROW, createTableHeader(new JLabel("Day")));
        instructorsPanel.addCornerHeader(HEADER_COLUMN, HEADER_ROW, createTableHeader(new JLabel("Hour")));
        for (Instructor instructor : week.getInstructorList()) {
        	instructorsPanel.addColumnHeader(instructor, HEADER_ROW,
                    createTableHeader(new JLabel(instructor.getLabel(), SwingConstants.CENTER)));
        }
        instructorsPanel.addColumnHeader(null, HEADER_ROW,
                createTableHeader(new JLabel("Unassigned", SwingConstants.CENTER)));
    }
    
    /**
     * filling in the column headers descriptions
     * @param week
     */
    private void fillLocationCells(Week week) {
    	locationsPanel.addCornerHeader(HEADER_COLUMN_GROUP1, HEADER_ROW, createTableHeader(new JLabel("Day")));
        locationsPanel.addCornerHeader(HEADER_COLUMN, HEADER_ROW, createTableHeader(new JLabel("Hour")));
        for (Location location : week.getLocationList()) {
        	locationsPanel.addColumnHeader(location, HEADER_ROW,
                    createTableHeader(new JLabel(location.getLabel(), SwingConstants.CENTER)));
        }
        locationsPanel.addColumnHeader(null, HEADER_ROW,
                createTableHeader(new JLabel("Unassigned", SwingConstants.CENTER)));
    }
    
    /**
     * filling in the column headers descriptions
     * @param week
     */
    private void fillDEVCells(Week week) {
    	DEVPanel.addCornerHeader(HEADER_COLUMN_GROUP1, HEADER_ROW, createTableHeader(new JLabel("Day")));
        DEVPanel.addCornerHeader(HEADER_COLUMN, HEADER_ROW, createTableHeader(new JLabel("Hour")));
        for (Group group : week.getGroupList()) {
            DEVPanel.addColumnHeader(group, HEADER_ROW,
                    createTableHeader(new JLabel(group.getLabel(), SwingConstants.CENTER)));
        }
        DEVPanel.addColumnHeader(null, HEADER_ROW,
                createTableHeader(new JLabel("Unassigned", SwingConstants.CENTER)));
    }

    /**
     * Filling in the headers of rows (radky)
     * @param week
     */
    private void fillTimestampCells(Week week) {
        for (Day day : week.getDayList()) {
        	Timestamp dayStartTimestamp = day.getTimestampList().get(0);
            Timestamp dayEndTimestamp = day.getTimestampList().get(day.getTimestampList().size() - 1);
            
            groupsPanel.addRowHeader(HEADER_COLUMN_GROUP1, 2*day.getDayIndex() + 1, HEADER_COLUMN_GROUP1, 2*day.getDayIndex() + 2,
            		createTableHeader(new JLabel(day.getLabel())));
            instructorsPanel.addRowHeader(HEADER_COLUMN_GROUP1, 3*day.getDayIndex() + 1, HEADER_COLUMN_GROUP1, 3*day.getDayIndex() + 3,
            		createTableHeader(new JLabel(day.getLabel())));
            locationsPanel.addRowHeader(HEADER_COLUMN_GROUP1, 3*day.getDayIndex() + 1, HEADER_COLUMN_GROUP1, 3*day.getDayIndex() + 3,
            		createTableHeader(new JLabel(day.getLabel())));
            
            DEVPanel.addRowHeader(HEADER_COLUMN_GROUP1, dayStartTimestamp, HEADER_COLUMN_GROUP1, dayEndTimestamp,
                    createTableHeader(new JLabel(day.getLabel())));
           
            for (int i = 1; i <= 2; i++){
            	String rowName = "";
            	switch (i){
            	case 1:
            		rowName = "Morning"; //latest start at 11.00
            		break;
            	case 2:
            		rowName = "Afternoon"; // earliest start at 11.30
            		break;
            	}
            	groupsPanel.addRowHeader(HEADER_COLUMN, 2*day.getDayIndex() + i,
                        createTableHeader(new JLabel(rowName)));  
            }
            for (int i = 1; i <= 3; i++){
            	String rowName = "";
            	switch (i){
            	case 1:
            		rowName = "Morning"; //latest start at 10.00
            		break;
            	case 2:
            		rowName = "1st Afternoon"; // earliest start at 10.30, latest at 14.00
            		break;
            	case 3:
            		rowName = "2nd Afternoon"; // earliest start at 14.30
            		break;
            	}
            	instructorsPanel.addRowHeader(HEADER_COLUMN, 3*day.getDayIndex() + i,
                        createTableHeader(new JLabel(rowName))); 
            	locationsPanel.addRowHeader(HEADER_COLUMN, 3*day.getDayIndex() + i,
                        createTableHeader(new JLabel(rowName))); 
            }
            for (Timestamp timestamp : day.getTimestampList()) {
                DEVPanel.addRowHeader(HEADER_COLUMN, timestamp,
                        createTableHeader(new JLabel(timestamp.getHour().getLabel())));
            }
        }
        DEVPanel.addRowHeader(HEADER_COLUMN_GROUP1, null, HEADER_COLUMN_GROUP1, null,
                createTableHeader(new JLabel("Unassigned")));
        groupsPanel.addRowHeader(HEADER_COLUMN_GROUP1, null, HEADER_COLUMN_GROUP1, null,
                createTableHeader(new JLabel("Unassigned")));
        instructorsPanel.addRowHeader(HEADER_COLUMN_GROUP1, null, HEADER_COLUMN_GROUP1, null,
                createTableHeader(new JLabel("Unassigned")));
        locationsPanel.addRowHeader(HEADER_COLUMN_GROUP1, null, HEADER_COLUMN_GROUP1, null,
                createTableHeader(new JLabel("Unassigned")));
    }
    
    /**
     * Placing tasks into the grid - especially where to place them(in each panel)
     * @param week
     */
    private void fillTaskCells(Week week) {
        TangoColorFactory tangoColorFactory = new TangoColorFactory();
        for (Task task : week.getTaskList()) {
            Color taskColor = tangoColorFactory.pickColor(task.getActivity());
            if(task.getStart() == null){
            	groupsPanel.addCell(task.getGroup(), null, createButton(task, taskColor, "groups"));
            	DEVPanel.addCell(task.getGroup(), task.getStart(), createButton(task, taskColor, "groups"));
                instructorsPanel.addCell(task.getInstructor(),null, createButton(task, taskColor, "instructors"));
                locationsPanel.addCell(task.getLocation(), null, createButton(task, taskColor, "locations"));
            } else {
            	int dayStartPos1 = task.getDay().getDayIndex() * 2;
            	if(task.getStart().getHour().getHourIndex() < 7){ //latest start at 11.00
            		dayStartPos1 += 1;
            	} else { // earliest start at 11.30
            		dayStartPos1 += 2;
            	}
            	groupsPanel.addCell(task.getGroup(), dayStartPos1, task.getGroup(), dayStartPos1, createButton(task, taskColor, "groups"));
	            DEVPanel.addCell(task.getGroup(), task.getStart(), task.getGroup(), 
	            		task.getEnd(), createButton(task, taskColor, "Groups"));
	            if (task.getInstructor() == null){
	            	instructorsPanel.addCell(task.getInstructor(), null, null, null,
		                    createButton(task, taskColor, "instructors"));
	            } else {
		            int dayStartPos2 = task.getDay().getDayIndex() * 3;
	            	if(task.getStart().getHour().getHourIndex() < 5){ //latest start at 10.00
	            		dayStartPos2 += 1;
	            	}
	            	else if(task.getStart().getHour().getHourIndex() < 13){ // earliest start at 10.30, latest at 14.00
	            		dayStartPos2 += 2;
	            	}
	            	else { // earliest start at 14.30
	            		dayStartPos2 += 3;
	            	}
		            instructorsPanel.addCell(task.getInstructor(), dayStartPos2, task.getInstructor(), 
		            		dayStartPos2, createButton(task, taskColor, "instructors"));
		        }
	            if (task.getLocation() == null){
	            	locationsPanel.addCell(task.getLocation(), null, null, null,
		                    createButton(task, taskColor, "locations"));
	            } else {
	            	int dayStartPos3 = task.getDay().getDayIndex() * 3;
	            	if(task.getStart().getHour().getHourIndex() < 5){ //latest start at 10.00
	            		dayStartPos3 += 1;
	            	}
	            	else if(task.getStart().getHour().getHourIndex() < 13){ // earliest start at 10.30, latest at 14.00
	            		dayStartPos3 += 2;
	            	}
	            	else { // earliest start at 14.30
	            		dayStartPos3 += 3;
	            	}
	            	locationsPanel.addCell(task.getLocation(), dayStartPos3, task.getLocation(), 
	            			dayStartPos3, createButton(task, taskColor, "locations"));
	            }
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
    
    private JButton createButton(Task task, Color color, String panel) {
    	String label = task.getStart() != null ? task.getStart().getHour().getLabel() + " " : "";
    	switch (panel){
	        case "groups":
	        	label += task.getActivity().getName();
	            break;
	        case "instructors":
	        	label += task.getActivity().getName();
	            break;
	        case "locations":
	        	label += task.getGroup().getName();
	            break;
	        default:
	        	label = task.getLabel();
	        	break;
        }
        
        JButton button = new JButton(new TaskAction(task));
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setBackground(color);
        button.setText(label);
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
