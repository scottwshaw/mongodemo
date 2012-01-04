(ns mongodemo.core
  (:require [somnium.congomongo :as mongo]))

(mongo/set-connection! (mongo/make-connection "mydb"))

(mongo/fetch :orders)
