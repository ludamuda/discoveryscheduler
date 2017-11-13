package discoveryscheduler.swingui;

import static org.optaplanner.examples.common.swingui.timetable.TimeTablePanel.HeaderColumnKey.HEADER_COLUMN;
import static org.optaplanner.examples.common.swingui.timetable.TimeTablePanel.HeaderColumnKey.HEADER_COLUMN_GROUP1;
import static org.optaplanner.examples.common.swingui.timetable.TimeTablePanel.HeaderRowKey.HEADER_ROW;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import javax.swing.border.TitledBorder;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.hssf.util.HSSFColor.RED;
//import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.examples.common.swingui.SolutionPanel;
//import org.optaplanner.examples.common.swingui.components.Labeled;
import org.optaplanner.examples.common.swingui.components.LabeledComboBoxRenderer;
import org.optaplanner.examples.common.swingui.timetable.TimeTablePanel;

import org.optaplanner.swing.impl.TangoColorFactory;

import discoveryscheduler.domain.*;

public class DiscoverySchedulerPanel extends SolutionPanel<Week> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 170795823157068652L;


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

    public boolean isRefreshScreenDuringSolving() {
        return true;
    }

    private Week getWeek() {
        return (Week) solutionBusiness.getSolution();
    }

    public void resetPanel(Week solution) {
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
        fillGroupsPanelHeaders(week);
        fillInstructorsPanelHeaders(week);
        fillLocationsPanelHeaders(week);
        fillDEVPanelHeaders(week);
        fillTimestampCells(week);
        fillTaskCells(week);
    }
     
    /**
     * filling in the column headers descriptions
     * @param week
     */
    private void fillGroupsPanelHeaders(Week week) {
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
    private void fillInstructorsPanelHeaders(Week week) {
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
    private void fillLocationsPanelHeaders(Week week) {
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
    private void fillDEVPanelHeaders(Week week) {
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
     * and marking arrivals and departures of groups in the grid
     * @param week
     */
    private void fillTaskCells(Week week) {
    	Map<Group, boolean[]> DEVPanelMarkedArrsAndDeps = new HashMap<>();
    	Map<Group, boolean[]> groupsPanelMarkedArrsAndDeps = new HashMap<>();
    	for(Group group : week.getGroupList()){
    		DEVPanelMarkedArrsAndDeps.put(group, new boolean[] {false, false});//{arrival, departure}
    		groupsPanelMarkedArrsAndDeps.put(group, new boolean[] {false, false});
    	}
        TangoColorFactory tangoColorFactory = new TangoColorFactory();
        for (Task task : week.getTaskList()) {
            Color taskColor = tangoColorFactory.pickColor(task.getActivity());
            //no start time set yet, unassigned column
            if(task.getStart() == null){ 
            	groupsPanel.addCell(task.getGroup(), null, createButton(task, taskColor, "groups"));
            	DEVPanel.addCell(task.getGroup(), task.getStart(), createButton(task, taskColor, "groups"));
                instructorsPanel.addCell(task.getInstructor(),null, createButton(task, taskColor, "instructors"));
                locationsPanel.addCell(task.getLocation(), null, createButton(task, taskColor, "locations"));
            //start time is set
            } else { 
            	
            	//start time is set, creating tasks in groupsPanel, marking the arrival or departure of group when needed
            	int dayStartPos1 = task.getDay().getDayIndex() * 2;
            	if(task.getStart().getHour().getHourIndex() < 7){ //latest start at 11.00
            		dayStartPos1 += 1;
            	} else { // earliest start at 11.30
            		dayStartPos1 += 2;
            	}
            	int dayArrivalPos = task.getGroup().getGroupTimestampList().get(0).getDay().getDayIndex() * 2;
            	if(task.getGroup().getGroupTimestampList().get(0).getHour().getHourIndex() < 7){ //latest start at 11.00
            		dayArrivalPos += 1;
            	} else { // earliest start at 11.30
            		dayArrivalPos += 2;
            	}
            	int dayDeparturePos = task.getGroup().getGroupTimestampList().get(task.getGroup().getGroupTimestampList().size()-1).getDay().getDayIndex() * 2;
            	if(task.getGroup().getGroupTimestampList().get(task.getGroup().getGroupTimestampList().size()-1).getHour().getHourIndex() < 7){ //latest start at 11.00
            		dayDeparturePos += 1;
            	} else { // earliest start at 11.30
            		dayDeparturePos += 2;
            	}
            	JButton groupButton = createButton(task, taskColor, "groups");
            	if(dayStartPos1 == dayArrivalPos){
            		setBorder(groupButton, "arrival");
            		boolean[] arrAndDep= groupsPanelMarkedArrsAndDeps.get(task.getGroup());
	            	arrAndDep[0] = true;
	            	groupsPanelMarkedArrsAndDeps.replace(task.getGroup(), arrAndDep);
            	}
            	if(dayStartPos1 == dayDeparturePos){
            		setBorder(groupButton, "departure");
            		boolean[] arrAndDep= groupsPanelMarkedArrsAndDeps.get(task.getGroup());
	            	arrAndDep[1] = true;
	            	groupsPanelMarkedArrsAndDeps.replace(task.getGroup(), arrAndDep);
            	}
            	if(dayStartPos1 == dayArrivalPos && dayStartPos1 == dayDeparturePos){
            		setBorder(groupButton, "both");
            		groupsPanelMarkedArrsAndDeps.replace(task.getGroup(), new boolean[]{true, true});
            	}
            	groupsPanel.addCell(task.getGroup(), dayStartPos1, task.getGroup(), dayStartPos1, groupButton);
            	
            	//start time is set, creating tasks in DEVPanel, marking the arrival or departure of group when needed
            	JButton DEVButton = createButton(task, taskColor, "Groups");
	            if(task.getStart() == task.getGroup().getGroupTimestampList().get(0)){
	            	setBorder(DEVButton, "arrival");
	            	boolean[] arrAndDep= DEVPanelMarkedArrsAndDeps.get(task.getGroup());
	            	arrAndDep[0] = true;
	            	DEVPanelMarkedArrsAndDeps.replace(task.getGroup(), arrAndDep);
	            }
	            if(task.getEnd() == task.getGroup().getGroupTimestampList().get(task.getGroup().getGroupTimestampList().size()-1)){
	            	setBorder(DEVButton, "departure");
	            	boolean[] arrAndDep= DEVPanelMarkedArrsAndDeps.get(task.getGroup());
	            	arrAndDep[1] = true;
	            	DEVPanelMarkedArrsAndDeps.replace(task.getGroup(), arrAndDep);
	            }
	            if(task.getStart() == task.getGroup().getGroupTimestampList().get(0) && task.getEnd() == task.getGroup().getGroupTimestampList().get(task.getGroup().getGroupTimestampList().size()-1)){
            		setBorder(DEVButton, "both");
	            	DEVPanelMarkedArrsAndDeps.replace(task.getGroup(), new boolean[]{true, true});
            	}
	            DEVPanel.addCell(task.getGroup(), task.getStart(), task.getGroup(), task.getEnd(), DEVButton);
	           
	            //start time is set, creating tasks in instructorsPanel
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
	            //start time is set, creating tasks in locationPanel
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
        
        //marking arrivals and departures when the time is not colliding with some activity
        //only needed in DEV and groups panels
        for(Group group : week.getGroupList()){
        	//marking in DEVPanel
        	boolean[] DEVmarked = DEVPanelMarkedArrsAndDeps.get(group);
        	if(DEVmarked[0] == false){
        		DEVPanel.addCell(group, group.getGroupTimestampList().get(0), createBorderCell("A", group.getGroupTimestampList().get(0)));
        	}
        	if(DEVmarked[1] == false){
        		DEVPanel.addCell(group, group.getGroupTimestampList().get(group.getGroupTimestampList().size()-1), createBorderCell("D", group.getGroupTimestampList().get(group.getGroupTimestampList().size()-1)));
        	}
        	
        	//marking in groupsPanel
        	int dayArrivalPos = group.getGroupTimestampList().get(0).getDay().getDayIndex() * 2;
        	if(group.getGroupTimestampList().get(0).getHour().getHourIndex() < 7){ //latest start at 11.00
        		dayArrivalPos += 1;
        	} else { // earliest start at 11.30
        		dayArrivalPos += 2;
        	}
        	int dayDeparturePos = group.getGroupTimestampList().get(group.getGroupTimestampList().size()-1).getDay().getDayIndex() * 2;
        	if(group.getGroupTimestampList().get(group.getGroupTimestampList().size()-1).getHour().getHourIndex() < 7){ //latest start at 11.00
        		dayDeparturePos += 1;
        	} else { // earliest start at 11.30
        		dayDeparturePos += 2;
        	}
        	boolean[] groupsMarked = groupsPanelMarkedArrsAndDeps.get(group);
        	if(dayArrivalPos == dayDeparturePos && groupsMarked[0] == false && groupsMarked[1] == false){
        		groupsPanel.addCell(group, dayArrivalPos, createBorderCell("B", group.getGroupTimestampList().get(0), group.getGroupTimestampList().get(group.getGroupTimestampList().size()-1)));	
        	}else{
	        	if(groupsMarked[0] == false){
	        		groupsPanel.addCell(group, dayArrivalPos, createBorderCell("A", group.getGroupTimestampList().get(0)));
	        	}
	        	if(groupsMarked[1] == false){
	        		groupsPanel.addCell(group, dayDeparturePos, createBorderCell("D", group.getGroupTimestampList().get(group.getGroupTimestampList().size()-1)));
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
    /**
     * Overload of createBorderCell(arrOrDep, time, null) for when there is no need for second time
     * @param arrOrDep
     * @param time
     * @return
     */
    private JPanel createBorderCell(String arrOrDep, Timestamp time){
    	return createBorderCell(arrOrDep, time, null);
    }
    
    /**
     * Creates cells with set borders
     * used to mark arrival or departure of group when the time doesn't collide with assigned task
     * @param arrOrDep expects String in shape "A" for arrival or "D" for departure or "B" for both
     * @param time expects Timestamp corresponding with the arrival or departure 
     * @param time2 used only when both arrival and departure within the same part of day 
     * @return
     */
    private JPanel createBorderCell(String arrOrDep, Timestamp time, Timestamp time2){
    	JPanel cell = new JPanel();
    	cell.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.RED));
    	if(arrOrDep == "A"){
    		cell.add(new JLabel("Arrival " + time.getHour().getLabel()));
    	} else if (arrOrDep == "D"){
    		cell.add(new JLabel("Departure " + time.getHour().getLabel()));
    	} else if(arrOrDep == "B"){
    		cell.setBorder(BorderFactory.createMatteBorder(2, 0, 2, 0, Color.RED));
    		cell.add(new JLabel(time.getHour().getLabel() + " - " + time2.getHour().getLabel()));
    	}
    	return cell;
    	
    }
    
    /**
     * sets top or bottom border or both of provided button depending on the border argument
     * @param button
     * @param border
     */
    private void setBorder(JButton button, String border){
    	if(border == "arrival"){
    		button.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.RED));
        	//button.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.RED), "Arrival", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP, button.getFont() ,Color.RED));
        }else if(border == "departure"){
        	button.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.RED));
        }else if(border == "both"){
        	button.setBorder(BorderFactory.createMatteBorder(2, 0, 2, 0, Color.RED));
        }
    }
    
    private JButton createButton(Task task, Color color, String panel){
    	return createButton(task, color, panel, null);
    }
    
    private JButton createButton(Task task, Color color, String panel, String border) {
    	String label = task.getStart() != null ? task.getStart().getHour().getLabel() + " " : "";
    	switch (panel){
	        case "groups":
	        	label += task.getActivity().getName();
	            break;
	        case "instructors":
	        	if (task.getLocation() != null){
	        		label += task.getLocation().getLabel();
	        	} else {
	        		label += task.getActivity().getName();
	        	}
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
        
        //mark departure and arrival
        if (border != null){
	        if(border == "arrival"){
	        	button.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.RED));
	        }
	        if(border == "departure"){
	        	button.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.RED));
	        }
        }
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
            timestampListField.setRenderer(new LabeledComboBoxRenderer(null));
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
