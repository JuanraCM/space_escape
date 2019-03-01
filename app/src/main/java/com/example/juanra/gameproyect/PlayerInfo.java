package com.example.juanra.gameproyect;

public class PlayerInfo implements Comparable<PlayerInfo> {

    private String alias;
    private int hiscore;

    public PlayerInfo(String alias, int hiscore) {
        this.alias = alias;
        this.hiscore = hiscore;
    }

    public PlayerInfo() {

    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getHiscore() {
        return hiscore;
    }

    public void setHiscore(int hiscore) {
        this.hiscore = hiscore;
    }

    @Override
    public int compareTo(PlayerInfo o) {
        return o.getHiscore() - hiscore;
    }
}
