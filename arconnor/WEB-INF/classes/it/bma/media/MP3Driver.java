package it.bma.media;
// JAVA
import java.io.*;
import java.util.*;
// MP3
import org.farng.mp3.*;
import org.farng.mp3.id3.*;

public class MP3Driver {
	public final String TAG_ARTIST = "Artist";
	public final String TAG_ALBUM = "Album";
	public final String TAG_TITLE = "Title";
	public final String TAG_GENRE = "Genre";
	public final String TAG_TRACK = "Track";
	public final String TAG_YEAR = "Year";
	public final String TAG_COMMENT = "Comment";
	String sFile = "";
	String sMode = "r";
	public MP3Driver() {
		super();
	}
	public MP3Driver(String fileName) {
		super();
		sFile = fileName;
	}
	public boolean isComplete(Properties props) {
		String tag = "";
		tag = props.getProperty(TAG_ARTIST);
		if (tag==null || tag.trim().length()==0) return false;
		tag = props.getProperty(TAG_ALBUM);
		if (tag==null || tag.trim().length()==0) return false;
		tag = props.getProperty(TAG_TITLE);
		if (tag==null || tag.trim().length()==0) return false;
		tag = props.getProperty(TAG_GENRE);
		if (tag==null || tag.trim().length()==0) return false;
		tag = props.getProperty(TAG_TRACK);
		if (tag==null || tag.trim().length()==0) return false;
		tag = props.getProperty(TAG_YEAR);
		if (tag==null || tag.trim().length()==0) return false;
		return true;
	}
	public Properties getProperties() {
		Properties props = new Properties();
		try {
			AbstractID3 mp3 = getID3v1();
			if (mp3!=null) setID3v1Props((ID3v1_1)mp3, props);
			if (isComplete(props)) return props;
			
			mp3 = getID3v2_2();
			if (mp3!=null) setID3v2Props((AbstractID3v2)mp3, props);
			if (isComplete(props)) return props;
			
			mp3 = getID3v2_3();
			if (mp3!=null) setID3v2Props((AbstractID3v2)mp3, props);
			if (isComplete(props)) return props;
			
			mp3 = getID3v2_4();
			if (mp3!=null) setID3v2Props((AbstractID3v2)mp3, props);
			if (isComplete(props)) return props;
			
			return props;
		}
		catch (IOException io) {
			return props;
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
	}
}
