/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;
import singlesort.Game;
import singlesort.PanelRenderer;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@SpringBootApplication
public class Main {

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Autowired
  private DataSource dataSource;

  private Map<String, Game> games = new ConcurrentHashMap<>();

  private Map<Game, PanelRenderer> panelRenderers = new ConcurrentHashMap<>();

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }


  @RequestMapping(path = "/game", method = RequestMethod.GET)
  String game() {
    return "game";
  }

  @RequestMapping(path = "/newgame", method = RequestMethod.GET)
  public RedirectView newGame(@RequestParam(name = "players", defaultValue = "1") Integer players,
                              @CookieValue("id") String id,
                              HttpServletResponse response) {
    Game game = new Game(players);
    games.put(id, game);
    panelRenderers.put(game, new PanelRenderer(game));

    response.addCookie(new Cookie("timer", "0"));

    return new RedirectView("game");
  }

  @RequestMapping(path = "/gameimg", method = RequestMethod.GET)
  public void gameImage(
          @RequestParam(name = "x", defaultValue = "-10") Integer x,
          @RequestParam(name = "y", defaultValue = "-10") Integer y,
          @RequestParam(name = "id", defaultValue = "0") String id,
          @CookieValue("id") String cookieId,
          @CookieValue("timer") String timer,
          HttpServletResponse response) throws IOException {
      String contentType = "application/octet-stream";
      response.setContentType(contentType);

      OutputStream out = response.getOutputStream();

      if(id.equals("0")) {
        id = cookieId;
      }
//      BufferedImage img = ImageIO.read(getClass().getResource("/public/lang-logo.png"));
//    if(x > 0 && y > 0) {

    Game game = null;
    if(games.containsKey(id)) {
      game = games.get(id);
    } else {
      game = new Game(1);
      games.put(id, game);
      panelRenderers.put(game, new PanelRenderer(game));
    }
      game.setTime(Integer.parseInt(timer));
      game.mouseClicked(x, y);
//    }

    PanelRenderer panelRenderer = panelRenderers.get(game);


      BufferedImage img = new BufferedImage(panelRenderer.getWindowWidth(), panelRenderer.getWindowHeight(), BufferedImage.TYPE_INT_ARGB);
      Graphics graphics = img.getGraphics();
      panelRenderer.paint(graphics);

      ImageIO.write(img,"png", out);

      out.close();
  }

  @RequestMapping("/")
  String index() {
    return "index";
  }

  @RequestMapping("/db")
  String db(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
      ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

      ArrayList<String> output = new ArrayList<String>();
      while (rs.next()) {
        output.add("Read from DB: " + rs.getTimestamp("tick"));
      }

      model.put("records", output);
      return "db";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @Bean
  public DataSource dataSource() throws SQLException {
    if (dbUrl == null || dbUrl.isEmpty()) {
      return new HikariDataSource();
    } else {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(dbUrl);
      return new HikariDataSource(config);
    }
  }

}
