package com.fndsea.main;

public class Fishes {
    String name;
    String loc;
    long date;
    long sal;
    double catchVal;
    double wti;
    double wTmp;
    double tmp;
    double atm;
    double ws;
    double waveH;
    double waveDH;
    double wP;
    double humit;
    double frozen;
    double fresh;
    double live;

    @Override
    public String toString() {
        return "Fishes{" +
                "name='" + name + '\'' +
                ", loc='" + loc + '\'' +
                ", date=" + date +
                ", sal=" + sal +
                ", catchVal=" + catchVal +
                ", wti=" + wti +
                ", wTmp=" + wTmp +
                ", tmp=" + tmp +
                ", atm=" + atm +
                ", ws=" + ws +
                ", waveH=" + waveH +
                ", waveDH=" + waveDH +
                ", wP=" + wP +
                ", humit=" + humit +
                ", frozen=" + frozen +
                ", fresh=" + fresh +
                ", live=" + live +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getSal() {
        return sal;
    }

    public void setSal(long sal) {
        this.sal = sal;
    }

    public double getCatchVal() {
        return catchVal;
    }

    public void setCatchVal(double catchVal) {
        this.catchVal = catchVal;
    }

    public double getWti() {
        return wti;
    }

    public void setWti(double wti) {
        this.wti = wti;
    }

    public double getwTmp() {
        return wTmp;
    }

    public void setwTmp(double wTmp) {
        this.wTmp = wTmp;
    }

    public double getTmp() {
        return tmp;
    }

    public void setTmp(double tmp) {
        this.tmp = tmp;
    }

    public double getAtm() {
        return atm;
    }

    public void setAtm(double atm) {
        this.atm = atm;
    }

    public double getWs() {
        return ws;
    }

    public void setWs(double ws) {
        this.ws = ws;
    }

    public double getWaveH() {
        return waveH;
    }

    public void setWaveH(double waveH) {
        this.waveH = waveH;
    }

    public double getWaveDH() {
        return waveDH;
    }

    public void setWaveDH(double waveDH) {
        this.waveDH = waveDH;
    }

    public double getwP() {
        return wP;
    }

    public void setwP(double wP) {
        this.wP = wP;
    }

    public double getHumit() {
        return humit;
    }

    public void setHumit(double humit) {
        this.humit = humit;
    }

    public double getFrozen() {
        return frozen;
    }

    public void setFrozen(double frozen) {
        this.frozen = frozen;
    }

    public double getFresh() {
        return fresh;
    }

    public void setFresh(double fresh) {
        this.fresh = fresh;
    }

    public double getLive() {
        return live;
    }

    public void setLive(double live) {
        this.live = live;
    }
}
