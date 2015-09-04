package aggregate.data;
/*given an image, this class will compress the it
*methods will also be provided to write an to a specified folder
*this method will determine a name depending on the date and time of the photo (year/month/milisecondValue)
*
*THIS PROCESS TAKES A LONG ASS TIME FOR A LOT OF PICTURES...SLOW TO MASS COMPRESS...
*/
import javax.imageio.*;
import javax.imageio.stream.FileImageOutputStream;

import java.awt.image.BufferedImage;
import java.io.*;

public class ImageProcessing 
{
	//class variables
	ImageWriter jpgWriter;
	ImageWriteParam jpgWriteParam;
	
	//constructor (pasted in javadoc to help understand
	public ImageProcessing()
	{
		//Returns an Iterator containing all currently registered ImageWriters that claim to be able to encode the named format.
		jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next(); 
		
		//Returns a new ImageWriteParam object of the appropriate type for this file format containing 
		//default values, that is, those values that would be used if no ImageWriteParam object were specified.
		jpgWriteParam = jpgWriter.getDefaultWriteParam();
		
		//Specifies whether compression is to be performed, and if so how compression parameters are to be determined.
		// Mode.Explicit: Compress using the compression type and quality settings specified in this ImageWriteParam.
		jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		
		jpgWriteParam.setCompressionQuality(.4f); //set image quality here; I tested it and .4f seems like gooD break point (goldilocks)
		//perhaps .7 is better because value really levels off afterwards but can't really notice diff until after .4 and I'm prioritizing 
		//space as more import to save
	}
	

	//the file path will be determined before hand and then passed to this method
	//pass buffered image
	public void processImage(File file, BufferedImage image) throws IOException
	{
		//set name of file to be written
		FileImageOutputStream fileImageOutputStream = new FileImageOutputStream(file);
		jpgWriter.setOutput(fileImageOutputStream);
		//setup image for compression
		IIOImage outputImage = new IIOImage(image, null, null);
		//write out image
		jpgWriter.write(null, outputImage, jpgWriteParam);
		
	}
	
	public void disposeWriter()
	{
		jpgWriter.dispose();
	}
}