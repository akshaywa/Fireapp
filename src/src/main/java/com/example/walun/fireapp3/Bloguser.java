package com.example.walun.fireapp3;

/**
 * Created by walun on 23-08-2017.
 */

public class Bloguser{
    private String list;
    private String listimg;

    public Bloguser(){

    }

    public Bloguser(String list, String listimg) {
        this.list= list;
        this.listimg = listimg;
    }




    public String getList(){
        return list;
    }

    public void setList(String list){
        this.list=list;
    }


    public String getListimg() {
        return listimg;
    }

    public void setListimg(String listimg) {
        this.listimg = listimg;
    }


}
