(defproject halite-bot "0.1.0-SNAPSHOT"
  :description "A halite bot."
  :url ""
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot halite-bot.core
  :uberjar-name "MyBot.jar"
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :debug   {:jvm-opts ["-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5010,quiet=y"]}}
  :aliases {"debug" ["with-profile" "debug" "run"]})
