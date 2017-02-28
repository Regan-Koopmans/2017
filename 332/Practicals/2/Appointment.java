import java.lang.StringBuilder;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Appointment {
  private String name = null;
  private Date date = null;
  private String desc = null;

  // Getters

  public String getName() { return name; }
  public Date getDate() { return date; }
  public String getDesc() { return desc; }

  // Setters

  public void setName(String name) { this.name = name; }
  public void setDate(Date date) { this.date = date; }

  public void setDate(String input) {
    Date newDate = null;
    try { newDate = new SimpleDateFormat("dd/MM/yyyy").parse(input);
    } catch (Exception e) { System.out.println(e); }

    setDate(newDate);
  }

  public void setDesc(String desc) { this.desc = desc; }


  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("# " + name + "\n");
    if (date != null) {
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
      String dateString = sdf.format(date);
      sb.append("date:" + dateString + "\n");
    }
    if (desc != null) {
      sb.append("desc:" + desc + "\n");
    }
    return sb.toString();
  }
}
