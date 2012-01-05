(ns mongodemo.core
  (:require [somnium.congomongo :as mongo]
            [clj-time.coerce :as tcoerce]
            [clj-time.core :as time]
            [clj-time.format :as tformat]))

;; (mongo/set-connection! (mongo/make-connection "mydb"))

;; (mongo/fetch :orders)

(defn product [type size color]
  {:product type :size size :color color})

(defn tshirt [size color slogan]
  {:product :tshirt :size size :color color :slogan slogan})

(def colors [:red :blue :white :orange :green :black])
(def sizes [:small :medium :large])
(def slogans ["I'm With Stupid" "Quack!" "Raiders" "No, I won't fix your computer"])

(defn random-product []
  (let [type (rand-nth [:tshirt :shorts :thongs])
        size (rand-nth sizes)
        color (rand-nth colors)]
    (product type size color)))

(defn color-mix-50-50 [color]
  (rand-nth [color (rand-nth colors)]))

;;; ncolors/total = mix, ncolors = (nsame + ncolors)*mix
;;; nsame*mix = ncolors - ncolors*mix, nsame = ncolors*(1-mix)/mix

(defn color-mix
  "mix must be a number between 0 and 1, ncolors/total = mix"
  [main-color mix]
  (let [ncolors (count colors)
        nsame (int (/ (* ncolors (- 1.0 mix)) mix))]
    (rand-nth (concat (repeat nsame main-color) colors))))

(defn random-coordinated-outfit []
  (let [size (rand-nth sizes)
        main-color (rand-nth [:red :blue :white :orange :green :black])]
    [(tshirt size (color-mix main-color 0.2) (rand-nth slogans))
     (product :shorts size (color-mix main-color 0.2))
     (product :thongs size (color-mix main-color 0.2))]))

(defn format-time-for-mongo [date-time-obj]
  (tcoerce/to-date date-time-obj))

(defn order-one-outfit [date]
  {:date (format-time-for-mongo date) :items (random-coordinated-outfit)})

;; (random-coordinated-outfit) 
;; (tcoerce/to-date (time/date-time 2010 01 01 04 25))
;; (time/date-time 2010 2 1)
;; (time/plus (time/date-time 2010 1 1) (time/days 1))
;; (mongo/insert! :orders (order-one-outfit (time/date-time 2010 1 1)))
;; (let [start-date (time/date-time 2010 1 1)
;;       end-date (time/date-time 2010 1 31)]
;;   (time/before? (time/plus start-date (time/days 60)) end-date))

(let [start-date (time/date-time 2010 1 1)
      end-date (time/date-time 2011 12 31)
      conn (mongo/make-connection "mydb")]
  (loop [this-date start-date]
    (when (time/before? this-date end-date)
      (let [norders (rand-int 9)]
        (dotimes [n norders]
          (let [this-order (order-one-outfit this-date)]
            (mongo/with-mongo conn
              (mongo/insert! :orders this-order))))
        (recur (time/plus this-date (time/days 1)))))))

      





