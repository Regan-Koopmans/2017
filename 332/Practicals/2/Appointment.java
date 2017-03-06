import java.lang.StringBuilder;
import java.util.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;

public class Appointment {
    private String name = null;
    private Date date = null;
    private Time time = null;
    private String desc = null;
    private String participants = null;

    public String getName() {
        return name;
    }
    public Date getDate() {
        return date;
    }
    public String getDesc() {
        return desc;
    }
    public String getParticipants() {
        return participants;
    }
    public Time getTime() {
        return time;
    }
    public String getTimeString() {
        return (time == null) ? "none" : time.toString();
    }
    public String getDateString() {
        if (date == null) {
            return "none";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
            return sdf.format(date);
        }
    }

    // Setters

    public void setName(String name) {
        this.name = name;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public void setTime(String timeString) {
        this.time = Time.valueOf(timeString);
    }
    public void setParticipants(String part) {
        participants = part;
    }

    public void setDate(String input) {
        Date newDate = null;
        try {
            newDate = new SimpleDateFormat("dd/MM/yyyy").parse(input);
        } catch (Exception e) {
            System.out.println(e);
        }
        setDate(newDate);
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("# " + name + "\n");
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
            String dateString = sdf.format(date);
            sb.append("date:" + dateString + "\n");
        }
        if (time != null) {
            sb.append("time:" + time.toString() + "\n");
        }
        if (desc != null) {
            sb.append("desc:" + desc + "\n");
        }
        if (participants != null) {
            sb.append("part:" + participants + "\n");
        }
        return sb.toString();
    }

    public String toPrettyString() {
        StringBuilder sb = new StringBuilder();
        sb.append("#" + name+"\n");

        if (desc != null) {
            sb.append(desc+"\n");
        }
        if (participants != null) {
            sb.append("With: " + participants + "\n");
        }
        if (time != null) {
            sb.append("At " + time.toString() + "\n");
        }
        if (date != null) {
            sb.append("On " + (new SimpleDateFormat("dd/MM/YYYY")).format(date)) ;
        }
        sb.append("\n");
        return sb.toString();
    }
}
