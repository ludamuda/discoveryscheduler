package discoveryscheduler.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


import org.apache.commons.lang3.tuple.Pair;
import org.optaplanner.examples.common.persistence.AbstractTxtSolutionImporter;

import discoveryscheduler.domain.*;
import discoveryscheduler.persistence.DiscoverySchedulerImportConfig;

public class DiscoverySchedulerImporter extends AbstractTxtSolutionImporter<Week> {
	 
	private static final String INPUT_FILE_SUFFIX = "dsb";//discovery schedule breakdown
    
    public static void main(String[] args) {
    	new DiscoverySchedulerImportConfig().loadConfig();
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
    	
    	private static final Properties configuration = DiscoverySchedulerImportConfig.getConfig();
    	
    	private static final int TIME_PERIODS_IN_DAY = Integer.parseInt(configuration.getProperty("time_periods_in_day"));
    	private static final String[] ACTIVITIES = (configuration.getProperty("activities")).split(",");
    	private static final int[] ACTIVITIES_LENGTH = Arrays.stream((configuration.getProperty("activities_length")).split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();
    	private static final int[] ACTIVITIES_INSTRUCTOR = Arrays.stream((configuration.getProperty("activities_instructor")).split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();
    	private static final int[] ACTIVITIES_LOCATION = Arrays.stream((configuration.getProperty("activities_location")).split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();
    	private static final int[] ACTIVITIES_TRANSPORT_LENGHT = Arrays.stream((configuration.getProperty("activities_transport_length")).split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();
    	private static final String[] LOCATIONS = (configuration.getProperty("locations")).split(",");
    	private static final Boolean[][] HOTEL_LOCATION_BUS_REQUIREMENT = 
    		{
    			{false, true, true, true, true, true},
    			{false, true, true, true, true, true},
    			{true, true, false, true, true, true},
    			{false, true, true, true, true, true},
    			{true, true, true, true, true, false},
    			{false, true, true, true, true, true},
    			{true, true, true, true, true, false},
    			{true, true, false, true, true, true},
    		};
    	private static final Boolean[][] HOTEL_ACTIVITY_BUS_REQUIREMENT = 
    		{
    			{false, false, false, false, false, false},
    			{false, false, false, false, false, false},
    			{false, true, true, true, true, true},
    			{true, true, true, true, true, true},
    			{true, true, true, true, true, false},
    			{false, true, true, true, true, true},
    			{false, false, false, false, false, false},
    			{true, true, true, true, true, true},
    			{false, true, true, true, true, true},
    		};
    	private static final Integer[][] HOTEL_LOCATION_PENALTY = 
    		{
    			{0, 0, 1, 1, 0, 0},
    			{1, 1, 3, 3, 1, 1},
    			{10, 10, 0, 5, 5, 8},
    			{0, 0, 1, 1, 1, 3},
    			{3, 2, 0, 0, 0, 0},
    			{0, 0, 3, 3, 2, 3},
    			{3, 2, 1, 1, 1, 1},
    			{3, 2, 0, 0, 0, 0},
    		};
    	private static final int NUMBER_OF_HOTELS = Integer.parseInt(configuration.getProperty("number_of_hotels")); 
        private static final int MAX_NUM_OF_INSTRUCTORS = Integer.parseInt(configuration.getProperty("max_num_of_instructors")); 
    	
        public Week readSolution() throws IOException {
            Week week = new Week();
            week.setId(0L); 
            
            week.setLocationList(generateLocationList());
            week.setActivityList(generateActivityList(week.getLocationList()));
            week.setHotelList(generateHotelList(week.getLocationList(), week.getActivityList()));
            
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
            for(int i = 0; i < week.getActivityList().size(); i++){
            	activityMap.put(week.getActivityList().get(i).getType(), week.getActivityList().get(i));
            }
            List<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> groupStayTimeList = new ArrayList<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>>(groupListSize);
            int numOfDays = 1;
            for (int i = 0; i < groupListSize; i++) {
                Group group = new Group();
                group.setId((long) i);
                // group line: <name> <hotel> <arrival day> <arrival time> <departure day> <departure time> <# of Activities> <Activity1> ... <ActivityN>
                String line = bufferedReader.readLine();
                String[] lineTokens = splitBySpacesOrTabs(line);
                if (lineTokens.length < 8) {
                    throw new IllegalArgumentException("Read line (" + line
                            + ") is expected to contain at least 8 tokens. (<name> <hotel> <arrival day> <arrival time> <departure day> <departure time> <# of Activities> <Activity1> ... <ActivityN>)");
                }
                group.setName(lineTokens[0]);
                
                group.setHotel(week.getHotelList().get(Integer.parseInt(lineTokens[1])));
                
                int arrival_day = Integer.parseInt(lineTokens[2]);
                int arrival_time = Integer.parseInt(lineTokens[3]);
                int departure_day = Integer.parseInt(lineTokens[4]);
                int departure_time = Integer.parseInt(lineTokens[5]);
                groupStayTimeList.add(Pair.of(Pair.of(arrival_day, arrival_time),Pair.of(departure_day, departure_time)));                
                if (departure_day+1 > numOfDays){
                	numOfDays = departure_day+1;
                }
                
                int numberOfRequriedActivities = Integer.parseInt(lineTokens[6]);
                if (lineTokens.length < (numberOfRequriedActivities + 7)) {
                    throw new IllegalArgumentException("Read line (" + line + ") is expected to contain at least"
                            + (numberOfRequriedActivities + 7) + " tokens.");
                }
                List<Activity> groupActivityList = new ArrayList<Activity>(numberOfRequriedActivities);
                for (int j = 7; j < lineTokens.length; j++) {
                	if(activityMap.containsKey(lineTokens[j])){
                		groupActivityList.add(activityMap.get(lineTokens[j]));
                	}
                	else {
                		throw new IllegalArgumentException("Activity " + lineTokens[j] + " not supported.");
                	}
                }
                group.setRequiredActivities(groupActivityList);
                
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
                    task.setLocked(false);
                    
                    taskList.add(task);
                }
            }
            week.setTaskList(taskList);
            week.setInstructorList(generateInstructorList(week.getGroupList().size()));
        }
        

        private List<Instructor> generateInstructorList(int numberOfGroups){
        	int numberOfInstructors = numberOfGroups < MAX_NUM_OF_INSTRUCTORS ? numberOfGroups : MAX_NUM_OF_INSTRUCTORS;
        	List<Instructor> instructorList = new ArrayList<Instructor>(numberOfInstructors);
        	for(int i = 0; i < numberOfInstructors ; i++){
        		Instructor instructor = new Instructor();
        		instructor.setId((long) i);
        		instructor.setInstructorIndex(i);
        		instructorList.add(instructor);
        	}
        	return instructorList;
        }
        
        private List<Hotel> generateHotelList(List<Location> locationList, List<Activity> activityList){
        	List<Hotel>hotelList = new ArrayList<Hotel>(NUMBER_OF_HOTELS);
        	for(int i = 0; i < NUMBER_OF_HOTELS; i++){
        		Hotel hotel = new Hotel();
        		hotel.setId((long) i);
        		hotel.setHotelIndex(i);
     
        		Map<Location, Boolean> hotelLocationBusRequirementMap = new HashMap<Location, Boolean>(LOCATIONS.length);
        		Map<Activity, Boolean> hotelActivityBusRequirementMap = new HashMap<Activity, Boolean>(ACTIVITIES.length-3);
        		Map<Location, Integer> hotelLocationPenaltyMap = new HashMap<Location, Integer>(LOCATIONS.length);
        		
        		for(int j = 0; j < LOCATIONS.length; j++){
        			hotelLocationBusRequirementMap.put(locationList.get(j), HOTEL_LOCATION_BUS_REQUIREMENT[j][i]);
        			hotelLocationPenaltyMap.put(locationList.get(j), HOTEL_LOCATION_PENALTY[j][i]);
        		}
        		for(int k = 3; k < ACTIVITIES.length; k++){
        			hotelActivityBusRequirementMap.put(activityList.get(k), HOTEL_ACTIVITY_BUS_REQUIREMENT[k-3][i]);
        		}
        		hotel.setHotelActivityBusRequirement(hotelActivityBusRequirementMap);
        		hotel.setHotelLocationBusRequirement(hotelLocationBusRequirementMap);
        		hotel.setHotelLocationPenalty(hotelLocationPenaltyMap);

        		hotelList.add(hotel);
        	}
        	
        	return hotelList;
        }
        
        private List<Location> generateLocationList(){
        	List<Location>locationList = new ArrayList<Location>(LOCATIONS.length);
        	for (int i = 0; i < LOCATIONS.length; i++){
        		Location location = new Location();
        		location.setId((long) i);
        		location.setLocationIndex(i);
        		String[] type = LOCATIONS[i].split(" ");
        		location.setType(type[0]);
        		location.setTypeNumber(Integer.parseInt(type[1]));
        		locationList.add(location);
        	}
        	
        	return locationList;
        }
        
        private List<Activity> generateActivityList(List<Location> locationList){
        	List<Activity>activityList = new ArrayList<Activity>(ACTIVITIES.length);
        	for(int i = 0; i < ACTIVITIES.length; i++){
        		Activity activity = new Activity();
        		activity.setId((long) i);
        		activity.setActivityIndex(i);
        		activity.setType(ACTIVITIES[i]);
        		activity.setLength(ACTIVITIES_LENGTH[i]+ACTIVITIES_TRANSPORT_LENGHT[i]);
        		if(ACTIVITIES_INSTRUCTOR[i] == 1){
        			activity.setInstructorRequired(true);
        		} else {
        			activity.setInstructorRequired(false);
        		}
        		if(ACTIVITIES_LOCATION[i] == 1){
        			activity.setLocationRequired(true);        			
        		} else {
        			activity.setLocationRequired(false);
        		}
        		List<Location> possibleLocations = new ArrayList<Location>();
    			for(Location location : locationList){
    				if(location.getType().equals(ACTIVITIES[i])){
    					possibleLocations.add(location);
    				}
    			}
    			activity.setPossibleLocations(possibleLocations);
        		
        		
        		activityList.add(activity);
        	}
        	
        	return activityList;
        }
    }
}
