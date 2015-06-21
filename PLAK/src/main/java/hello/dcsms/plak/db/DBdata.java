package hello.dcsms.plak.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBdata {
    public long IDs;
    public String Kiri, Kanan, Tengah;
    public long Tanggal;
    public byte[] Preview;

    @Override
    public String toString() {
        return String.format("kanan : %s kiri : %s tengah : %s id : %d", Kanan,
                Kiri, Tengah, IDs);
    }

    public long getTanggal() {
        return Tanggal;
    }

    public void setTanggal(long tanggal) {
        Tanggal = tanggal;
    }

    public byte[] getPreview() {
        return Preview;
    }

    public void setPreview(byte[] preview) {
        Preview = preview;
    }

    public static List<String> str2List(String data) {
        if (data == null)
            return new ArrayList<String>();
        String[] x = data.split(",");
        return Arrays.asList(x);
    }

    public static String List2str(List<String> data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            if (i == data.size() - 1)
                sb.append(data.get(i));
            else
                sb.append(data.get(i) + ",");
        }
        return sb.toString();
    }

    public long getIDs() {
        return IDs;
    }

    public String getKiri() {
        return Kiri;
    }

    public String getKanan() {
        return Kanan;
    }

    public String getTengah() {
        return Tengah;
    }

    public void setIDs(long iDs) {
        IDs = iDs;
    }

    public void setKiri(String kiri) {
        Kiri = kiri;
    }

    public void setKanan(String kanan) {
        Kanan = kanan;
    }

    public void setTengah(String tengah) {
        Tengah = tengah;
    }

}