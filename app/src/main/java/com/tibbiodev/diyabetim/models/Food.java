package com.tibbiodev.diyabetim.models;

/**
 * Created by User on 4.11.2016.
 */
public class Food {

    private String isim;
    private int glisemikIndeks;
    private int glisemikYuk;

    public Food(){

    }

    public Food(String isim, int glisemikIndeks, int glisemikYuk) {
        this.isim = isim;
        this.glisemikIndeks = glisemikIndeks;
        this.glisemikYuk = glisemikYuk;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public int getGlisemikIndeks() {
        return glisemikIndeks;
    }

    public void setGlisemikIndeks(int glisemikIndeks) {
        this.glisemikIndeks = glisemikIndeks;
    }

    public int getGlisemikYuk() {
        return glisemikYuk;
    }

    public void setGlisemikYuk(int glisemikYuk) {
        this.glisemikYuk = glisemikYuk;
    }
}
