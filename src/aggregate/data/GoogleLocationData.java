package aggregate.data;

import geocode.kdtree.ReverseGeoCode;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

public class GoogleLocationData extends AggData { //implements Comparable<GoogleLocationData>
	public Timestamp timestamp;
	public Double latitude;
	public Double longitude;
	public ArrayList<GoogleLocationData> googleLocationDatas; //does this mean every googleLocationdata has an empty arraylist?
	public String latlong;
	//public String location;
	/*public int accuracy; Add in later?
	public int velocity;
	public int heading;
	public int altitude;
	public int verticalAccuracy;*/
	
	public GoogleLocationData()
	{
		this.timestamp = null;
		this.latitude = null;
		this.longitude = null;
	}
	
	public GoogleLocationData(Timestamp timestamp, Double latitude, Double longitude, String latlong)
	{
		this.timestamp = timestamp;
		this.latitude = latitude;
		this.longitude = longitude;
		setDateFromTimestamp(timestamp);
		this.latlong = latlong;
	}
	
	//sets a date value of agg data
	public void setDateFromTimestamp(Timestamp timestamp)
	{
		this.date = (Date)(timestamp);
	}
	/*public void setLocation(String location)
	{
		this.location = location;
	}*/
	
	/*public String getLocation()
	{
		return location;
	}*/
	
	public Timestamp getTimestamp()
	{
		return timestamp;
	}
	
	public Double getLatitude()
	{
		return latitude;
	}
	
	public Double getLongitude()
	{
		return longitude;
	}
	
	//for csv
	@Override
	public String toString()
	{
		Date date = new Date( timestamp.getTime());
		return (location + ","+ date);
			/* "\n latitude: "+ latitude + "\n longitude: "+ longitude);*/
	}
	
	/*@Override
	public int compareTo(GoogleLocationData o)
	{
		return getTimestamp().compareTo(o.getTimestamp());
	}*/
	
	 /*public String[] htmlGoogleLocationData(GoogleLocationData googleLocationData)
	    {

	    }*/
	
	public void writeToText() throws IOException
	{//depreciated
		//write location data to txt file
        FileWriter output = new FileWriter("/Users/aarongirard/Desktop/Locationdata.txt");
        BufferedWriter out = new BufferedWriter(output);
            try{
                out.write("Location: " + this.getLocation());
                out.newLine();
                
                out.write("Date: " + new Date( this.getTimestamp().getTime()));
                out.newLine();
                out.newLine();
                
            } catch (IOException e){
                //handle exception
            }
         
         //close writer
         out.flush();
         out.close();
         
	}
	
	public String toHTML()
	{
		String string = 
		    //	"<div class = \"row\">"+
		    		"\n\t<div class = \"col-lg-4\">"+
		    			"\n\t\t<div class=\"well-lg\">"+
		    				"\n\t\t <center>"+ "Aaron was  at " + location + " on " + date + "</center>"+					
		    			"\n\t\t</div>"+
		    		"\n\t</div>"; //+
		    //	"\n</div> ";
		return string;
	}
	
	public String toHTMLListItem(){
		//String dataType = "googleLocationData"; --> static as below
		String dataPosition  = latlong;
		String dataDescription = location;
		String dataTime = "" + date.getTime();
		
		String LI = "<li data-type=\"googleLocationData\" data-position =\""+ dataPosition +"\" data-description=\""+ dataDescription + "\" data-time=\""+dataTime+"\">" +
		dataDescription +"</li>";
		return  LI;
	}
	
	public void setGoogleLocationData(ReverseGeoCode reverseGeoCode)
	{
		/* Here is the reading of Google Location Data in the form of json 
		 * It is read in, converted to object, sorted (already sorted?)
		 * then it will be printed out with the geo data from the photo
		 */
		//array list to hold location data from google location data
		googleLocationDatas = new ArrayList<GoogleLocationData>();

		//read in google location history --HARD CODED FOR NOW
		File file = new File("/Users/aarongirard/Desktop/data_resources/GoogleLocationHistory.json");
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String timestampMs ="";
			String latitude = "";
			String longitude = "";
			int x= 10000000; //need to divide lon/lat to format; its scientific notation at 1E7, so divide by 1E7 =) wow 6th grade math doooge
			Double latitudeDouble = 0.0;
			Double longitudeDouble = 0.0;

			//use buffered reader to read in lines....specific to this file format
			//the information is in JSON so need to continue inorder to get next line of object
			//really boogy way of doing this lol  ---- to homebrew for you
			
			
			for (String line;(line = br.readLine()) != null; ) {
				
				if (line.contains("timestampMs")){
					timestampMs = line.substring(21,line.length() - 2); //21 to length-2
					continue;
				}
				if(line.contains("latitudeE7")){
					latitude = line.substring(19, line.length() - 1);
					latitudeDouble = Double.parseDouble(latitude)/x;
					continue;
 				}
				
				if(line.contains("longitudeE7"))
				{
					longitude = line.substring(20, line.length() - 1);
					longitudeDouble = Double.parseDouble(longitude)/x;
					latlong = latitudeDouble + "," + longitudeDouble;
					googleLocationDatas.add(new GoogleLocationData( new Timestamp(Long.parseLong(timestampMs)),latitudeDouble,longitudeDouble, latlong ));
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();    
		}
		Collections.sort(googleLocationDatas);


		for( GoogleLocationData s: googleLocationDatas){
			String string = "" + reverseGeoCode.nearestPlace(s.getLatitude(), s.getLongitude());
			s.setLocation(""+ string);
		}
		
		//need to remove the entries that re repeats for the better design on the website -- need to be aware of it if I need this
		//data for something else
		String previousLocation = " ";
		GoogleLocationData s;
		Iterator<GoogleLocationData> iter = googleLocationDatas.iterator();
		while(iter.hasNext()){
			s = iter.next();
			//System.out.println(s);
			if(previousLocation.equals(s.location)){
				iter.remove();
				//System.out.println("removed");
			}
			previousLocation = s.location;
		}
	}
}
	
