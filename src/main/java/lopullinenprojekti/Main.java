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

        get("/etusivu", (req, res) -> {
            HashMap map = new HashMap<>();
            return new ModelAndView(map, "etusivu");
        }, new ThymeleafTemplateEngine());

        Spark.post("/etusivu", (req, res) -> {
            Kysymys kysymys = new Kysymys(req.queryParams("kurssi"), req.queryParams("aihe"), req.queryParams("kysymys"));
            kdao.save(kysymys);

            res.redirect("/etusivu");
            return "";
        });

        get("/kurssit", (req, res) -> {
            HashMap kurssilista = new HashMap<>();
            kurssilista.put("kurssit", kdao.getKurssit());

            return new ModelAndView(kurssilista, "kurssit");
        }, new ThymeleafTemplateEngine());

        get("/kurssit/:kurssi", (req, res) -> {
            HashMap kurssinaiheet = new HashMap<>();
            kurssinaiheet.put("aiheet", kdao.getAiheet(req.params("kurssi")));

            return new ModelAndView(kurssinaiheet, "aiheet");
        }, new ThymeleafTemplateEngine());

        get("/aiheet/:aihe", (req, res) -> {
            HashMap aiheenkysymykset = new HashMap<>();
            aiheenkysymykset.put("kysymykset", kdao.getKysymykset(req.params("aihe")));

            return new ModelAndView(aiheenkysymykset, "kysymykset");
        }, new ThymeleafTemplateEngine());

    }
}
