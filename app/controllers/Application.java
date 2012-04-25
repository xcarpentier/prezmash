package controllers;


import models.Candidat;
import models.Stats;
import models.StatsAggregate;
import models.User;
import play.Logger;
import play.Play;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Cookie;
import play.mvc.Result;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import views.html.askemail;
import views.html.dataviz.stats;
import views.html.index;

import java.util.*;

public class Application extends Controller {

    private static final String COOKIE_PREZMASH = "prezmash";

    final static Form<User> signupForm = form(User.class);

    public static Result index() {
        User user = new User();

        // Cookie -> stats
        final Cookie cookie = request().cookies().get(COOKIE_PREZMASH);
        if (cookie != null) {
            final User userExist = User.find.where().eq("cookie", cookie.value()).findUnique();
            boolean hadVote = Stats.find.where().eq("user", userExist).findRowCount() > 0;
            if (hadVote)
                return ok(stats.render());
            else
                return ok(index.render());
        }

        // Ip + User agent -> ask email
        final String ip = request().getHeader("X-Forwarded-For");

        final String userAgent = request().getHeader(USER_AGENT);

        if (ip != null && userAgent!=null) {
            user.ip = ip.trim();
            Map<String, Object> mapEq = new HashMap<String, Object>();
            mapEq.put("ip", ip.trim());
            mapEq.put("userAgent", userAgent.trim());
            User existingUser = User.find.where().allEq(mapEq).findUnique();

            if (existingUser != null) {

                // reset cookie
                response().setCookie(COOKIE_PREZMASH, existingUser.cookie, 60 * 60 * 24 * 30);

                boolean hadVote = Stats.find.where().eq("user", existingUser).findRowCount() > 0;

                if (hadVote)
                    return ok(stats.render());
                else
                    return ok(askemail.render(signupForm));
            }
        }

        // set cookie here
        final String alea = Math.abs(new Random().nextInt()) + "";
        final String cookieVal = new Date().getTime() + "-" + ip + "-" + alea;
        response().setCookie(COOKIE_PREZMASH, cookieVal, 60 * 60 * 24 * 30);

        return ok(index.render());
    }

    public static Result candidatsInJson() {
        List<Candidat> candidats = Candidat.find.all();
        Collections.shuffle(candidats);
        return ok(Json.toJson((Object) candidats)).as("application/json");
    }

    public static Result stat() {
        return ok(stats.render());
    }

    public static Result statsInJson() {

        final List<StatsAggregate> aggregates = StatsAggregate.findStatsAggregate();
        final List<Stat> statsList = new ArrayList<Stat>();
        for (StatsAggregate aggregate : aggregates) {
            final Stat stat = new Stat();
            stat.name = aggregate.candidat.firstName.substring(0, 1) + ". " + aggregate.candidat.lastName;
            stat.color = aggregate.candidat.colorParty;
            stat.y = aggregate.result;
            statsList.add(stat);
        }

        final List<Stat> statSub = statsList.subList(statsList.size() - 3, statsList.size());
        final StringBuffer twitterStatus = new StringBuffer();
        final int totalVote = Stats.find.findRowCount();

        Collections.shuffle(statSub);
        for (Stat stat : statSub) {
            if (stat.y > 0)
                twitterStatus.append(stat.name + ":" + Math.round((stat.y / totalVote) * 100) + "%, ");
        }

        if (Play.isProd()) {
            // Cookie -> stats
            final Cookie cookie = request().cookies().get(COOKIE_PREZMASH);
            if (cookie != null) {
                final ConfigurationBuilder cb = new ConfigurationBuilder();
                cb.setDebugEnabled(true)
                        .setOAuthConsumerKey("UD7nOUBM3l7IblZ2E7ZJ1A")
                        .setOAuthConsumerSecret("EEqJSZprIKTYvjg9RRHXb7sSIduloLM7q2SeJCpDBU")
                        .setOAuthAccessToken("526411186-xqKGtkL9BqRYRvszDF5gwMoYxrHcx1Ij1yytonUO")
                        .setOAuthAccessTokenSecret("GVeNGp71IlkxITip7t4IRnrYjqZudkMHtsqUOmvA");

                final TwitterFactory tf = new TwitterFactory(cb.build());
                final Twitter twitter = tf.getInstance();

                try {
                    twitter.updateStatus(twitterStatus + " #présidentielle2012 #prezmash http://tiny.cc/qs89bw");
                } catch (TwitterException e) {
                    Logger.error(e.getMessage());
                }
            }
        }


        return ok(Json.toJson((Object) statsList)).as("application/json");
    }

    private static class Stat {
        public String name;
        public double y;
        public String color;
    }

    public static Result vote() {

        final String reqCandidat = request().body().asFormUrlEncoded().get("candidat")[0];
        final Candidat candidat = Candidat.find.where().eq("id", Long.valueOf(reqCandidat)).findUnique();

        final Cookie cookie = request().cookies().get(COOKIE_PREZMASH);
        User user = User.find.where().eq("cookie", cookie.value()).findUnique();

        if (candidat != null) {
            if (user == null) {
                // Ip + User agent -> ask email
                final String ip = request().getHeader("X-Forwarded-For");
                final String userAgent = request().getHeader(USER_AGENT);

                // create user
                user = new User();
                user.ip = ip;
                user.cookie = cookie.value();
                user.userAgent = userAgent.trim();
                user.createdDate = new Date();
                user.save();

            }

            final Stats stats = new Stats();
            stats.user = user;
            stats.candidat = candidat;
            stats.save();
        }

        return ok();
    }

    public static Result postEmail() {

        // here we know host and user-agent and we have reset the cookie in browser
        final Form<User> filledForm = signupForm.bindFromRequest();
        final User existingUserByEmail = User.find.where().eq("email", filledForm.get().email).findUnique();

        if (existingUserByEmail != null)
            return ok(stats.render());

        final String cookieValue = request().cookies().get(COOKIE_PREZMASH).value();
        User userToUpdate = User.find.where().eq("cookie", cookieValue).findUnique();
        userToUpdate.email = filledForm.get().email;
        userToUpdate.update();

        return ok(index.render());
    }
}
