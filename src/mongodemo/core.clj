(ns mongodemo.core
  (:require [somnium.congomongo :as mongo]))

(mongo/set-connection! (mongo/make-connection "mydb"))

(mongo/fetch :orders)

;;; tshirts, shorts, thongs
;;; tshirts: size, color
;;; shorts: size, color
;;; thongs: size, color

(defn product [type size color]
  {:product type :size size :color color})

(defn tshirt [size color]
  (product :tshirt size color))

(defn random-product []
  (let [type (rand-nth [:tshirt :shorts :thongs])
        size (rand-nth [:small :medium :large])
        color (rand-nth [:red :blue :white :orange :green :black])]
    (product type size color)))

(random-product)

