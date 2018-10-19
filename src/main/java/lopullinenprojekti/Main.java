package lopullinenprojekti;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import java.util.List;
import java.util.Map;
import lopullinenprojekti.Database;
import lopullinenprojekti.Kysymys;
import lopullinenprojekti.KysymysDao;
import lopullinenprojekti.VastausDao;

/**
 *
 * @author Päivö Niska
 */
public class Main {

    public static void main(String[] args) throws Exception {
        //if (System.getenv("PORT") != null) {
        //   Spark.port(Integer.valueOf(System.getenv("PORT")));
        //}
        File tiedosto = new File("db", "taulu.db");
        Database database = new Database("jdbc:sqlite:" + tiedosto.getAbsolutePath());

        VastausDao vdao = new VastausDao(database);
        KysymysDao kdao = new KysymysDao(database);
        
        

    

    get( 
        "/", (req, res) -> {
            HashMap map = new HashMap<>();
        return new ModelAndView(map, "index");
    }
     ,

    new ThymeleafTemplateEngine()

    );
        
    Spark.post ( 
        "/", (req, res) -> {
            String kurssi = req.queryParams("kurssi");
        String aihe = req.queryParams("aihe");
        String kysymysteksti = req.queryParams("kysymys");
        kdao.save(new Kysymys(kurssi, aihe, kysymysteksti));
        res.redirect("/");
        return "";
    }

    );

    get( 
        "/kurssit", (req, res) -> {
            HashMap kysymykset = new HashMap();
        kysymykset.put("kysymykset", kdao.findKysymysPerKurssi());
        return new ModelAndView(kysymykset, "kurssit");
    }
     ,

    new ThymeleafTemplateEngine()

    );

    get( 
        "/aiheet/:id", (req, res) -> {
            Kysymys kysymys = kdao.findOne(Integer.parseInt(req.params("id")));
        HashMap kysymykset = new HashMap();
        kysymykset.put("kysymykset", kdao.findKysymysPerAihe(kysymys.getKurssi()));
        return new ModelAndView(kysymykset, "aiheet");
    }
     ,

    new ThymeleafTemplateEngine()

    );

    get( 
        "/kysymykset/:id", (req, res) -> {
            Kysymys kysymys = kdao.findOne(Integer.parseInt(req.params("id")));
        HashMap kysymykset = new HashMap();
        kysymykset.put("kysymykset", kdao.getKysymykset(kysymys.getKurssi(), kysymys.getAihe()));
        return new ModelAndView(kysymykset, "kysymykset");
    }
     ,

    new ThymeleafTemplateEngine()

    );
        
        //////////////////////////////kysymysten poistaminen
    Spark.post ( 
        "/delete/:id", (req, res) -> {
            vdao.deleteKysymyksenVastaukset(Integer.parseInt(":id"));
        kdao.delete(Integer.parseInt(":id"));
        res.redirect("/");
        return "";
    }

    );
        
        ////////////////////////////////
        
    get( 
        "/vastaukset/:id", (req, res) -> {
            Kysymys kysymys = kdao.findOne(Integer.parseInt(":id"));
        HashMap vastaukset = new HashMap();
        vastaukset.put("vastaukset", vdao.getKysymyksenVastaukset(kysymys));
        vastaukset.put("kysymys", kysymys);
        return new ModelAndView(vastaukset, "vastaukset");
    }
     ,

    new ThymeleafTemplateEngine()

    );
        
    get( 
        "/oikein/:id", (req, res) -> {
            HashMap vastaus = new HashMap();
        vastaus.put("vastaus", vdao.findOne(Integer.parseInt(":id")));
        return new ModelAndView(vastaus, "oikein");
    }
     ,

    new ThymeleafTemplateEngine()

);
    }
}
