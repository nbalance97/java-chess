package chess.controller;

import chess.domain.Team;
import chess.domain.result.StatusResult;
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

        post("/load", (req, res) -> {
           chessController.load();
           res.redirect("/");
           return null;
        });

        post("/save", (req, res) -> {
            chessController.save();
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
        addScoreInformation(model);
        addFinish(model);
        return model;
    }

    private void addScoreInformation(Map<String, Object> model) {
        if (chessController.isPlaying()) {
            StatusResult statusResult = chessController.processStatus();
            model.put("blackscore", statusResult.getBlackScore());
            model.put("whitescore", statusResult.getWhiteScore());
        }
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

    private void addFinish(Map<String, Object> model) {
        if (chessController.isFinish()) {
            model.put("finish", true);
            return;
        }
        model.put("finish", false);
    }

    private void processMove(Request request) {
        String[] parameters = request.body().split("&");
        Map<String, String> map = parameterToMap(parameters);
        String rawSource = map.get("source").trim();
        String rawTarget = map.get("target").trim();
        chessController.processMove(rawSource, rawTarget);
    }

    private Map<String, String> parameterToMap(String[] parameters) {
        return Arrays.stream(parameters)
                .map(s -> s.split("="))
                .collect(Collectors.toMap(s -> s[0], s -> URLDecoder.decode(s[1], StandardCharsets.UTF_8)));
    }

    private String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
