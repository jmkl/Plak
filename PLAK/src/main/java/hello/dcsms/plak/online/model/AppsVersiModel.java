package hello.dcsms.plak.online.model;

import java.io.Serializable;

/**
 * Created by jmkl on 5/19/2015.
 */
public class AppsVersiModel implements Serializable {
    public int versi;
    public String link;

    @Override
    public String toString() {
        return String.format("versi : %d\nlink : %s",getVersi(),getLink());
    }

    public int getVersi() {
        return versi;
    }

    public void setVersi(int versi) {
        this.versi = versi;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
