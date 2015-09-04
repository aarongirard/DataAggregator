package aggregate.data;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.lang.GeoLocation;

import geocode.kdtree.ReverseGeoCode;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

import javax.imageio.*;

import java.awt.image.*;
import java.util.ArrayList;

public class JPEGMetadata extends AggData { //implements Comparable<JPEGMetadata>
	private Date date;
	//private String location;
	private File file; //location of file being read in
	protected BufferedImage img; //needs to be accessed by other class i.e. image processing just for now change back to private
	public  ArrayList<JPEGMetadata> jpegMetadatas;
	public String latlong;
	
	//empty constructor
	//**if any data does not appear in a jpeg file it will remain null**
	public JPEGMetadata()
	{
		this.date = null;
		this.location = null;
		this.file = null;
		this.latlong = null;
	}
	
	//constructor with two parameters:
	//@date, @location
	public JPEGMetadata(Date date, String location, File file)
	{
		this.date = date;
		this.location = location;
		this.file = file;
	}
	
	public Date getDate()  //don't need to override
	{
		return date;
	}
	
	public File getFile()
	{
		return file;
	}
	
	public void setDate(Date date)
	{
		this.date = date;
	}
	
	
	
	public void setFile(File file)
	{
		this.file = file;
	}
	
	@Override
	public String toString()
	{
		/*//just want file name w/o entire filepath --  testing
		String[] shortenedFilepaths = (file.toString()).split("/");
		String shortenedFilepath = shortenedFilepaths[shortenedFilepaths.length - 1];
		int string  = getNumberForFilepath(shortenedFilepath);
		return "";*/
		
		return ("Location: " + location + "\n Date: "+ date + "\n File Path: "+ file);
	}
	
	public String toHTML() 
    {//method which turns object into usable html code
		String string;
		string  = "Aaron took this photo on " + date + " at " + location;
		int[] intArray = this.getPixelsDim();
		int width =intArray[1];
		int height = intArray[0];
		
		/* old way of assignign name for filepath of image --way before compress
		//just want file name w/o entire filepath
		String[] shortenedFilepaths = (file.toString()).split("/");
		String shortenedFilepath = shortenedFilepaths[shortenedFilepaths.length - 1];
		getNumberForFilepath(shortenedFilepath) + "/"+ shortenedFilepath
		*/
		//System.out.println("../images/" + this.getDate().getTime()+ ".jpg");
		
		return string = 
			//"<div class = \"row\">"+
	    		"\n\t<div class = \"col-lg-3\">"+
	    			"\n\t\t<div class=\"well-lg\">"+
	    				"\n\t\t <center>"+ string + "</center>"+					
	    			"\n\t\t</div>"+
	    		"\n\t</div>"+
	    		"\n\t<div class = \"col-lg-3\">" +
	    			"<img src=" + "\"" + "../images/"+ this.getDate().getTime()+ ".jpg"+ "\"" + "class=\"img-thumbnail\" alt=\"Wheres da Photo? who knows...\" width=\"" + width + "\" height=\""+ height +"\">" +
	    		"\n\t</div>";//+	
	    //	"\n</div> ";
    }
	//this is a list item abstraction of jpegmetaData to be used on new website. It is meant to be called instead of the above toHTML.
	public String toHTMLListItem(){
		//String dataType = "img"; --> static as below
		String dataPosition  = latlong;
		String dataDescription = location;
		String dataTime = "" + date.getTime();
		
		String LI = "<li data-type=\"img\" data-position =\""+ dataPosition +"\" data-description=\""+ dataDescription + "\" data-time=\""+dataTime+"\">" + 
		dataDescription + "</li>";
		
		return  LI;
	}
	
	//used with old way of assigning filepath for html reference to image
	//get the number in a string
	/*private static int getNumberForFilepath(String string) 
	{
		char[] chars = string.toCharArray();
		String number = "";
		for(char c: chars){
			int ascii = (int)c;
			if(ascii < 58 && ascii > 47){
				number += c;
			}
		}
		int stringToNumber = Integer.parseInt(number);
		stringToNumber = stringToNumber/100;
		return stringToNumber;
		
	}*/
	
	private int[] getPixelsDim() 
	{//returns array with height in first index and width in second, path as pixels
			//before had read img here with try catch
		try{	
			this.img = ImageIO.read(file); 
			int[] intArray = new int[2];
			intArray[0] = img.getHeight();
			intArray[1] = img.getWidth();
			img = null; //for garbage collection
			return intArray;
		} catch (IOException e){
			
		}
		return null;
	}

	//this method constructs an arraylist given the input of jpegs. 
	//atm it is hard coded to the filepath but may be changed to accept a file path + other params !!
	//@param: reverseGeoCode object so that the locations can be derived
	public void setJPEGMetadata(ReverseGeoCode reverseGeoCode, File inputImageDir, File fileOutputFile)  //at some point do by passing filepath?
	{
		/*
        * write all jpeg data into arraylist of type jpegMetaData
        */
        
		//make dir to store compressed images
		(new File(fileOutputFile + "/images")).mkdir();
		fileOutputFile = new File(fileOutputFile + "/images");
		
        //initialize arraylist to hold aggdata
        jpegMetadatas = new ArrayList<JPEGMetadata>();
        
        //object which compresses/writes jpegs
        ImageProcessing imageProcessing = new ImageProcessing();
        
        //get all images in directory
        String[] imageFileNames = inputImageDir.list(); //return list of the names of each file, but not full path
        /*for(String s: imageFileNames)
        {
        	System.out.println(s);
        }*/
        //add metadata to arraylist 
        //this for loop iterates through each file in the folder
        for(int x = 0; x<imageFileNames.length; x++){ 
        	File file = new File(inputImageDir+"/"+imageFileNames[x]);
            
            //specific use for jpg files
            try {
                //read in metadata from jpeg file
                Metadata metadata = JpegMetadataReader.readMetadata(file);
                
                //create new object AggData to hold information from new jpeg
                JPEGMetadata placeholder = new JPEGMetadata();
                placeholder.setFile(file);
                
                if (jpgDate(metadata) != null) {
                    placeholder.setDate(jpgDate(metadata));
                    //System.out.println(jpgDate(metadata));
                }
               
                if(jpgLocation(metadata) != null){
                   String [] lonlat= jpgLocation(metadata);
                   placeholder.latlong = lonlat[0] + "," + lonlat[1]; //set long lat I think this is the correct syntasx for output but need to check 
                  
                    String place =""+ reverseGeoCode.nearestPlace(Double.parseDouble(lonlat[0]), Double.parseDouble(lonlat[1]));
                    placeholder.setLocation(place);
                    //System.out.println(place);
                }
                
                //add placeholder to arraylist only if date != null (for sorting reasons)
                //also if date is greater than september 2014 -- only for my info
                Date date = new Date(new Long("1409554800000"));
               
                //only add photo if it has a date, location, and was taken after sept 2014
                if(placeholder.getDate() != null && placeholder.getLocation() != null && (placeholder.getDate().compareTo(date) > 0 )){ 
                	jpegMetadatas.add(placeholder); //switched this up a bit
                	
                	// write compressed image to folder using date to determine which folder it is in
                    placeholder.img = ImageIO.read(file); 
                   
                    //set new file path for new  compressed image
                    imageProcessing.processImage(new File(fileOutputFile+"/"+placeholder.getDate().getTime()+".jpg"), placeholder.img); 
                    
                    //must set to null for garbage collection or else cache will fill up
                    placeholder.img = null;
                    //System.out.println(file + "compressed image set");
                   
                }
               
            } catch (JpegProcessingException e) {
                // handle exception
            } catch (IOException e) {
                // handle exception
            }
            //System.out.println("----------------"+ x +"---------------------");
        }
       
        
        //sort arrylist in ascending order based on date
     	Collections.sort(jpegMetadatas);
	}

/*
 * Two static methods below are used in the construction of the arraylist 
 */
	//get long/lat of a photo in an array,null if no gps coords 
    //string[0]= long; string[1] = lat
    public static String[] jpgLocation(Metadata metadata)
    {
        // See whether it has GPS data, if so print it
        GpsDirectory gpsDirectories = metadata.getDirectory(GpsDirectory.class);
        if (gpsDirectories != null){    
            GeoLocation geoLocation = gpsDirectories.getGeoLocation();
            if (geoLocation != null && !geoLocation.isZero()) {
                String x = "" + geoLocation + ""; //cast geolocation to string
                //System.out.println(x);
                String[] lonlat = x.split(",\\s*" ); //split string into 2 index array
                return lonlat; 
            }       
        
        }
        return null;
    }
    
    //get date a photo was taken
    //null if no date
    public static Date jpgDate(Metadata metadata)
    {
        //get date
        ExifSubIFDDirectory directory = metadata.getDirectory(ExifSubIFDDirectory.class);
        
        // query the tag's value, if has date, print date
        if(directory != null){
            Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
            return date;  
        }
        return null;  
    }

	
	/*@Override
	public int compareTo(JPEGMetadata o)
	{
		if (getDate() == null || o.getDate() == null)
		      return 0;
		return getDate().compareTo(o.getDate());
	}*/
}
