package aggregate.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class CCTransaction  extends AggData { //implements Comparable<CCTransaction>
	public ArrayList<CCTransaction> ccTransactions;
	
	public CCTransaction(){
		//empty constructor
	}
	public CCTransaction(Date date, String location)
	{
		this.date = date;
		this.location = location;
	}
	
	/*@Override
	public int compareTo(CCTransaction o)
	{
		if (getDate() == null || o.getDate() == null)
		      return 0;
		return getDate().compareTo(o.getDate());
	}*/
	
	/*@Override
	public String toString()
	{
		return ("Location: " + location + "\n Date: "+ date);
	}*/
	
	//to print out csv
	@Override
	public String toString()
	{
		return (location + ","+ date);
	}
	
	public String toHTML()
	{
		String string = 
		    	//"<div class = \"row\">"+
		    		"\n\t<div class = \"col-lg-4\">"+
		    			"\n\t\t<div class=\"well-lg\">"+
		    				"\n\t\t <center>"+ "Aaron used his credit card at" + location+ " on " + date + "</center>"+					
		    			"\n\t\t</div>"+
		    		"\n\t</div>";//+
		    	//"\n</div> ";
		return string;
	}
	
	public String toHTMLListItem()
	{
		//String dataType = ccTransaction --> set staticly below cause won't change
		String dataDescription = location;
		String dataTime = "" + date.getTime();
		
		String LI = "<li data-type=\"ccTransaction\" data-description=\""+ dataDescription + "\" data-time=\""+dataTime+"\">"+ dataDescription +"</li>";
		return  LI;
	}
	
	public void setCCTransactionData()
	{//set CCTransaction arraylist in object
		ccTransactions = new ArrayList<CCTransaction>();
	    
		//read in cc transaction data from file
        File file = new File("/Users/aarongirard/Desktop/data_resources/CC_Transactions_2.txt");
        try{
        	BufferedReader br = new BufferedReader(new FileReader(file));
        	//parse each line of file
	        for(String line; (line = br.readLine()) != null; )
	       	{
	       		StringTokenizer st  = new StringTokenizer(line);
	       		LinkedList<String> ll = new LinkedList<String>();
	       		//add each token (seperated by whitespace) to linked list
	        	while(st.hasMoreTokens())
	        	{
	        		ll.add(st.nextToken());
	        	}
	        	String string =  ll.removeFirst();
	        	int one = Integer.parseInt("" + string.charAt(0) + string.charAt(1));
	        	int two  = Integer.parseInt("" + string.charAt(3) + string.charAt(4));
	        	
	        	//create date object from string of month/day in cc statement
	        	Date date = null;
	        	if ( one > 6 ){
	        		Calendar calendar = new GregorianCalendar();
	        		calendar.set(2014, one, two);
	        		date = calendar.getTime();
	        	} else{
	        		Calendar calendar = new GregorianCalendar();
	        		calendar.set(2015, one, two);
	        		date = calendar.getTime();
	        	}
	        	ll.removeFirst();
	        	String location = "";
	        	while(!ll.isEmpty()){
	        		 location += " " + ll.removeFirst();
	        	}
	        	ccTransactions.add(new CCTransaction(date, location));
	       	}
        br.close(); //close reader
        } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();    
		}
	}
}
