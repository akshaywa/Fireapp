package com.example.walun.fireapp3;

/**
 * Created by walun on 02-08-2017.
 */

public class Blog {
    private String nameGrp;
    private String imageGrp;

    public Blog(){

    }

    public Blog(String nameGrp, String imageGrp) {
        this.nameGrp = nameGrp;
        this.imageGrp = imageGrp;
    }




    public String getNameGrp(){
        return nameGrp;
    }

    public void setNameGrp(String nameGrp){
        this.nameGrp=nameGrp;
    }


    public String getImageGrp() {
        return imageGrp;
    }

    public void setImageGrp(String imageGrp) {
        this.imageGrp = imageGrp;
    }


}
