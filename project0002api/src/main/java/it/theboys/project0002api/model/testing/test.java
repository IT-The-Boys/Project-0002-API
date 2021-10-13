package it.theboys.project0002api.model.testing;

public class test {
    
    private final long id;
    private final String content;

    public test(long id, String content){
        this.id = id;
        this.content = content;
    }

    public long getId(){
        return id;
    }
    public String getContent(){
        return content;
    }
}
