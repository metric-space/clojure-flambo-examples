(ns clojure-flambo-examples.pagerank
   (:require [clojure.core.matrix :as c]
             [clojure.math.numeric-tower :as m]
             [flambo.conf :as conf]
             [flambo.api :as f]
             [flambo.tuple :as ft]))

 (def c (-> (conf/spark-conf)
            (conf/master "local")
            (conf/app-name "pagerank example")))

(defn separate [z]
  (let [[ligne val] (f/untuple z)
        rank (/ val (count ligne))]
    (map (fn [x] (ft/tuple x rank)) ligne)))

 (defn page-rank-iter-step [links ranks]
   (-> (f/join links ranks)
       (f/values)
       (f/flat-map separate)
       (f/reduce-by-key +)
       (f/map-values (fn [x] (+ 0.15 (* 0.85 x))))))


