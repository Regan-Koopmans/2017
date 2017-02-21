import java.lang.StringBuilder;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Appointment {
  private String name;
  private Date date;

  // Getters

  public String getName() { return name; }
  public Date getDate() { return date; }

  // Setters

  public void setName(String name) { this.name = name; }
  public void setDate(Date date) { this.date = date; }

  public void setDate(String input) {
    Date newDate = null;
    try { newDate = new SimpleDateFormat("dd/MM/yyyy").parse(input);
    } catch (Exception e) { System.out.println(e); }

    setDate(newDate);
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("# " + name + "\n");
    if (date != null) {
      sb.append("date:" + date.toString() + "\n");
    }
    return sb.toString();
  }
}
