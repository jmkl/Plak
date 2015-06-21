package hello.dcsms.plak.data;

public class SuperScriptTagData {

	public enum STIPE {
		SUPERSCR, SUBSCR
	}
	private String matches;
	private int posisi, len;
	private STIPE type;
	public String getMatches() {
		return matches;
	}
	public int getPosisi() {
		return posisi;
	}
	public int getLen() {
		return len;
	}
	public void setMatches(String matches) {
		this.matches = matches;
	}
	public void setPosisi(int posisi) {
		this.posisi = posisi;
	}
	public void setLen(int len) {
		this.len = len;
	}
	public STIPE getType() {
		return type;
	}
	public void setType(STIPE type) {
		this.type = type;
	}
	public SuperScriptTagData setData(String m, int pos, int lenght, STIPE type) {
		setMatches(m);
		setPosisi(pos);
		setLen(lenght);
		setType(type);
		return this;
	}

}
