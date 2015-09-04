package aggregate.data;

import java.util.Date;

public abstract class AggData implements Comparable<AggData>{
	public Date date;
	public String location;
	
	public void setDate(Date date){
		this.date = date;
	}
	public Date getDate()
	{
		return this.date;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}
	
	public String getLocation()
	{
		return location;
	}
	
	public int compareTo(AggData o)
	{
		if (getDate() == null || o.getDate() == null)
		      return 0;
		return getDate().compareTo(o.getDate());
	}
	
	//method overrided for each data type that prints out ready to use HTML using object
	public String toHTML(){
		return "not defined...";
	}
	//similar to above method but returns list itme abstraction fo each data type
	public String toHTMLListItem(){
		return "not defined...";
	}
}
