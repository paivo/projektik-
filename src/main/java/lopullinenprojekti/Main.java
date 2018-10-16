package lopullinenprojekti;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.util.HashMap;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Päivö Niska
 */
public class Main {

    public static void main(String[] args) throws Exception {
        if (System.getenv("PORT") != null) {
           Spark.port(Integer.valueOf(System.getenv("PORT")));
        }
        File tiedosto = new File("db", "taulu.db");
        Database database = new Database("jdbc:sqlite:" + tiedosto.getAbsolutePath());

        VastausDao vdao = new VastausDao(database);
        KysymysDao kdao = new KysymysDao(database);
        
        
        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        Spark.post("/", (req, res) -> {
            String kurssi = req.queryParams("kurssi");
            String aihe = req.queryParams("aihe");
            String kysymysteksti = req.queryParams("kysymys");
            if (!(kurssi.isEmpty() || aihe.isEmpty() || kysymysteksti.isEmpty())) {
                //Varmistetaan että ei saada tyhjää syötettä.
                Kysymys kysymys = new Kysymys(kurssi, aihe, kysymysteksti);
                kdao.save(kysymys);
            }
            res.redirect("/");
            return "";
        });

        get("/kurssit", (req, res) -> {
            HashMap kurssilista = new HashMap();
            kurssilista.put("kurssit", kdao.getKurssit());
            return new ModelAndView(kurssilista, "kurssit");
        }, new ThymeleafTemplateEngine());

        get("/kurssit/:kurssi", (req, res) -> {
            List aiheet = kdao.getAiheet(":kurssi");
            HashMap kurssinaiheet = new HashMap<>();
            kurssinaiheet.put("aiheet", aiheet);
            return new ModelAndView(kurssinaiheet, "aiheet");
        }, new ThymeleafTemplateEngine());

        get("/aiheet/:aihe", (req, res) -> {
            List kysymykset = kdao.getKysymykset(":aihe");
            HashMap aiheenkysymykset = new HashMap();
            aiheenkysymykset.put("kysymykset", kysymykset);
            return new ModelAndView(aiheenkysymykset, "kysymykset");
        }, new ThymeleafTemplateEngine());
        
        get("/kysymykset/:kysymys", (req, res) -> {
            List kysymykset = vdao.getKysymyksenVastaukset(":kysymys");
            HashMap kaikkikysymykset = new HashMap();
            kaikkikysymykset.put("kysymykset", kysymykset);
            return new ModelAndView(kaikkikysymykset, "vastaukset");
        }, new ThymeleafTemplateEngine());
        
    }
}
