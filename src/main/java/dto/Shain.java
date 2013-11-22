package dto;

import java.util.Date;

public class Shain {
    private String cdShain;

    private String nmShimei;

    private Date ymdNyuusha;

    private String cdBumon;

    public Shain() {}

    public Shain(String cdShain, String nmShimei, Date ymdNyuusha,
            String cdBumon) {
        super();
        this.cdShain = cdShain;
        this.nmShimei = nmShimei;
        this.ymdNyuusha = ymdNyuusha;
        this.cdBumon = cdBumon;
    }

    public String getCdBumon() {
        return cdBumon;
    }

    public void setCdBumon(String cdBumon) {
        this.cdBumon = cdBumon;
    }

    public String getCdShain() {
        return cdShain;
    }

    public void setCdShain(String cdShain) {
        this.cdShain = cdShain;
    }

    public String getNmShimei() {
        return nmShimei;
    }

    public void setNmShimei(String nmShamei) {
        this.nmShimei = nmShamei;
    }

    public Date getYmdNyuusha() {
        return ymdNyuusha;
    }

    public void setYmdNyuusha(Date ymdNyuusha) {
        this.ymdNyuusha = ymdNyuusha;
    }
}
