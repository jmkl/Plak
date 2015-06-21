package hello.dcsms.plak.ss;

import java.io.Serializable;

/**
 * Created by jmkl on 5/1/2015.
 */
public class SSFrameData implements Serializable {
    public String ssframe;
    public int ss_x;
    public int ss_y;
    public frameData ssframedata;

    public int getSs_y() {
        return ss_y;
    }

    public void setSs_y(int ss_y) {
        this.ss_y = ss_y;
    }

    public int getSs_x() {
        return ss_x;
    }

    public void setSs_x(int ss_x) {
        this.ss_x = ss_x;
    }


    @Override
    public String toString() {
        return "SSFrameData{" +
                "ssframe='" + ssframe + '\'' +
                ", ssframedata=" + ssframedata +
                '}';
    }

    public String getSsframe() {
        return ssframe;
    }

    public void setSsframe(String ssframe) {
        this.ssframe = ssframe;
    }

    public frameData getSsframedata() {
        return ssframedata;
    }

    /**
     * Frame
     * Overly
     * Background
     * Width
     * Height
     * X
     * Y
     * POS_X
     * pos_Y
     * @author jmkl
     */
    public void setSsframedata(String frame, String background, int w, int h, int x, int y,int ssx,int ssy, Overlay... overlay) {
        ssframedata = new frameData(frame, background, w, h, x, y,ssx,ssy, overlay);
    }

    public static class Overlay implements Serializable{
        public String overlay_img;
        public int overlay_x,overlay_y;
        public Overlay(String img, int x, int y){
            overlay_img=img;
            overlay_x=x;
            overlay_y=y;
        }

    }

    public static class frameData implements Serializable {

        public String frame;
        public Overlay[] overlay;
        public String background;
        public int bg_width;
        public int bg_height;
        public int frame_x;
        public int frame_y;

        public int getPos_ss_x() {
            return pos_ss_x;
        }

        public void setPos_ss_x(int pos_ss_x) {
            this.pos_ss_x = pos_ss_x;
        }

        public int getPos_ss_y() {
            return pos_ss_y;
        }

        public void setPos_ss_y(int post_ss_y) {
            this.pos_ss_y = post_ss_y;
        }

        public int pos_ss_x;
        public int pos_ss_y;

        public frameData(String frame, String background, int w, int h, int x, int y,int ssx,int ssy, Overlay... overlay) {
            setFrame(frame);
            setOverlay(overlay);
            setBackground(background);
            setBg_width(w);
            setBg_height(h);
            setFrame_x(x);
            setFrame_y(y);
            setPos_ss_x(ssx);
            setPos_ss_y(ssy);
        }

        public String getFrame() {
            return frame;
        }

        public void setFrame(String frame) {
            this.frame = frame;
        }

        public Overlay[] getOverlay() {
            return overlay;
        }

        public void setOverlay(Overlay[] overlay) {
            this.overlay = overlay;
        }

        public String getBackground() {
            return background;
        }

        public void setBackground(String background) {
            this.background = background;
        }

        public int getBg_width() {
            return bg_width;
        }

        public void setBg_width(int bg_width) {
            this.bg_width = bg_width;
        }

        public int getBg_height() {
            return bg_height;
        }

        public void setBg_height(int bg_height) {
            this.bg_height = bg_height;
        }

        public int getFrame_x() {
            return frame_x;
        }

        public void setFrame_x(int frame_x) {
            this.frame_x = frame_x;
        }

        public int getFrame_y() {
            return frame_y;
        }

        public void setFrame_y(int frame_y) {
            this.frame_y = frame_y;
        }
    }

}
