/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lopullinenprojekti;

 
/**
 *
 * @author Päivö Niska
 */
public class Kysymys {
    private Integer id;
    private String kurssi;
    private String aihe;
    private String kysymysteksti;
    
    public Kysymys(String kurssi, String aihe, String kysymysteksti){
        this.kurssi=kurssi;
        this.aihe=aihe;
        this.kysymysteksti=kysymysteksti;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getKurssi(){
        return this.kurssi;
    }
    
    public String getAihe(){
        return this.aihe;
    }

    public String getKysymysteksti() {
        return this.kysymysteksti;
    }
    
}
