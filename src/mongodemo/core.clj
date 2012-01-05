(ns mongodemo.core
  (:require [somnium.congomongo :as mongo]
            [clj-time.core :as time]
            [clj-time.format :as tformat]))

(mongo/set-connection! (mongo/make-connection "mydb"))

(mongo/fetch :orders)

(defn product [type size color]
  {:product type :size size :color color})

(defn tshirt [size color]
  (product :tshirt size color))

(def colors [:red :blue :white :orange :green :black])
(def sizes [:small :medium :large])

(defn random-product []
  (let [type (rand-nth [:tshirt :shorts :thongs])
        size (rand-nth sizes)
        color (rand-nth colors)]
    (product type size color)))


(defn color-mix-50-50 [color]
  (rand-nth [color (rand-nth colors)]))

(defn random-coordinated-outfit []
  (let [size (rand-nth sizes)
        main-color (rand-nth [:red :blue :white :orange :green :black])]
    [(product :tshirt size (color-mix-50-50 main-color))
     (product :shorts size (color-mix-50-50 main-color))
     (product :thongs size (color-mix-50-50 main-color))]))

(defn order-one-outfit [date]
  {:time date :items (random-coordinated-outfit)})

(defn format-time-for-mongo [date-time-obj]
  (tformat/unparse (tformat/formatters :date-time) date-time-obj))

(random-coordinated-outfit) 
(time/date-time 2010 01 01 04 25)

(mongo/insert! :orders (order-one-outfit (time/date-time 2010 01 01 04 25)))

(tformat/show-formatters)

(format-time-for-mongo (time/date-time 2010 01 01 04 25))



