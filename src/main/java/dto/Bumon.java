package dto;

public class Bumon {
    private String cdBumon;

    private String nmBumon;

    public Bumon() {}

    public Bumon(String cdBumon, String nmBumon) {
        super();
        this.cdBumon = cdBumon;
        this.nmBumon = nmBumon;
    }

    public String getCdBumon() {
        return cdBumon;
    }

    public void setCdBumon(String cdBumon) {
        this.cdBumon = cdBumon;
    }

    public String getNmBumon() {
        return nmBumon;
    }

    public void setNmBumon(String nmBumon) {
        this.nmBumon = nmBumon;
    }
}
