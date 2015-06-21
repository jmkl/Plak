package hello.dcsms.plak.manual;

import java.util.ArrayList;
import java.util.List;

public class ListManResItem {
	public String key;
	public List<ManResItem> data;

	public ListManResItem() {
		data = new ArrayList<ManResItem>();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void add(ManResItem item) {
		data.add(item);
	}

	public List<ManResItem> getData() {
		return data;
	}

	public void setData(List<ManResItem> data) {
		this.data = data;
	}

}
