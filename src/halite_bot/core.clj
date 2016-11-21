(ns halite-bot.core
  (:gen-class)
  (:require [halite-bot.game :as game]
            [halite-bot.io :as io]))


(def bot-name "trigobot")

(defn random-moves
  "Takes a 2D vector of sites and returns a list of [site, direction] pairs"
  [my-id game-map]
  (let [my-sites (->> game-map
                      flatten
                      (filter #(= (:owner %) my-id)))]
    (map vector my-sites (repeatedly #(rand-nth game/directions)))))

(defn move
  [id game-map]
  (let [my-sites (->> game-map
                      flatten
                      (filter #(= (:owner %) id)))]
    (map (fn [site]
           (if (< (:strength site) (* (:production site) 5))
             [site :still]
             [site (rand-nth (rest game/directions))]))
         my-sites)))


(defn -main []
  (let [{:keys [my-id productions width height game-map]} (io/get-init!)]

    ;; Do any initialization you want with the starting game-map before submitting the bot-name
    (println bot-name)

    (doseq [turn (range)]
      (let [game-map (io/create-game-map width height productions (io/read-ints!))]
        (io/send-moves! (move my-id game-map))))))
