package it.bma.media;
// Java
import java.io.*;
import java.text.ParseException;
import java.util.*;
// Antelmann
import com.antelmann.cddb.*;
// MP3
import org.farng.mp3.*;
import org.farng.mp3.id3.*;
public class FreeDbTest {
	public FreeDbTest() {
	}
	public static void main(String[] args) {
		try {
			File dir = new File("D:\\A-Buena Vista Social Club - Buena Vista Social Club");
			File[] list = dir.listFiles();
			int nTrack = 0;
			int nSize = 0;
			int[] offsets = new int[100];
			for (int i=0; i<list.length;i++) {
				String ext = list[i].getName().substring(list[i].getName().length()-3);
				if (ext.equalsIgnoreCase("mp3")) {
					ID3v1 mp3 = new ID3v1(new RandomAccessFile(list[i],"r"));
					nTrack++;
					int size = mp3.getSize();
					if (i==0) offsets[i]=0;
					else offsets[i] = offsets[i-1] + size;
					nSize = nSize + size;
				}
			}
			int[] offsets2 = new int[nTrack];
			for (int i=0; i<nTrack;i++) {
				offsets2[i]=offsets[i];
			}
			String id = "aabbcc";
			CDID objId = new CDID(id, offsets2, nSize);
			FreeDB server = new FreeDB();
			boolean exit = server.verifyDiscID(objId);
			
			String myId = objId.getDiscID();
			System.out.println(myId);
			
/*			
			FreeDB cd = new FreeDB();
			CDDBRecord cdRec = new CDDBRecord("rock", "b70f380d", "");
			CDInfo cdInfo = cd.readCDInfo(cdRec);
			String xmcd = cdInfo.getXmcdContent();
			CDDBXmcdParser cdParser = new CDDBXmcdParser(xmcd);
			
			System.out.println(cdParser.readArtist());
			System.out.println(cdParser.readCDTitle());
			System.out.println(cdParser.readTitle());
			System.out.println(cdParser.readGenre());
			System.out.println(cdParser.readYear());
			int n = cdParser.readNumberOfTracks();
			for (int i=0;i<n;i++) {
				System.out.print("Track: " + Integer.toString(i));
				System.out.print("Artist=" + cdParser.readTrackArtist(i));
				System.out.print("Title =" + cdParser.readTrackTitle(i));
				System.out.println("");				
			}
*/			
/*			
			Properties cdProps = cdInfo.getProperties();
			Enumeration e = cdProps.keys();
			while (e.hasMoreElements()) {
				String k = (String)e.nextElement();
				String v = cdProps.getProperty(k);
				System.out.println("Property: " + k + "=" + v);			
			}
*/
//			System.out.println(cdInfo.getXmcdContent());
			
			System.out.println("OK");
		}
		catch (IOException io) {
			System.out.println(io.getMessage());
		}
/*
		catch (ParseException pe) {
			System.out.println(pe.getMessage());
		}
*/
		catch (TagNotFoundException tfe) {
			System.out.println(tfe.getMessage());
		}
	}
	
}
