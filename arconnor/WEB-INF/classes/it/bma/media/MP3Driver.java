package it.bma.media;
// JAVA
import java.io.*;
import java.util.*;
// MP3
import org.farng.mp3.*;
import org.farng.mp3.id3.*;
// BMA
import it.bma.comuni.*;

public class MP3Driver {
	public final String TAG_ARTIST = "Artist";
	public final String TAG_ALBUM = "Album";
	public final String TAG_TITLE = "Title";
	public final String TAG_GENRE = "Genre";
	public final String TAG_TRACK = "Track";
	public final String TAG_YEAR = "Year";
	public final String TAG_COMMENT = "Comment";
	public final String TAG_MUSICID = "MusicId";
	String sFile = "";
	String sMode = "r";
	public MP3Driver() {
		super();
	}
	public MP3Driver(String fileName) {
		super();
		sFile = fileName;
	}
	public void setFile(String fileName) { sFile = fileName; }
	public static boolean isMP3File(String fileName) {
		String ext = fileName.substring(fileName.length()-3);
		if (!ext.equalsIgnoreCase("mp3")) return false;
		File f = new File(fileName);
		return f.exists();
	}
	public String[] getPropertiesNames() {
		return new String[] {TAG_ARTIST, TAG_ALBUM, TAG_TITLE, TAG_GENRE, TAG_TRACK, TAG_YEAR, TAG_COMMENT, TAG_MUSICID};
	}
	private boolean isComplete(Properties props) {
		return isComplete(props, new String[]{TAG_COMMENT, TAG_MUSICID});
	}
	public boolean isComplete(Properties props, String[] excludeTags) {
		String[] tags = getPropertiesNames();
		String temp = "";
		for (int i=0;i<excludeTags.length;i++) {
			temp = temp + "," + excludeTags[i];
		}
		for (int i=0;i<tags.length;i++) {
			if (temp.indexOf(tags[i])<0) {
				String tag = props.getProperty(tags[i]);
				if (tag==null || tag.trim().length()==0) return false;
			}
		}
		return true;
	}
	public Properties getProperties() throws BmaException {
		Properties props = new Properties();
		try {
			AbstractID3 mp3 = getID3v2_4();
			if (mp3!=null) setID3v2Props((AbstractID3v2)mp3, props);
			if (isComplete(props)) return props;
			
			mp3 = getID3v2_3();
			if (mp3!=null) setID3v2Props((AbstractID3v2)mp3, props);
			if (isComplete(props)) return props;
			
			mp3 = getID3v2_2();
			if (mp3!=null) setID3v2Props((AbstractID3v2)mp3, props);
			if (isComplete(props)) return props;
			
			mp3 = getID3v1();
			if (mp3!=null) setID3v1Props((ID3v1_1)mp3, props);
			if (isComplete(props)) return props;
			
			for (int i=0;i<getPropertiesNames().length;i++) {
				String v = props.getProperty(getPropertiesNames()[i]);
				if (v==null) props.setProperty(getPropertiesNames()[i], "");
			}
			return props;
		}
		catch (IOException io) {
			throw new BmaException("IOException", io.getMessage());
		}
	}
	private void setID3v1Props(ID3v1_1 mp3, Properties props) {
		String tag = "";
		tag = mp3.getArtist();
		if (tag!=null) props.setProperty(TAG_ARTIST,  tag);
		tag = mp3.getAlbum();
		if (tag!=null) props.setProperty(TAG_ALBUM,  tag);
		tag = mp3.getTitle();
		if (tag!=null) props.setProperty(TAG_TITLE,  tag);
		HashMap hm = TagConstant.genreIdToString;
		tag = (String)hm.get(new Long((long)mp3.getGenre()));
		if (tag!=null) props.setProperty(TAG_GENRE,  tag);
		tag = Byte.toString(mp3.getTrack());
		while (tag.length()<2) tag = "0" + tag;
		if (tag!=null) props.setProperty(TAG_TRACK,  tag);
		tag = mp3.getYear();
		if (tag!=null) props.setProperty(TAG_YEAR,  tag);
		tag = mp3.getComment();
		if (tag!=null) props.setProperty(TAG_COMMENT, tag);
	}
	private void setID3v2Props(AbstractID3v2 mp3, Properties props) {
		String tag = "";
		AbstractID3v2Frame frame;
		String[] ids = new String[] {"TPE1","TALB","TIT2","TCON","TRCK","TDRC"};
		String[] tgs = new String[] {TAG_ARTIST,TAG_ALBUM,TAG_TITLE,TAG_GENRE,TAG_TRACK,TAG_YEAR};

		for (int i=0;i<ids.length;i++) {
			frame = mp3.getFrame(ids[i]);
			if (frame!=null) {
				tag = ((AbstractFrameBodyTextInformation)frame.getBody()).getText();
				if (tag!=null) props.setProperty(tgs[i], tag);
			}
		}
		frame = mp3.getFrame("COMM");
		if (frame!=null) {
			tag = ((FrameBodyCOMM)frame.getBody()).getText();
			if (tag!=null) props.setProperty(TAG_COMMENT, tag);
		}
		frame = mp3.getFrame("MCDI");
		if (frame!=null) {
			FrameBodyMCDI frameTemp = (FrameBodyMCDI)frame.getBody();
			tag = ((FrameBodyMCDI)frame.getBody()).getDescription();
			if (tag!=null) props.setProperty(TAG_MUSICID, tag);
		}
	}
	private AbstractID3 getID3v1() throws IOException {
		try {
			RandomAccessFile raFile = new RandomAccessFile(sFile, sMode);
			AbstractID3 vFile = new ID3v1_1(raFile);
			return vFile;
		}
		catch (TagException tag) {
			return null;
		}
		catch (NullPointerException np) {
			return null;
		}	
	}
	private AbstractID3 getID3v2_2() throws IOException {
		try {
			RandomAccessFile raFile = new RandomAccessFile(sFile, sMode);
			AbstractID3 vFile = new ID3v2_2(raFile);
			return vFile;
		}
		catch (TagException tag) {
			return null;
		}	
		catch (NullPointerException np) {
			return null;
		}	
	}
	private AbstractID3 getID3v2_3() throws IOException {
		try {
			RandomAccessFile raFile = new RandomAccessFile(sFile, sMode);
			AbstractID3 vFile = new ID3v2_3(raFile);
			return vFile;
		}
		catch (TagException tag) {
			return null;
		}	
		catch (NullPointerException np) {
			return null;
		}	
	}
	private AbstractID3 getID3v2_4() throws IOException {
		try {
			RandomAccessFile raFile = new RandomAccessFile(sFile, sMode);
			AbstractID3 vFile = new ID3v2_4(raFile);
			return vFile;
		}
		catch (TagException tag) {
			return null;
		}	
		catch (NullPointerException np) {
			return null;
		}	
	}
}
