package discoveryscheduler.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
//import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.examples.common.persistence.AbstractTxtSolutionImporter;

import discoveryscheduler.domain.*;


import discoveryscheduler.persistence.DiscoverySchedulerImportConfig;

public class DiscoverySchedulerImporter extends AbstractTxtSolutionImporter<Week> {
	
	private static final Properties configuration = DiscoverySchedulerImportConfig.getConfig();
	
	private static final int TIME_PERIODS_IN_DAY = Integer.parseInt(configuration.getProperty("time_periods_in_day"));
	private static final String[] ACTIVITIES = (configuration.getProperty("activities")).split(",");
	private static final int[] ACTIVITIES_LENGTH_IN_TIME_PERIODS = Arrays.stream((configuration.getProperty("activities_length")).split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();
	private static final int TRANSPORT_LENGHT = 2; 
	private static final String[] LOCATIONS = {"Climb", "Climb", "Climb", "MTB", "MTB", "Trek", "Trek", "Trek", "Trek", "Raft", "Raft", "Raft"};
	private static final String INPUT_FILE_SUFFIX = "dsb";//discovery schedule breakdown
    
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

    public TxtInputBuilder<Week> createTxtInputBuilder() {
        return new DiscoverySchedulerInputBuilder();
    }

    public static class DiscoverySchedulerInputBuilder extends TxtInputBuilder<Week> {

        public Week readSolution() throws IOException {
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
            List<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> groupStayTimeList = new ArrayList<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>>(groupListSize);
            int numOfDays = 1; 
            for (int i = 0; i < groupListSize; i++) {
                Group group = new Group();
                group.setId((long) i);
                // group line: <name> <arrival day> <arrival time> <departure day> <departure time> <# of Activities> <Activity1> ... <ActivityN>
                String line = bufferedReader.readLine();
                String[] lineTokens = splitBySpacesOrTabs(line);
                if (lineTokens.length < 7) {
                    throw new IllegalArgumentException("Read line (" + line
                            + ") is expected to contain at least 7 tokens. (<name> <arrival day> <arrival time> <departure day> <departure time> <# of Activities> <Activity1> ... <ActivityN>)");
                }
                group.setName(lineTokens[0]);
                
                int arrival_day = Integer.parseInt(lineTokens[1]);
                int arrival_time = Integer.parseInt(lineTokens[2]);
                int departure_day = Integer.parseInt(lineTokens[3]);
                int departure_time = Integer.parseInt(lineTokens[4]);
                groupStayTimeList.add(Pair.of(Pair.of(arrival_day, arrival_time),Pair.of(departure_day, departure_time)));                
                if (departure_day-arrival_day+1 > numOfDays){
                	numOfDays = departure_day-arrival_day+1;
                }
                
                int numberOfRequriedActivities = Integer.parseInt(lineTokens[5]);
                if (lineTokens.length < (numberOfRequriedActivities + 6)) {
                    throw new IllegalArgumentException("Read line (" + line + ") is expected to contain at least"
                            + (numberOfRequriedActivities + 6) + " tokens.");
                }
                List<Activity> groupActivityList = new ArrayList<Activity>(numberOfRequriedActivities);
                for (int j = 6; j < lineTokens.length - 1; j++) {
                	if(activityMap.containsKey(lineTokens[j])){
                		groupActivityList.add(activityMap.get(lineTokens[j]));
                	}
                	else{
	                    Activity activity = new Activity();
	                    activity.setId((long) (i+1)*(j+1));
	                    activity.setName(lineTokens[j]);
	                    switch(activity.getName()){
	                    case "Raft":
	                    	activity.setLength(5+2);
	                    	break;
	                    case "MTB":
	                    	activity.setLength(7+2);
	                    	break;
	                    case "Archery":
	                    	activity.setLength(7);
	                    	break;
	                    case "OB":
	                    	activity.setLength(7);
	                    	break;
	                    case "Trek":
	                    	activity.setLength(6);
	                    	break;
	                    default:
	                    	activity.setLength(6+2);
	                    	break;	
	                    }
	                    /*
	                    if(activity.getName().equals("Raft")) {
	                    	activity.setLength(5);
	                    } else if(activity.getName().equals("MTB") || activity.getName().equals("Archery") || 
	                    		activity.getName().equals("OB")){
	                    	activity.setLength(7);
	                    } else {
	                    	activity.setLength(6);
	                    }*/
	                    if(activity.getName().equals("MTB") || activity.getName().equals("C&R") || 
	                    		activity.getName().equals("HRHS") || activity.getName().equals("Climb")){
	                    	activity.setInstructorRequired(true);
	                    }
	                    else{
	                    	activity.setInstructorRequired(false);
	                    }
	                    if(activity.getName().equals("MTB") || activity.getName().equals("Trek") || 
	                    		activity.getName().equals("Raft") || activity.getName().equals("Climb")){
	                    	activity.setLocationRequired(true);
	                    }
	                    else{
	                    	activity.setLocationRequired(false);
	                    }
	                    groupActivityList.add(activity);
	                    activityMap.put(lineTokens[j], activity);
                	}
                    
                }
                group.setRequiredActivities(groupActivityList);
                group.setNumOfClients(Integer.parseInt(lineTokens[(6 + numberOfRequriedActivities + 1) - 1]));
                groupList.add(group);
            }
            
            createTimeSchedule(week, numOfDays);
            
            /**
             * Sets basic time window for each group (in timestamps).
             * 
             * For each groups iterates through the whole week's timestampList and selects only the timestamps between group arrival and departure.
             * These become groupTimestampList 
             */
            for (int i = 0; i < groupList.size(); i++){
            	int arrival_day = groupStayTimeList.get(i).getLeft().getLeft();
            	int arrival_time = groupStayTimeList.get(i).getLeft().getRight();
            	int departure_day = groupStayTimeList.get(i).getRight().getLeft();
            	int departure_time = groupStayTimeList.get(i).getRight().getRight();
            	int lenghtOfStayInPeriods = 0;
            	if (departure_day-arrival_day == 0){
            		lenghtOfStayInPeriods = departure_time-arrival_time;
            	} else if (departure_day-arrival_day == 1){
            		lenghtOfStayInPeriods = TIME_PERIODS_IN_DAY-arrival_time + departure_time;
            	} else {
            		lenghtOfStayInPeriods = TIME_PERIODS_IN_DAY-arrival_time + (departure_day-arrival_day-1) * TIME_PERIODS_IN_DAY + departure_time;
            	}
            	List<Timestamp> groupTimestampList = new ArrayList<Timestamp>(lenghtOfStayInPeriods);
            	for(Timestamp timestamp : week.getTimestampList()){
            		if(departure_day-arrival_day == 0) {
            			if(timestamp.getDay().getDayIndex() == arrival_day && (timestamp.getHour().getHourIndex() >= arrival_time && timestamp.getHour().getHourIndex() <= departure_time)){
            				groupTimestampList.add(timestamp);
            			}
            		} else {
            			if((timestamp.getDay().getDayIndex() == arrival_day && timestamp.getHour().getHourIndex() >= arrival_time)
            					|| (timestamp.getDay().getDayIndex() == departure_day && timestamp.getHour().getHourIndex() <= departure_time)
            					|| (timestamp.getDay().getDayIndex() > arrival_day && timestamp.getDay().getDayIndex() < departure_day)	){
            				groupTimestampList.add(timestamp);
            			}
            		}
            	}
            	groupList.get(i).setGroupTimestampList(groupTimestampList);
            }
            
            
            week.setGroupList(groupList);
            week.setActivityList(new ArrayList<Activity>(activityMap.values()));
           
        }
        
        private void createTimeSchedule(Week week, int dayListSize) {
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
            week.setLocationList(generateLocationList());
        }
        
        private static final int MAX_NUM_OF_INSTRUCTORS = 10;
        private List<Instructor> generateInstructorList(int numberOfInstructors){
        	numberOfInstructors = numberOfInstructors < MAX_NUM_OF_INSTRUCTORS ? numberOfInstructors : MAX_NUM_OF_INSTRUCTORS;
        	List<Instructor> instructorList = new ArrayList<Instructor>(numberOfInstructors);
        	for(int i = 0; i < numberOfInstructors ; i++){
        		Instructor instructor = new Instructor();
        		instructor.setId((long) i);
        		instructor.setInstructorIndex(i);
        		instructor.setGuide(false);
        		instructorList.add(instructor);
        	}
        	
        	return instructorList;
        }
        
        private List<Location> generateLocationList(){
        	List<Location>locationList = new ArrayList<Location>(LOCATIONS.length);
        	for(int i = 0; i < LOCATIONS.length; i++){
        		Location location = new Location();
        		location.setId((long) i);
        		location.setLocationIndex(i);
        		location.setType(LOCATIONS[i]);
        		locationList.add(location);
        	}
        	
        	return locationList;
        }
    }
}
