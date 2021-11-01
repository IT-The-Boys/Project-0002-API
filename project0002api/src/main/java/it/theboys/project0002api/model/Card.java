package it.theboys.project0002api.model;

public class Card{
    
    private String card_id;
    private String card_text;
    private String card_set;
    private String card_image_url;
    private String card_type;

    public void setCard_ID(String card_id){
        this.card_id = card_id;
    }
    public String getCard_ID(){
        return card_id;
    }

    public void setCard_Text(String card_text){
        this.card_text = card_text;
    }
    public String getCard_Text(){
        return card_text;
    }

    public void setCard_Set(String card_set){
        this.card_set = card_set;
    }
    public String getCard_set(){
        return card_set;
    }

    public void setCard_Image_Url(String card_image_url){
        this.card_image_url = card_image_url;
    }
    public String getCard_Image_Url(){
        return card_image_url;
    }

    public void setCard_type(String card_type){
        this.card_type = card_type;
    }
    public String getCard_Type(){
        return card_type;
    }
}
