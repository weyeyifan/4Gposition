package com.position4g.model;

/**
 * @createAuthor zfb
 * @createTime 2017/4/5${Time}
 * @describe ${TODO}
 */

public class LocationBean {
    public int getPaNumber() {
        return paNumber;
    }

    public void setPaNumber(int paNumber) {
        this.paNumber = paNumber;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getWaveRadio() {
        return waveRadio;
    }

    public void setWaveRadio(int waveRadio) {
        this.waveRadio = waveRadio;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    int paNumber;
    int power;
    int waveRadio;
    int temp;
}
