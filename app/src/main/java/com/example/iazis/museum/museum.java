package com.example.iazis.museum;

public class museum {

    private String museum_id;
    private String museum_name;
    private String museum_price;
    private String museum_desc;
    private String museum_open;
    private String museum_close;
    private String regional_id;
    private String museum_foto;
    private String museum_temp;
    private String regional_name;
    private String latitude;
    private String longitude;


    public museum() {
        // TODO Auto-generated constructor stub
    }

    public museum(String museum_id, String museum_name, String museum_price, String museum_desc,
                  String museum_open, String museum_close, String regional_id, String museum_foto, String museum_temp, String regional_name ) {
        super();
        this.museum_id = museum_id;
        this.museum_name = museum_name;
        this.museum_price = museum_price;
        this.museum_desc = museum_desc;
        this.museum_open = museum_open;
        this.museum_close = museum_close;
        this.regional_id = regional_id;
        this.museum_foto = museum_foto;
        this.museum_temp = museum_temp;
        this.regional_name = regional_name;
    }


    public String getmuseum_id() {
        return museum_id;
    }


    public String getmuseum_name() {
        return museum_name;
    }


    public String getmuseum_price() {
        return museum_price;
    }

    public String getmuseum_desc() {
        return museum_desc;
    }

    public String getmuseum_open() {
        return museum_open;
    }

    public String getmuseum_close() {
        return museum_close;
    }

    public String getregional_id() {
        return regional_id;
    }

    public String getmuseum_foto() {
        return museum_foto;
    }

    public String getmuseum_temp() {
        return museum_temp;
    }

    public String getregional_name() {
        return regional_name;
    }

    public void setmuseum_name(String museum_name) {
        this.museum_name = museum_name;
    }

    public void setmuseum_desc(String museum_desc) {
        this.museum_desc = museum_desc;
    }

    public void setregional_name(String regional_name) {
        this.regional_name = regional_name;
    }



}