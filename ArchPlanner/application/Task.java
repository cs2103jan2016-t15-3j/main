package application;

public class Task {
    private String description;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    
    public Task(String des, String sd, String st, String ed, String et) {
        description = des;
        startDate = sd;
        startTime = st;
        endDate = ed;
        endTime = et;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getStartDate() {
        return startDate;
    }
    
    public String getStartTime() {
        return startTime;
    }
    
    public String getEndDate() {
        return endDate;
    }
    
    public String getEndTime() {
        return endTime;
    }
}
