package discoveryscheduler.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.examples.common.persistence.AbstractTxtSolutionImporter;

import discoveryscheduler.domain.*;

public class DiscoverySchedulerImporter extends AbstractTxtSolutionImporter {
	
	private static final int TIME_PERIODS_IN_DAY = 25; //8.00, 8.30, 9.00, 9.30 .. 20.00
    private static final String INPUT_FILE_SUFFIX = "ctt";
    private static final int ACTIVITY_LENGTH_IN_HALF_HOURS = 6;
    
    public static void main(String[] args) {
        new DiscoverySchedulerImporter().convertAll();
    }

    public DiscoverySchedulerImporter() {
        super(new DiscoverySchedulerDao());
    }

    @Override
    public String getInputFileSuffix() {
        return INPUT_FILE_SUFFIX;
    }

    public TxtInputBuilder createTxtInputBuilder() {
        return new DiscoverySchedulerInputBuilder();
    }

    public static class DiscoverySchedulerInputBuilder extends TxtInputBuilder {

        public Solution readSolution() throws IOException {
            Week week = new Week();
            week.setId(0L);

            week.setNumber(readIntegerValue("Week:"));

            int groupListSize = readIntegerValue("Groups:");

            readGroupList(week, groupListSize);
            
            readEmptyLine();
            readConstantLine("END\\.");
            
            createTaskList(week);
            
            return week;
        }

        private void readGroupList(Week week, int groupListSize) throws IOException {
            readEmptyLine();
            readConstantLine("GROUPS:");
            List<Group> groupList = new ArrayList<Group>(groupListSize);
            Map<String, Activity> activityMap = new HashMap<String, Activity>();
            List<Pair<Integer, Integer>> groupStayTimeList = new ArrayList<Pair<Integer, Integer>>(groupListSize);
            for (int i = 0; i < groupListSize; i++) {
                Group group = new Group();
                group.setId((long) i);
                // Groups: <name> <arrival> <departure> <# Activities> <Activity1> ... <ActivityN>
                String line = bufferedReader.readLine();
                String[] lineTokens = splitBySpacesOrTabs(line);
                if (lineTokens.length < 5) {
                    throw new IllegalArgumentException("Read line (" + line
                            + ") is expected to contain at least 5 tokens.");
                }
                group.setName(lineTokens[0]);
                
                int arrival = Integer.parseInt(lineTokens[1]);
                int departure = Integer.parseInt(lineTokens[2]);                
                groupStayTimeList.add(Pair.of(arrival, departure));                
                if (week.getDayList() == null || departure-arrival+1 > week.getDayList().size()){
                	updateTimeSchedule(week, departure-arrival+1);
                }
                
                int numberOfRequriedActivities = Integer.parseInt(lineTokens[3]);
                if (lineTokens.length != (numberOfRequriedActivities + 4)) {
                    throw new IllegalArgumentException("Read line (" + line + ") is expected to contain "
                            + (numberOfRequriedActivities + 4) + " tokens.");
                }
                List<Activity> groupActivityList = new ArrayList<Activity>(numberOfRequriedActivities);
                for (int j = 4; j < lineTokens.length; j++) {
                	if(activityMap.containsKey(lineTokens[j])){
                		groupActivityList.add(activityMap.get(lineTokens[j]));
                	}
                	else{
	                    Activity activity = new Activity();
	                    activity.setId((long) j*i);
	                    activity.setName(lineTokens[j]);
	                    activity.setLength(ACTIVITY_LENGTH_IN_HALF_HOURS);
	                    if(activity.getName().equals("MTB") || activity.getName().equals("Lezeni") || 
	                    		activity.getName().equals("C&R") || activity.getName().equals("HighRopes")){
	                    	activity.setInstructorRequired(true);
	                    }
	                    else{
	                    	activity.setInstructorRequired(false);
	                    }
	                    groupActivityList.add(activity);
	                    activityMap.put(lineTokens[j], activity);
                	}
                    
                }
                group.setRequiredActivities(groupActivityList);
                groupList.add(group);
            }
            
            for (int i = 0; i < groupList.size(); i++){
            	int arrival = groupStayTimeList.get(i).getLeft();
            	int departure = groupStayTimeList.get(i).getRight();
            	int lenghtOfStay = departure - arrival + 1;
            	List<Timestamp> groupTimestampList = new ArrayList<Timestamp>(lenghtOfStay * TIME_PERIODS_IN_DAY);
            	for(Timestamp timestamp : week.getTimestampList()){
            		if(timestamp.getDay().getDayIndex() >= arrival && timestamp.getDay().getDayIndex() <= departure){
            			groupTimestampList.add(timestamp);
            		}
            	}
            	groupList.get(i).setGroupTimestampList(groupTimestampList);
            }
            
            
            week.setGroupList(groupList);
            week.setActivityList(new ArrayList<Activity>(activityMap.values()));
           
        }
        
        private void updateTimeSchedule(Week week, int dayListSize) {
        	int hourListSize = TIME_PERIODS_IN_DAY;
        	
        	List<Day> dayList = new ArrayList<Day>(dayListSize);
            for (int i = 0; i < dayListSize; i++) {
                Day day = new Day();
                day.setId((long) i);
                day.setDayIndex(i);
                day.setTimestampList(new ArrayList<Timestamp>(hourListSize));
                dayList.add(day);
            }
            week.setDayList(dayList);
            
            List<Hour> hourList = new ArrayList<Hour>(hourListSize);
            for (int i = 0; i < hourListSize; i++) {
                Hour hour = new Hour();
                hour.setId((long) i);
                hour.setHourIndex(i);
                hourList.add(hour);
            }
            week.setHourList(hourList);
            
            int timestampListSize = dayListSize * hourListSize;
            List<Timestamp> timestampList = new ArrayList<Timestamp>(timestampListSize);
            for (int i = 0; i < dayListSize; i++) {
                Day day = dayList.get(i);
                List<Timestamp> dayTimestampList = new ArrayList<Timestamp>(hourListSize);
                for (int j = 0; j < hourListSize; j++) {
                    Timestamp timestamp = new Timestamp();
                    timestamp.setId((long) (i * hourListSize + j));
                    timestamp.setTimestampIndex(i * hourListSize + j);
                    timestamp.setDay(day);
                    timestamp.setHour(hourList.get(j));
                    timestampList.add(timestamp);
                    dayTimestampList.add(timestamp);
                }
                day.setTimestampList(dayTimestampList);
            }
            week.setTimestampList(timestampList);
        }
        
        private void createTaskList(Week week) {
            List<Group> groupList = week.getGroupList();
            int taskListSize = 0;
            for(Group group : groupList){
            	taskListSize += group.getRequiredActivities().size();
            }
            List<Task> taskList = new ArrayList<Task>(taskListSize);
            long id = 0L;
            for (Group group : groupList) {
                for (Activity activity : group.getRequiredActivities()) {
                    Task task = new Task();
                    task.setId(id);
                    id++;
                    task.setActivity(activity);
                    task.setGroup(group);

                    taskList.add(task);
                }
            }
            week.setTaskList(taskList);
            week.setInstructorList(generateInstructorList(week.getTaskList().size()));
        }
        
        private List<Instructor> generateInstructorList(int numberOfInstructors){
        	List<Instructor> instructorList = new ArrayList<Instructor>(numberOfInstructors);
        	for(int i = 0; i < numberOfInstructors; i++){
        		Instructor instructor = new Instructor();
        		instructor.setId((long) i);
        		instructor.setInstructorIndex(i);
        		instructor.setGuide(false);
        		instructorList.add(instructor);
        	}
        	
        	return instructorList;
        }
    }
}
