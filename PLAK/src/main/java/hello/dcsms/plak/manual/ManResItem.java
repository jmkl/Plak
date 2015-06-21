package hello.dcsms.plak.manual;

public class ManResItem {

	@Override
	public String toString() {
		return "ManResItem [objek=" + objek + ", tipe=" + tipe + ", catatan="
				+ catatan + ", nilai=" + nilai + "]";
	}

	public String getNamapaket() {
		return namapaket;
	}

	public void setNamapaket(String namapaket) {
		this.namapaket = namapaket;
	}

	public String namapaket, objek, tipe, catatan;

	public String getObjek() {
		return objek;
	}

	public ManResItem setObjek(String fieldtype) {
		this.objek = fieldtype;
		return this;
	}

	public Object nilai;

	public String getTipe() {
		return tipe;
	}

	public String getCatatan() {
		return catatan;
	}

	public Object getNilai() {
		return nilai;
	}

	public ManResItem setTipe(String tipe) {
		this.tipe = tipe;
		return this;
	}

	public ManResItem setCatatan(String catatan) {
		this.catatan = catatan;
		return this;
	}

	public ManResItem setNilai(Object nilai) {
		this.nilai = nilai;
		return this;
	}
}
