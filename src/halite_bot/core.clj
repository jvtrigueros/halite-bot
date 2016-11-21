(ns halite-bot.core
  (:gen-class)
  (:require [halite-bot.game :as game]
            [halite-bot.io :as io]
            [taoensso.timbre :as timbre :refer [log spy]]
            [taoensso.timbre.appenders.3rd-party.rolling :as rolling]))

(timbre/merge-config! {:appenders
                       {:println (rolling/rolling-appender {:path    "trigobot.log"
                                                            :pattern :daily})}})

(def bot-name "trigobot")

(defn random-moves
  "Takes a 2D vector of sites and returns a list of [site, direction] pairs"
  [my-id game-map]
  (let [my-sites (->> game-map
                      flatten
                      (filter #(= (:owner %) my-id)))]
    (map vector my-sites (repeatedly #(rand-nth game/directions)))))

(defn move-weakest
  [id game-map site]
  (let [weakest (->> game/cardinal-directions
                     (map #(vector % (game/adjacent-site game-map site %)))
                     (filter #(not= (:owner (second %)) id))
                     (filter #(< (:strength (second %))
                                 (:strength site))))]
    (if (not (empty? weakest))
      (first (rand-nth weakest)))))

(defn move
  [id game-map]
  (let [my-sites (->> game-map
                      flatten
                      (filter #(= (:owner %) id)))]
    (map (fn [site]
           (vector
             site
             (or (move-weakest id game-map site)
                 (if (< (:strength site) (* (:production site) 5))
                   :still
                   (rand-nth [:north :west])))))
         my-sites)))


(defn -main []
  (let [{:keys [my-id productions width height game-map]} (io/get-init!)]

    ;; Do any initialization you want with the starting game-map before submitting the bot-name
    (log :info (str "Starting out with bot " bot-name))
    (println bot-name)

    (doseq [turn (range)]
      (let [game-map (io/create-game-map width height productions (io/read-ints!))]
        (io/send-moves! (move my-id game-map))))))
