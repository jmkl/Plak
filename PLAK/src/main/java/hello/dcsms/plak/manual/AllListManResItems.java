package hello.dcsms.plak.manual;

import java.util.ArrayList;
import java.util.List;

public class AllListManResItems {
	private List<ListManResItem> data;

	public AllListManResItems() {
		data = new ArrayList<ListManResItem>();
	}

	public void addData(ListManResItem data) {
		this.data.add(data);
	}

	public List<ListManResItem> getData() {
		return data;
	}

	public void setData(List<ListManResItem> data) {
		this.data = data;
	}

}
