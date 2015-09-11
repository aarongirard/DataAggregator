package aggregate.data;

import geocode.kdtree.ReverseGeoCode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.Calendar;
import java.lang.Double;
import java.lang.Long;

//scripts to write out individuals objects to file are in data project/writeToFileFiles
public class AggDataCompiler
{
    public static void main(String[] args) throws IOException, ParseException
    {
    	 /*
    	  * This section is the creation of output directory's and the setting of input directories
    	  * can be eventually changed to interact with an input GUI (here for convenient access)
    	  */
    	 //set root for  output directory
    	 (new File("/Users/aarongirard/Desktop/DataOutput")).mkdir(); //make new directory to direct all output
    	 final File desktop = new File ("/Users/aarongirard/Desktop/DataOutput");//this is used as a way to set output
    	 
    	 //set directory of images
    	 File inputImageDir = new File("/Users/aarongirard/Desktop/data_resources/Gphotos");
    	 
    	
    	long start = System.currentTimeMillis();//timer
       /*
        * write all jpeg data into arraylist of type jpegMetaData
        */
        
        //construct object containing txtfile data to reverse geocode lat/long
        //intensive time wise so do once and use for all reverse geocoding
    	//send same reversgeGeocode object in call to whichever data constructor needs it
    	System.out.println("Setting Reverse GeoCoder!");
    	ReverseGeoCode reverseGeoCode = null;
        try {
                 reverseGeoCode = new ReverseGeoCode(new FileInputStream("/Users/aarongirard/Desktop/data_resources/US.txt"), true);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();    
            }
        
    	//arraylist which hold all Data
       	ArrayList<AggData> ad = new ArrayList<AggData>();	
        
       	/*
         * Detailed descriptions of the parsing/creating of each type of data can be found
         * in the respective class =)
        */
       	
        //read in jpeg metadata / construct datastructure
        JPEGMetadata jpegMetadata = new JPEGMetadata();
        jpegMetadata.setJPEGMetadata(reverseGeoCode,inputImageDir, desktop); //reverseGeoCode, input dir, output directoy
        System.out.println("JPEG Metadata Parsing");
        for(JPEGMetadata s: jpegMetadata.jpegMetadatas)
        {
        	ad.add((AggData)s);
        	//System.out.println(s);
        }
        
        //read in Google Location Data / construct data structure
       	System.out.println("Google Location Data parsing");
       	GoogleLocationData googleLocationData = new GoogleLocationData();
        googleLocationData.setGoogleLocationData(reverseGeoCode);
        
        for(GoogleLocationData s:googleLocationData.googleLocationDatas)
        {
        	ad.add((AggData)s);
        }

        //read in IMessage data / construct data structure
        IMessage iMessage = new IMessage();
        iMessage.setIMessageData();
        System.out.println("IMessage Data parsing");
        for(IMessage s: iMessage.iMessages)
        {
        	ad.add((AggData)s);
        }
        
        //read in CCTransaction data / create data structure
        CCTransaction ccTransaction = new CCTransaction();
        ccTransaction.setCCTransactionData();
        System.out.println("CCTransaction Data Parsing");
        for(CCTransaction s: ccTransaction.ccTransactions)
        {
        	ad.add((AggData)s);
        }
        //sort collection using java system sort
       	Collections.sort(ad);
     
       	//print out ad data
       	/*for(AggData s: ad){
       		System.out.println(s);
       	}*/
       	
       	ArrayList<AggData> byMonth = new ArrayList<AggData>();
       	byMonth.add(ad.get(0));
       	
       	//make directory for html files
       	//can be modified later to accept input from gui
       	(new File(desktop+"/indexedbymonth")).mkdir();
		File htmlDir  = new File(desktop+"/indexedbymonth");
       	
       	//seperates data by month to create a new page of html for each month
       	//calls the html builder on each months worth of data
       	for(int i = 1; i < ad.size(); i++){
       		if(i == ad.size() - 1)
       		{//edge case, last value would have compared to null; kinda repetitive fix?
       			//System.out.println("a");
       			byMonth.add(ad.get(i));
       			HTMLBuilder htmlb = new HTMLBuilder(byMonth);
       	       	//sets file path to save file as month_year in integer forms
       			String dateTitle = byMonth.get(0).getDate().getMonth()+"_"+byMonth.get(0).getDate().getYear();
       			htmlb.writeHTMLBuilder(htmlDir +"/"+ dateTitle+".html", dateTitle);
       	       	//System.out.println("/Users/aarongirard/Desktop/indexedbymonth2/"+ byMonth.get(0).getDate().getMonth()+"_"+byMonth.get(0).getDate().getYear()+".html");
       	       //time whole thing tool
       			System.out.println("Parsed: " + dateTitle);
       			System.out.println(("This WHOLE PROCESS (YAYA) took.....: " + (System.currentTimeMillis() - start)/1000) +" seconds");
       			return;
       		} 
       		
       		
       		int eye = ad.get(i).getDate().getMonth();
       		int eyePlusOne = ad.get(i + 1).getDate().getMonth();
       		
       		if (  eye != eyePlusOne)
       		{ //if two consecutive objects not in same month, i.e. new month needs to be started
       			//System.out.println(eye);
       			HTMLBuilder htmlb = new HTMLBuilder(byMonth);
       	       	//sets file path to save file as month_year in integer forms
       			String dateTitle = byMonth.get(0).getDate().getMonth()+"_"+byMonth.get(0).getDate().getYear();
       			htmlb.writeHTMLBuilder(htmlDir+"/"+ dateTitle +".html", dateTitle);
       			//System.out.println("/Users/aarongirard/Desktop/indexedbymonth2/"+ byMonth.get(0).getDate().getMonth()+"_"+byMonth.get(0).getDate().getYear()+".html");
       			byMonth.clear();
       	       	byMonth.add(ad.get(i+1));
       	       	System.out.println("Parsed: " + dateTitle);
       		} else {
       			//add value to arrayList b/c in same month as previous object
       			byMonth.add(ad.get(i+1));
       		}
       	}
       	
       
       	//for (AggData s: allThree){
       	//	System.out.println(s.getClass());
       	//}
       	
       	//write out all 3 lists into ordered txt file.      	
       	/*FileWriter output = new FileWriter("/Users/aarongirard/Desktop/Alldatafour.txt");
        BufferedWriter out = new BufferedWriter(output);
        for(AggData s: allThree){
        	try{
                out.write(s + "");
                out.newLine();
                
                
            } catch (IOException e){
                //handle exception
            }
            //System.out.println(s);
         
        }
         //close writer
         out.flush();
         out.close();
    	
    	*/
       	/* to write out csv 
       	FileWriter output = new FileWriter("/Users/aarongirard/Desktop/ccTransactionData.txt");
        BufferedWriter out = new BufferedWriter(output);
        for(CCTransaction s: ccTransaction){
        	try{
        		out.write(s+"");
        		out.newLine();
        	} catch(IOException e){
        		
        	}
        }
        out.flush();
        out.close();*/
    }
}
