package aggregate.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

//notes:
/* phonenumber: +13109137479
*date: "jan 29, 2015, 10:55PM <-- need to avoud these two commas when seperating lines
* yes/no depending if I sent the message
* mesage.sadfsaf.dsafaf...asfdsdf
*/
public class IMessage extends AggData {

	public String message; 
	public long phoneNumber;
	public boolean whoSent; //true if I sent, false otherwise
	public ArrayList<IMessage> iMessages;
	
	
	public void setMessage(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return message;
	}
	
	public void setPhoneNumber(long phoneNumber){
		this.phoneNumber = phoneNumber;
	}
	
	public long getPhoneNumber(){
		return phoneNumber;
	}
	
	public void setWhoSent(boolean whoSent){
		this.whoSent = whoSent;
	}
	
	public boolean getWhoSent(){
		return whoSent;
	}
	
	public String toString(){
		return "Textee: " + phoneNumber + "\n Date: " + date + "\n Who Sent: " + whoSent + "\nMessage: " + message;  	
	}
	
	public static IMessage parseIMessage(String string) throws ParseException{
		IMessage temp  = new IMessage();
		String[] tokenArray  = string.split(",", 6);
	
    	//three cases for sender ID: phonenumber, empty, emailaddress (icloud...)
    	if(tokenArray[0].isEmpty() || isWhiteSpace(tokenArray[0])){
    		temp.setPhoneNumber(0000000000);
    	} else if(isNumeric(tokenArray[0].substring(1))){
    		temp.setPhoneNumber(Long.parseLong(tokenArray[0].substring(0))); //sets phonenumber minus the + as double
    	} else temp.setPhoneNumber(1111111111); //email address for imessage, only ints supported as type
    	
    	//parsing time
    	String time  = tokenArray[1].substring(1)+" "+tokenArray[2]+" "+tokenArray[3].substring(0,tokenArray[3].length()-1);
    	//System.out.println("1 " + time);
    	SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy hh:mm a");
    	Date date = sdf.parse(time);
    	//System.out.println("2 "+ time);
    	temp.setDate(date);
    	
    	//who sent, if user sent, then true else false
    	if(tokenArray[4].equals("Yes")) temp.setWhoSent(true);
    	else temp.setWhoSent(false);
    	
    	//set message
    	if(tokenArray[5].isEmpty()) temp.setMessage("");
    	else temp.setMessage(tokenArray[5]);
    	
    	return temp;
	}
	
	public static boolean isNumeric(String str)
	{
	    for (char c : str.toCharArray())
	    {
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;
	}

	public String toHTML()
    {
    	String string;
    	if(whoSent){
    		string = "On "+ date + ", Aaron messaged " + ",\""  + message + "\""; 
    	} else{
    		string  = "On "+ date + "," + phoneNumber + " messaged Aaron" + ", \""  + message + "\"";
    	}

    	return string = 
    	//"<div class = \"row\">"+
    		"\n\t<div class = \"col-lg-4\">"+
    			"\n\t\t<div class=\"well-lg\">"+
    				"\n\t\t <center>"+ string + "</center>"+					
    			"\n\t\t</div>"+
    		"\n\t</div>"; //+
    	//"\n</div> ";
    }
	
	public String toHTMLListItem(){
		String dataDescription;
		String myPhoneNumber = "3109137479";
		if(whoSent){
			//sender is this person
			dataDescription =  "true," + myPhoneNumber + "," + message;
    	} else{
    		//someone sent to this phone
    		dataDescription  = "false," + phoneNumber +"," +  message;
    	}
		
		//String dataType = "img"; --> static as below
		
		String dataTime = "" + date.getTime();
		
		String LI = "<li data-type=\"iMessage\"  data-description=\""+ dataDescription + "\" data-time=\""+dataTime+"\">" + "text at some time" + "</li>";
		return  LI;
	}
	
	public static boolean isWhiteSpace(String str)
	{
		for(char c: str.toCharArray())
		{
			if (c !=  ' ') return false;
		}
		return true;
	}
	
	public void setIMessageData()
	{
		//initiate arraylist
		iMessages  = new ArrayList<IMessage>();
		//file of imessages to be read
		File fileImsg = new File("/Users/aarongirard/Desktop/data_resources/Aaron_iMessage_History.csv");
		try{
			BufferedReader brtwo = new BufferedReader(new FileReader(fileImsg));
			//use buffered reader to read in lines....specific to this file format
			//reads in a line and exectutes loop while next line exists
			for (String line;(line = brtwo.readLine()) != null; ) {
				IMessage temp = new IMessage();
				try {
					temp =temp.parseIMessage(line);  //parseIMessage returns IMessage object with info on 1 imsg
				} catch (ParseException e) {
					e.printStackTrace();
				} 
				iMessages.add(temp); //add object to arraylist
			}
			brtwo.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();    
		}
	     Collections.sort(iMessages);
	}
}
