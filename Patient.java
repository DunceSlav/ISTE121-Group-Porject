import java.io.Serializable;
public class Patient implements Serializable, Comparable<Patient>
{
   // attributes
   private static final long serialVersionUID = 01L;
   
   private String firstName;
   private String lastName;
   
   private String dateOfBirth;
   private int age;
   
   private double height;
   private double weight;
   
   private String reason;
   private int daysStayed;
   private boolean insurance;
   private double cost;
   
   
   // Constructor
   public Patient(String f, String l, String d, int a, double h, double w, String r, int ds, double c, boolean i)
   {
      firstName = f;
      lastName = l;
      dateOfBirth = d;
      a = age;
      height = h;
      weight = w;
      reason = r;
      cost = c;
      daysStayed = ds;
      insurance = i;
   }
   
   
   // Accsessors
   public String getFirstName()
   {
      return firstName;
   }
   
   public String getLastName()
   {
      return lastName;
   }
   
   public String getDateOfBirth()
   {
      return dateOfBirth;
   }  
   
   public int getAge()
   {
      return age;
   } 
   
   public double getHeight()
   {
      return height;
   }
   
   public double getWeight()
   {
      return weight;
   }
   
   public String getReason()
   {
      return reason;
   }  
   
   public double getCost()
   {
      return cost;
   }
   
   public int getDays()
   {
      return daysStayed;
   } 
   
   public boolean getInsurance()
   {
      return insurance;
   }

   
   
   // Mutators
   public void setFirstName(String x)
   {
      firstName = x;
   }
   
   public void setLastName(String x)
   {
      lastName = x;
   }
   
   public void setDateOfBirth(String x)
   {
      dateOfBirth = x;
   }

   public void setAge(int x)
   {
      age = x;
   }
   
   public void setHeight(double x)
   {
      height = x;
   }   
   
   public void setWeight(double x)
   {
      weight = x;
   }
   
   public void setReason(String x)
   {
      reason = x;
   }   
   
   public void setCost(double x)
   {
      cost = x;
   }
   
   public void setInsurance(boolean x)
   {
      insurance = x;
   }
   
   public void setDays(int x)
   {
      daysStayed = x;
   }
   
   
   
   // toString
   public String toString() 
   {
      
      return String.format("%-20s %-20s %-20s %-20s %-20s", lastName,firstName,dateOfBirth,reason ,String.format("$%.2f",cost));

   }


   @Override
   public int compareTo(Patient p) {
      return this.getLastName().compareTo(p.getLastName());
   }
}
