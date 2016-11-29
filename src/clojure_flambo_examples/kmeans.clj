(ns clojure-flambo-examples.kmeans
  (:require [clojure.core.matrix :as c]
            [clojure.math.numeric-tower :as m]
            [flambo.conf :as conf]
            [flambo.api :as f]
            [flambo.tuple :as ft]))

(def c (-> (conf/spark-conf)
           (conf/master "local")
           (conf/app-name "k-means example")))

(defn squared-distance [x y]
  (m/expt (c/distance x y) 2))

(defn parse-text-to-vec [line]
  (->> (re-seq #"[\d.]+" line)
       (map read-string)
       (c/dense)))

(defn closest-point [point centers-collection]
  ;; we need the index of the collection,
  ;; not the value itself
  (->> centers-collection
       (map vector (range))
       (map #(-> [(first %), (squared-distance point (second %))]))
       (sort-by second)
       first
       first))

(defn calculate-new-centroids
  [data kpoints]
  (let [wrapped-points
        (-> data
            (f/map-to-pair (f/fn [point]
                             (let [cpoint (closest-point point kpoints)]
                               (ft/tuple cpoint [point 1]))))
            (f/reduce-by-key (f/fn [x y]
                               [(apply c/add (map first [x y]))
                                (apply + (map second [x y]))]))
            (f/map-values (f/fn [value]
                            (c/scale (first value) (/ 1 (second value)))))
            f/sort-by-key
            f/collect)]
    (map (fn [x] (second (f/untuple x))) wrapped-points)))

(defn kmeans-final-points
  [data centroids converge-dist]
  (loop [temp-dist 1 centroids centroids]
    (if (> temp-dist converge-dist)
      (let [new-centroids (calculate-new-centroids data centroids)
            new-temp-dist (->> (do
                                 (println new-centroids)
                                 (map squared-distance centroids new-centroids))
                               (apply +))]
        (recur new-temp-dist new-centroids))
      centroids)))

(defn k-means [text-file-path number-of-clusters converge-dist]
  (f/with-context context c
    (let [data (-> (f/text-file context text-file-path)
                   (f/map parse-text-to-vec)
                   (f/cache))
          kpoints (f/sample data false number-of-clusters 42)]
      (kmeans-final-points data kpoints converge-dist))))

