package aggregate.data;

/*
* Class to build HTML output for data inputs
* controller that uses each html builder method in each respective data class 
*/
import java.util.ArrayList;
import java.io.*;

public class HTMLBuilder
{
    public static ArrayList<String> html; 

    public HTMLBuilder(ArrayList<AggData> arraylist)
    { 	//constructor using a sorted arraylist of data   
    	
    	//call builder which constructs/sets html value
    	this.htmlBuilder(arraylist);
    }

    public void htmlBuilder(ArrayList<AggData> aggData)
    {   //given list of aggdata, parse through it building html<String> which is used to create website
    	html = new ArrayList<String>();
    	int i = 0; // counter in order to determine first iteration of loop
    	int day = 9;
    	for(AggData a: aggData)
    	{	//get object type in order to call correct to html method
    		
    		//depreciated...used before toHTML was added to abstract class
    		/*String methodToCall = getObjectClass(a);
    		String temp = null;
    		
    		//call specific method for data object type
    		//call html builder for Imessage's
    		if(methodToCall.equals("IMessage")){ 
    			IMessage b = ((IMessage)a); //need to recast because eclipse told me so...
    			temp = b.htmlIMessage();
    		//call htmlbuilder for JPEG's
    		} else if (methodToCall.equals("JPEGMetadata")) {
    			JPEGMetadata b = ((JPEGMetadata) a);
    			temp = b.htmlJPEGMetadata();
    		} else {
    			continue;
        		}
    		} else if (methodToCall == "GoogleLocationData") {
    			temp = b.htmlGoogleLocationData();
    		} else if (methodToCall == "CCTransaction") {
    			temp = ab.htmlCCTransaction();
    		}*/
    		
    		//in order to add date images (i.e. at beginging of new day add an image that says "Wednesday"
    		//need to access the dates and compare. 
    		
    		/* don't need this for new website
    		//on first iteration of loop, set the day for future comparison
    		if(i == 0) {
    			day =  a.getDate().getDay(); //gets day of week 
    			html.add(getHTMLForDay(day)); //add image for day of week 
    			i++; //increment counter, not needed after first iteration of loop
    			//System.out.println("first of month");
    			//System.out.println(a.getDate().getDay());
    		}
    		
    		//compare days, if different, then add new image to show a new day
    		if(day != a.getDate().getDay()){
    			html.add(getHTMLForDay(a.getDate().getDay()));
    			//System.out.println("different day in same month");
    			//System.out.println(a.getDate().getDay());
    		}
    		
    		//set day for next iteration comparison
    		day = a.getDate().getDay();
    		*/
    		
    		//generate html for given data class. calls specific one depending on object type
    		String temp = a.toHTMLListItem(); 
    		//for each generated line of html, add to master html doc
    		html.add(temp);
    	}
    }
    
    public static String getHTMLForDay(int dayInt)
    {
    	//in the web index there is an image for each day of the week which will match the generated html
    	String day = "";
    	
    	//pick correct day depending on date
    	switch (dayInt)
    	{
    		case 0: day = "Sunday";
    				break;
    		case 1: day = "Monday";
					break;
    		case 2: day = "Tuesday";
					break;
    		case 3: day = "Wednesday";
					break;
    		case 4: day = "Thursday";
					break;
    		case 5: day = "Friday";
					break;
    		case 6: day = "Saturday";
					break;
    	}
    	
    	@SuppressWarnings("unused")
		String string;
    	return string = 
    			//"<div class = \"row\">"+
    	    		"\n\t<div class = \"col-lg-3\">" +
    	    			"<img src=" + "\"" + "../images/weekday/"+ day+ ".gif"+ "\"" + "class=\"img-thumbnail\" alt=\"Wheres da Photo? who knows...\" width=\"" + 3264 + "\" height=\""+ 2448 +"\">" +
    	    		"\n\t</div>";//+	
    	    //	"\n</div> ";
    }
    
    public static String getObjectClass(Object obj)
    { //returns the last value of the method signature
    	String string = ""+ obj.getClass();
    	String[] stringtwo = string.split("\\.");
    	return stringtwo[stringtwo.length - 1];
    }
    
    //@String : filepath to write to, @String: date to use as title of html file
    public void writeHTMLBuilder(String string, String Date) throws IOException
    {   //write out data from html
    	//String Date needs to be implemented in order to write out the title of the HTML document
    	FileWriter output = new FileWriter(string); //"/Users/aarongirard/Desktop/photograph.txt"
    	BufferedWriter out = new BufferedWriter(output);
    	
    	//add header to html file
    	ArrayList<String> al = htmlHeaderOrFooter(true);
    	for(String s: al){
    		out.write(s);
    		out.newLine();
    	}
    	
    	//add body to html file
    	for(String s: html)
    	{
    		out.write(s);
            out.newLine();
    	}
        
    	//add footer to html file
    	al = htmlHeaderOrFooter(false);
    	for(String s: al){
    		out.write(s);
            out.newLine();
    	}
    	
    	out.flush();
        out.close();
    }
    //method that returns arraylist of strings holding the lines
    //that need to be added to the begining of each HTML doc
    //returns txt for HTML Header if True or Returns Footer if false by
    //using a buffered reader to read in txt from files
    public static ArrayList<String> htmlHeaderOrFooter(boolean headerOrFooter)
    {
    	String filePath;
    	if(headerOrFooter) filePath  = "/Users/aarongirard/Desktop/data_resources/HTMLHeader.txt";
    		else filePath  =  "/Users/aarongirard/Desktop/data_resources/HTMLFooter.txt";
    	try{
    		ArrayList<String> AL = new ArrayList<String>();
    		BufferedReader br  = new BufferedReader(new FileReader( new File(filePath)));
    		for(String line; (line = br.readLine()) != null; )
    		{
    			AL.add(line);
    		}
    		br.close();
    		return AL;
    	} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();    
		}
    	return null; //won't occur
    }
}