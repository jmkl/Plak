package hello.dcsms.plak.manual;

import hello.dcsms.plak.widget.AddItemDialog.ITEMTYPE;

public class ManualItemData {
	public static String TDIMEN = "dimen", TSTRING = "string",
			TINT = "integer", TBOOL = "bool", TCOL = "color";

	public static String getTypeFromDialog(ITEMTYPE type) {
		if (type == ITEMTYPE.DIMEN)
			return "dimen";
		else if (type == ITEMTYPE.STRING)
			return "string";
		else if (type == ITEMTYPE.INTEGER)
			return "integer";
		else if (type == ITEMTYPE.BOOLEAN)
			return "bool";
		else if (type == ITEMTYPE.COLOR)
			return "color";
		else
			return null;
	}

	public static ITEMTYPE getTypeFromDATA(ManualItemData md) {
		if (md.getTipe().equals("dimen"))
			return ITEMTYPE.DIMEN;
		else if (md.getTipe().equals("string"))
			return ITEMTYPE.STRING;
		else if (md.getTipe().equals("integer"))
			return ITEMTYPE.INTEGER;
		else if (md.getTipe().equals("bool"))
			return ITEMTYPE.BOOLEAN;
		else if (md.getTipe().equals("color"))
			return ITEMTYPE.COLOR;
		else
			return null;

	}

	
	public String nama_field, tipe, nilai, catatan, namapaket;
	public long cek;
	public long id;
	
	public String getCatatan() {
		return catatan;
	}

	public String getNamaPaket() {
		return namapaket;
	}

	public void setCatatan(String itemDesc) {
		catatan = itemDesc;
	}

	public void setNamaPaket(String itemPackage) {
		namapaket = itemPackage;
	}

	public String getNamaField() {
		return nama_field;
	}

	public String getTipe() {
		return tipe;
	}

	public String getNilai() {
		return nilai;
	}

	public long isCek() {
		return cek;
	}

	public void setCek(long iscek) {
		cek = iscek;
	}

	public long getId() {
		return id;
	}

	public void setNamaField(String item) {
		nama_field = item;
	}

	public void setItemType(String itemType) {
		tipe = itemType;
	}

	public void setItemValue(String itemValue) {
		nilai = itemValue;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ManualItemData [Item=" + nama_field + ", ItemType=" + tipe
				+ ", ItemValue=" + nilai + ", ItemDesc=" + catatan
				+ ", ItemPackage=" + namapaket + ", ItemBool=" + cek
				+ ", id=" + id + "]";
	}

}
