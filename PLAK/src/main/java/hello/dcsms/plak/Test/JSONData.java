package hello.dcsms.plak.Test;

import java.util.Arrays;
import java.util.List;

public class JSONData {
	public static String INTEGER = "integer", DIMEN = "dimen", COLOR = "color",
			STRING = "string", BOOL = "bool";

	public static class ListJSONData {
		public List<JSONData> data;
	}

	public static class Items {
		public String type;
		public String[] value;

		@Override
		public String toString() {
			return "Items [value=" + Arrays.toString(value) + "]";
		}

		public Items() {
		}

		public Items set(String type, String[] val) {
			this.type = type;
			this.value = val;
			return this;
		}

		public String getType() {
			return type;
		}

		public String[] getValue() {
			return value;
		}

		public void setType(String type) {
			this.type = type;
		}

		public void setValue(String[] value) {
			this.value = value;
		}
	}

	public JSONData() {
	}

	public String key;

	public String getKey() {
		return key;
	}

	public List<Items> getData() {
		return data;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setData(List<Items> data) {
		this.data = data;
	}

	public List<Items> data;

}
