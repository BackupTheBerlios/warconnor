package it.bma.comuni;

import java.util.*;

public class BmaRecordField extends BmaObject {
	protected String name = "";
	protected String type = BMA_SQL_TYP_CHAR;
	protected int len = 0;
	protected int dec = 0;
	protected String value = "";
	
	/** TABELLE ITALIANE: PC Code Page = UNICODE, Host Code Page = 00280/1
	 * I primi 128 elementi UNICODE corrispondono agli elementi della tabella
	 * ASCII.
	 * N.B.: per @ e § è stata usata la codifica Host Code Page = 00500/1 = 0037/1
	 * Tabella di conversione UNICODE to EBCDIC
	 */
	private static int ascii_to_ebcdic[]=
	{ 
		
		  0,  1,  2,  3, 55, 45, 46, 47, 22,  5, 37, 11, 12, 13, 14, 15,  // 0x00
		 16, 17, 18, 19, 60, 61, 50, 38, 24, 25, 63, 39, 28, 29, 30, 31,  // 0x10
		 64, 79,127,177, 91,108, 80,125, 77, 93, 92, 78,107, 96, 75, 97,  // 0x20
		240,241,242,243,244,245,246,247,248,249,122, 94, 76,126,110,111,  // 0x30
		124,193,194,195,196,197,198,199,200,201,209,210,211,212,213,214,  // 0x40
		215,216,217,226,227,228,229,230,231,232,233,144, 72, 81, 95,109,  // 0x50
		221,129,130,131,132,133,134,135,136,137,145,146,147,148,149,150,  // 0x60
		151,152,153,162,163,164,165,166,167,168,169, 68,205, 84, 88,  7,  // 0x70

		111,111,111,111,111,111,111,111,111,111,111,111,111,111,111,111,  // 0x80
		111,111,111,111,111,111,111,111,111,111,111,111,111,111,111,111,  // 0x90
		111,111,111,123,111,111,111,181,111,180,111,111,111,111,159,111,  // 0xa0
		 74,143,111,111,111,111,111,111,111,111,111,111,111,111,111,171,  // 0xb0
		111,111,111,111,111,111,111,111,111,111,111,111,111,111,111,111,  // 0xc0
		111,111,111,111,111,111,111,111,111,111,111,111,111,111,111,111,  // 0xd0
		192,111,111,111,111,111,111,224,208, 90,111,111,161,111,111,111,  // 0xe0
		111,111,106,111,111,111,111,111,111,121,111,111,111,111,111,111   // 0xf0		
	};
	/** 
	 * Tabella di conversione EBCDIC to UNICODE
	 */
	private static int ebcdic_to_ascii[]=
	{
		//0   1   2   3   4   5   6   7   8   9   A   B   C   D   E   F
		  0,  1,  2,  3,156,  9,134,127,151,141,142, 11, 12, 13, 14, 15,  // 0x00
		 16, 17, 18, 19,157,133,  8,135, 24, 25,146,143, 28, 29, 30, 31,  // 0x10
		128,129,130,131,132, 10, 23, 27,136,137,138,139,140,  5,  6,  7,  // 0x20
		144,145, 22,147,148,149,150,  4,152,153,154,155, 20, 21,158, 26,  // 0x30
		 32,160,161,162,163,164,165,166,167,168, 91, 46, 60, 40, 43, 33,  // 0x40
		 38,169,170,171,172,173,174,175,176,177, 93, 36, 42, 41, 59, 94,  // 0x50
		 45, 47,178,179,180,181,182,183,184,185,124, 44, 37, 95, 62, 63,  // 0x60
		186,187,188,189,190,191,192,193,194, 96, 58, 35, 64, 39, 61, 34,  // 0x70
		195, 97, 98, 99,100,101,102,103,104,105,196,197,198,199,200,201,  // 0x80
		202,106,107,108,109,110,111,112,113,114,203,204,205,206,207,208,  // 0x90
		209,126,115,116,117,118,119,120,121,122,210,211,212,213,214,215,  // 0xa0
		216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,  // 0xb0
		123, 65, 66, 67, 68, 69, 70, 71, 72, 73,232,233,234,235,236,237,  // 0xc0
		125, 74, 75, 76, 77, 78, 79, 80, 81, 82,238,239,240,241,242,243,  // 0xd0
		 92,159, 83, 84, 85, 86, 87, 88, 89, 90,244,245,246,247,248,249,  // 0xe0
		 48, 49, 50, 51, 52, 53, 54, 55, 56, 57,250,251,252,253,254,255   // 0xf0
	};
	private static int reverseTo_ebcdic[]=
	{ 
		//0   1   2   3   4   5   6   7   8   9   A   B   C   D   E   F
		  0,  1,  2,  3, 55, 45, 46, 47, 22,  5, 37, 11, 12, 13, 14, 15, 	// 0x00
		 16, 17, 18, 19, 60, 61, 50, 38, 24, 25, 63, 39, 28, 29, 30, 31,  // 0x10
		 64, 79,127,123, 91,108, 80,125, 77, 93, 92, 78,107, 96, 75, 97,  // 0x20
		240,241,242,243,244,245,246,247,248,249,122, 94, 76,126,110,111,  // 0x30
		124,193,194,195,196,197,198,199,200,201,209,210,211,212,213,214,  // 0x40
		215,216,217,226,227,228,229,230,231,232,233, 74,224, 90, 95,109,  // 0x50
		121,129,130,131,132,133,134,135,136,137,145,146,147,148,149,150,  // 0x60
		151,152,153,162,163,164,165,166,167,168,169,192,106,208,161,  7,  // 0x70
		 32, 33, 34, 35, 36, 21,  6, 23, 40, 41, 42, 43, 44,  9, 10, 27,  // 0x80
		 48, 49, 26, 51, 52, 53, 54,  8, 56, 57, 58, 59,  4, 20, 62,225,  // 0x90
		 65, 66, 67, 68, 69, 70, 71, 72, 73, 81, 82, 83, 84, 85, 86, 87,  // 0xa0
		 88, 89, 98, 99,100,101,102,103,104,105,112,113,114,115,116,117,  // 0xb0
		118,119,120,128,138,139,140,141,142,143,144,154,155,156,157,158,  // 0xc0
		159,160,170,171,172,173,174,175,176,177,178,179,180,181,182,183,  // 0xd0
		184,185,186,187,188,189,190,191,202,203,204,205,206,207,218,219,  // 0xe0
		220,221,222,223,234,235,236,237,238,239,250,251,252,253,254,255   // 0xf0
	};
	
public BmaRecordField() {
	super();
}
public BmaRecordField(String name, String type, int len, int decimals) {
	super();
	this.name = name;
	this.type = type;
	this.len = len;
	this.dec = decimals;
}
/**
 * Description:   base 10 int changed to two 4 bit BitSet's
 * @param byte the byte the BitSet's are to be created from
 * @return BitSet[] the BitSet's created from the byte
 * @exception
 */
    private final static BitSet[] bitSetsFromBase10(byte hi) {
       BitSet bits[] = new BitSet[2];
       bits[0] = new BitSet(4);
       bits[1] = new BitSet(4);
       int bb = new Integer(hi).intValue();
       if (bb<0) bb = 256 + bb; 
       int m7 = bb / 128;
       bb = (bb-(m7*128));
       if (m7 > 0) { bits[0].set(3); }
       int m6 = bb / 64;
       bb = (bb-(m6*64));
       if (m6 > 0) { bits[0].set(2); }
       int m5 = bb / 32;
       bb = (bb-(m5*32));
       if (m5 > 0) { bits[0].set(1); }
       int m4 = bb / 16;
       bb = (bb-(m4*16));
       if (m4 > 0) { bits[0].set(0); }
       int m3 = bb / 8;
       bb = (bb-(m3*8));
       if (m3 > 0) { bits[1].set(3); }
       int m2 = bb / 4;
       bb = (bb-(m2*4));
       if (m2 > 0) { bits[1].set(2); }
       int m1 = bb / 2;
       bb = (bb-(m1*2));
       if (m1 > 0) { bits[1].set(1); }
       int m0 = bb;
       if (m0 > 0) { bits[1].set(0); }
       return(bits);
    }
/**
 * Conversione ASCII - EBCDIC del Buffer da inviare in linea
 * Creation date: (28/03/2000 19.35.08)
 * @return byte[]
 * @param in byte[]
 */
private static byte[] convertToASCII(byte[] in) {
	
	byte out[] = new byte[in.length];
	
	for (int i=0; i<in.length; i++) {		
		if (in[i] < 0) out[i] = (byte)ebcdic_to_ascii[256 + in[i]];
		else out[i] = (byte)ebcdic_to_ascii[in[i]];
	}
		
	return out;
}
/**
 * Conversione EBCDIC - ASCII del Buffer ricevuto da linea
 * Creation date: (28/03/2000 19.35.08)
 * @return byte[]
 * @param in byte[]
 */
private static byte[] convertToEBCDIC(byte[] in) {
	
	byte out[] = new byte[in.length];	
	for (int i=0; i<in.length; i++) {
		if (in[i] < 0) out[i] = (byte)ascii_to_ebcdic[256 + in[i]];
		else out[i] = (byte)ascii_to_ebcdic[in[i]];
	}
		
	return out;
}
private String fill (String in) {
	if (in.length()>=len) return in;
	char[] z = new char[len - in.length()];
	if (type.equals(new BmaRecord().PIC_X)) {
		java.util.Arrays.fill(z, '\u0020');
		return in + new String(z);
	}
	else {
		java.util.Arrays.fill(z, '0');
		return new String(z) + in;
	}
		
}
public String getChiave() {
	return name;
}
private final static boolean getSignFromBitSet(BitSet bs) {
  boolean m0 = bs.get(0);
  boolean m1 = bs.get(1);
  boolean m2 = bs.get(2);
  boolean m3 = bs.get(3);

  int bb = 0;
  if (m0) bb = bb + 1;
  if (m1) bb = bb + 2;
  if (m2) bb = bb + 4;
  if (m3) bb = bb + 8;

	return !(bb==11 || bb==13);
}
protected String getXmlTag() {
	return getClassName();
}
/**
 * Description:   bit settings changed to base 10
 * @param BitSet the BitSet the decimal integer is to be created from
 * @return bb the int created from the BitSet 
 * @exception
 */
    private final static int intBase10FromBitSet(BitSet bs) {
    // bit settings changed to base 10
       boolean m0 = bs.get(0);
       boolean m1 = bs.get(1);
       boolean m2 = bs.get(2);
       boolean m3 = bs.get(3);
       
       int bb = 0;
       if (m0) { bb = bb + 1; }
       if (m1) { bb = bb + 2; }
       if (m2) { bb = bb + 4; }
       if (m3) { bb = bb + 8; }        
       return(bb);      
    }
/**
 * Description:   bit settings changed to base 10
 * @param BitSet the BitSet the zoned format decimal integer is to be created from
 * @return bb the int created from the BitSet 
 * @exception
 */
    private final static int intBase10ZonedFromBitSet(BitSet bs) {
       boolean m0 = bs.get(0);
       boolean m1 = bs.get(1);
       boolean m2 = bs.get(2);
       boolean m3 = bs.get(3);
       
       int bb = 0;
       if (m0) { bb = bb + 1; }
       if (m1) { bb = bb + 2; }
       if (m2) { bb = bb + 4; }
       if (m3) { bb = bb + 8; }
       if (bb < 10) {             
           bb = bb + 240;         
       } 
       else if (bb == 10) {     /* positive */
           bb = bb + 240; 
       } 
       else if (bb == 11) {     /* negative */
           bb = bb + 208;         
       } 
       else if (bb == 12) {     /* positive */
           bb = bb + 240; 
       } 
       else if (bb == 13) {     /* negative */
           bb = bb + 208;         
       } 
       else if (bb == 14) {     /* positive */
           bb = bb + 240;          
       } 
       else if (bb == 15) {     /* positive */
           bb = bb + 240;
       }    
       return(bb);      
    }
private String parseComp3(String newValue) {
	String s = newValue;
	boolean positive = true;
	byte[] ebcdicString = revertToEBCDIC(s.getBytes());
	byte[] ebcdicOut = new byte[ebcdicString.length * 2-1];
	for (int i = 0; i < ebcdicString.length; i++) {
		byte ebcdicByte = ebcdicString[i];
		BitSet ebcdicBits[] = bitSetsFromBase10(ebcdicByte);
		int b0 = intBase10FromBitSet(ebcdicBits[0]) + 240;
		ebcdicOut[i*2] = (byte)b0;
		if (i==ebcdicString.length-1) {
			positive = getSignFromBitSet(ebcdicBits[1]);
		}
		else {
			int b1 = intBase10FromBitSet(ebcdicBits[1]) + 240;
			ebcdicOut[i*2+1] = (byte)b1;
		}
	}
	ebcdicOut = convertToASCII(ebcdicOut);
	String tmp = new String(ebcdicOut);
	if (positive) return tmp;
	else return "-" + tmp;
//	return tmp.substring(tmp.length()-1) + tmp.substring(0,tmp.length()-1);
}
public String parseValue(String newValue) {
	BmaRecord r = new BmaRecord();
	if (type.equals(r.PIC_X)) return newValue.trim();
	String tmp = newValue.trim();
	if (tmp.length()<len) {
		char[] z = new char[len - tmp.length()];
		java.util.Arrays.fill(z, '0');
		tmp = new String(z) + tmp;
	}

	if (type.equals(r.PIC_S)) {
		String v = "{ABCDEFGHI}JKLMNOPQR";
		String s = tmp.substring(tmp.length()-1);
		int i = v.indexOf(s);
		if (i<0) tmp = newValue;
		else if (i<=9) tmp = tmp.substring(0,tmp.length()-1) + Integer.toString(i);
		else tmp = "-" + tmp.substring(0,tmp.length()-1) + Integer.toString(i-10);
	}
	else if (type.equals(r.PIC_COMP3)) {
		tmp = parseComp3(tmp);
	}
	if (dec>0) {
		tmp = tmp.substring(0, tmp.length()-dec) + "." + tmp.substring(tmp.length()-dec);
		double d = Double.parseDouble(tmp);
		return Double.toString(d);
	}
	else {
		long d = Long.parseLong(tmp);
		return Long.toString(d);
	}
}
private static byte[] revertToEBCDIC(byte[] in) {
	
	byte out[] = new byte[in.length];	
	for (int i=0; i<in.length; i++) {
		if (in[i] < 0) out[i] = (byte)reverseTo_ebcdic[256 + in[i]];
		else out[i] = (byte)reverseTo_ebcdic[in[i]];
	}
		
	return out;
}
public void setValue(String newValue) throws BmaException {
	BmaRecord r = new BmaRecord();
	String tmp = newValue.trim();

	if (type.equals(r.PIC_X)) {
		if (tmp.length()>len) {
			throw new BmaException(BMA_ERR_IO__WRITE,"Valore troppo lungo per il campo: " + name,"", this);
		}
		value = fill(tmp);
	}
	else {
		if (tmp.trim().length()==0) tmp = "0";
		if (dec>0) {
			double d = Double.parseDouble(tmp);
			d = d * java.lang.Math.pow(10, dec);
			tmp = Long.toString((long)d);
		}
		long d = Long.parseLong(tmp);
		if (type.equals(r.PIC_9)) {
			if (d<0) throw new BmaException(BMA_ERR_IO__WRITE, "Valore negativo non ammesso per il campo: " + name, "", this);
			value = fill(Long.toString(d));
		}
		else if (type.equals(r.PIC_S)) {
			String v = "{ABCDEFGHI}JKLMNOPQR";
			int pos = 0;
			if (d<0) pos = 10;
			tmp = Long.toString(java.lang.Math.abs(d));
			pos = pos + Integer.parseInt(tmp.substring(tmp.length()-1));
			tmp = tmp.substring(0, tmp.length()-1) + v.charAt(pos);
			value = fill(tmp);
		}
		else if (type.equals(r.PIC_COMP3)) {
			throw new BmaException(BMA_ERR_IO__WRITE, "Formato non previsto per il campo: " + name,"",this);
		}
		else {
			throw new BmaException(BMA_ERR_IO__WRITE, "Formato non previsto per il campo: " + name,"",this);
		}
	}
}
}
