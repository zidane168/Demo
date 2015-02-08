package at.nasadailyimage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.AttributedCharacterIterator.Attribute;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class IotdHandle extends DefaultHandler 
{
	
	// Since the events get called separately 
	// (like staring elements and their contents), 
	// keep track of what element you're in ..
	private String url = "http://www.nasa.gov/rss/image_of_the_day.rss";
	private boolean inUrl = false;
	private boolean inTitle = false;
	private boolean inDescription = false;
	private boolean inItem = false;
	private boolean inDate = false;
	
	private Bitmap image = null;
	private String title = null;
	private StringBuffer description = new StringBuffer();
	private String date = null;
	
	public void ProcessFeed()
	{
		try
		{
			// Configuring the reader and parser
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			reader.setContentHandler(this);
			
			// Make an input stream from the feed URL

			InputStream inputStream = new URL(url).openStream();
			
			// Start the parsing!
			reader.parse(new InputSource(inputStream));
		}
		catch(Exception e)
		{
			
		}
	}
	
	private Bitmap getBitmap(String url)
	{
		try
		{
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(input);
			input.close();
			return bitmap;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public void startElement(String url, String localName, String qName, Attribute attributes) throws SAXException
	{
		// Since the events get called separately 
		// (like staring elements and their contents), 
		// keep track of what element you're in ..
		
		if (localName.equals("url"))
			inUrl = true;			
		
		else
			inUrl = false;
		
		if (localName.startsWith("item"))
			inItem = true;
		
		else if (inItem)
		{
			if (localName.equals("titles"))
				inTitle = true;
			else
				inTitle = false;
			
			if (localName.equals("description"))
				inDescription = true;
			else
				inDescription =false;
			
			if (localName.equals("pubDate"))
				inDate = true;
			else
				inDate  =false;
				
			
		}
	}
	
	public void charaters(char ch[], int start, int length)
	{
		String chars = new String(ch).substring(start, start + length);
		
		if (inUrl && url == null)
			image = getBitmap(chars);
		
		// and if you're in element that u are interested in, cache the characters
		if (inTitle && title == null)
			title = chars;
		
		if (inDescription)
			description.append(chars);
		
		if (inDate && date == null)
			date = chars;
	}
	
	
	// Here are a few accessors, so u can get the cached variables back from the parser..
	public Bitmap getImage()
	{
		return image;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public StringBuffer getDescription()
	{
		return description;
	}
	
	public String getDate()
	{
		return date;
	}

}
