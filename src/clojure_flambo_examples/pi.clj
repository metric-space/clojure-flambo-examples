(ns clojure-flambo-examples.pi
  (:require [flambo.conf :as conf]
            [flambo.api :as f]))

(def c (-> (conf/spark-conf)
           (conf/master "local")
           (conf/app-name "flamo-examples")))

(def sample-length 10000)

(defn circle? [i]
  (let [x (rand)
        y (rand)]
    (or
     (and (< (+ (* x x) (* y y)) 1) 1)
     0)))

(defn pi [& {:keys [sample-len] :or {sample-len sample-length}}]
  (let [prob-calc (f/with-context a c
                    (-> (f/parallelize a (range sample-len))
                        (f/map (f/fn [x] (circle? x)))
                        (f/reduce (f/fn [x y] (+ x y)))))]
    (/ (* 4.0 prob-calc)
       (float sample-len))))

