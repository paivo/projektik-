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
            return new ModelAndView(map, "etusivu");
        }, new ThymeleafTemplateEngine());

        Spark.post("/", (req, res) -> {
            Kysymys kysymys = new Kysymys(req.queryParams("kurssi"), req.queryParams("aihe"), req.queryParams("kysymys"));
            kdao.save(kysymys);
            res.redirect("/");
            return "";
        });

        get("/kurssit", (req, res) -> {
            if (kdao.getKurssit()==null){
                HashMap palautus = new HashMap();
                palautus.put("palautettava", "Kursseja ei ole luotu.");
                return new ModelAndView(palautus, "eipalautettavaa");
            }
            HashMap kurssilista = new HashMap();
            kurssilista.put("kurssit", kdao.getKurssit());
            return new ModelAndView(kurssilista, "kurssit");
        }, new ThymeleafTemplateEngine());

        get("/kurssit/:kurssi", (req, res) -> {
            List aiheet = kdao.getAiheet(req.params("kurssi"));
            if (aiheet==null){
                HashMap palautus = new HashMap();
                palautus.put("palautettava", "Kurssille ei ole luotu aiheita.");
                return new ModelAndView(palautus, "eipalautettavaa");
            }
            HashMap kurssinaiheet = new HashMap<>();
            kurssinaiheet.put("aiheet", aiheet);
            return new ModelAndView(kurssinaiheet, "aiheet");
        }, new ThymeleafTemplateEngine());

        get("/aiheet/:aihe", (req, res) -> {
            List kysymykset = kdao.getKysymykset(req.params("aihe"));
            if (kysymykset==null){
                HashMap palautus = new HashMap();
                palautus.put("palautettava", "Aiheelle ei ole luotu kysymyksia.");
                return new ModelAndView(palautus, "eipalautettavaa");
            }
            HashMap aiheenkysymykset = new HashMap<>();
            aiheenkysymykset.put("kysymykset", kysymykset);
            return new ModelAndView(aiheenkysymykset, "kysymykset");
        }, new ThymeleafTemplateEngine());
        
    }
}
