package hello.dcsms.plak.Utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import hello.dcsms.plak.Frgmnt.DragnDropAbstract;


public class ParcelData implements Parcelable {
	List<DragnDropAbstract.LayoutData> kanan;
	List<DragnDropAbstract.LayoutData> kiri;
	List<DragnDropAbstract.LayoutData> tengah;

	/* everything below here is for implementing Parcelable */

	// 99.9% of the time you can just ignore this
	@Override
	public int describeContents() {
		return 0;
	}

	public ParcelData(List<DragnDropAbstract.LayoutData> kanan, List<DragnDropAbstract.LayoutData> kiri,
			List<DragnDropAbstract.LayoutData> tengah) {
		this.kanan = kanan;
		this.kiri = kiri;
		this.tengah = tengah;
	}

	// write your object's data to the passed-in Parcel
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeList(kanan);
		out.writeList(kiri);
		out.writeList(tengah);
	}

	// this is used to regenerate your object. All Parcelables must have a
	// CREATOR that implements these two methods
	public static final Parcelable.Creator<ParcelData> CREATOR = new Parcelable.Creator<ParcelData>() {
		@Override
		public ParcelData createFromParcel(Parcel in) {
			return new ParcelData(in);
		}

		@Override
		public ParcelData[] newArray(int size) {
			return new ParcelData[size];
		}
	};

	// example constructor that takes a Parcel and gives you an object populated
	// with it's values
	private ParcelData(Parcel in) {
		
	}
}