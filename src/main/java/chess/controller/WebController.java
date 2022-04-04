package chess.controller;

import chess.domain.Team;
import spark.ModelAndView;
import spark.Request;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class WebController {

    private final ChessController chessController = new ChessController();

    public void run() {
        staticFileLocation("/static");

        get("/", (req, res) -> {
            Map<String, Object> model = setModel();
            return render(model, "index.html");
        });

        post("/move", (req, res) -> {
            processMove(req);
            res.redirect("/");
            return null;
        });

        post("/start", (req, res) -> {
            chessController.start();
            res.redirect("/");
            return null;
        });

        exception(Exception.class, (exception, request, response) -> {
            Map<String, Object> model = setModel();
            model.put("error", exception.getMessage());
            response.body(render(model, "index.html"));
        });
    }

    private Map<String, Object> setModel() {
        Map<String, Object> model = new HashMap<>();
        addBoardInformation(model);
        addTurnInformation(model);
        addPlayingInformation(model);
        return model;
    }

    private void addBoardInformation(Map<String, Object> model) {
        model.put("pieces", chessController.getCurrentImages());
    }

    private void addTurnInformation(Map<String, Object> model) {
        if (!chessController.getCurrentTeam().isNeutrality(Team.NEUTRALITY)) {
            model.put("team", chessController.getCurrentTeam());
        }
    }

    private void addPlayingInformation(Map<String, Object> model) {
        if (chessController.isPlaying()) {
            model.put("start", true);
            return;
        }
        model.put("start", false);
    }

    private void processMove(Request request) {
        String[] parameters = request.body().split("&");
        Map<String, String> map = Arrays.stream(parameters)
                .map(s -> s.split("="))
                .collect(Collectors.toMap(s -> s[0], s -> URLDecoder.decode(s[1], StandardCharsets.UTF_8)));
        String rawSource = map.get("source").trim();
        String rawTarget = map.get("target").trim();
        chessController.processMove(rawSource, rawTarget);
    }

    private String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
