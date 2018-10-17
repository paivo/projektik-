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
public class Vastaus {
    private Integer id;
    private Integer kysymys_id;
    private String vastausteksti;
    private Boolean oikein;
    
    public Vastaus(Integer kysymys_id, String vastausteksti, Boolean oikein){
        this.kysymys_id=kysymys_id;
        this.vastausteksti=vastausteksti;
        this.oikein=oikein;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getKysymysId(){
        return this.kysymys_id;
    }
    
    public String getVastausteksti(){
        return this.vastausteksti;
    }

    public Boolean getOikein() {
        return this.oikein;
    } 
    
    public String getTotuus() {
        if (this.oikein) {
            return "Oikein!";
        }
        return "Väärin!";
    }
}
