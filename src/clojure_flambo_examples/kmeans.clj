(ns clojure-flambo-examples.kmeans
  (:require [clojure.core.matrix :as c]
            [flambo.conf :as conf]
            [flambo.api :as f]
            [flambo.tuple :as ft]))

(def c (-> (conf/spark-conf)
           (conf/master "local")
           (conf/app-name "k-means example")))

(defn parse-text-to-vec [line]
  (->> (re-seq #"[\d.]+" line)
       (map read-string)
       (c/dense)))

(defn closest-point [point centers-collection]
  (->> centers-collection
       (map vector (range))
       (map #(-> [(first %), (c/distance point (second %))]))
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
    (map (fn [x] (f/untuple x)) wrapped-points)))

;;(defn k-means [text-file-path number-of-clusters converge-dist]
;;  (f/with-context context c
;;    (let [data (-> (f/text-file context text-file-path)
;;                   (f/map parse-text-to-vec)
;;                   (f/cache))
;;          kpoints (f/sample data false number-of-clusters 42)]
;;      (loop [temp-dist 1 points kpoints]
;;        (if (> temp-dist converge-dist)
;;          (let [processed-points (calculate-new-centroid data kpoints)
;;                new-temp-dist (->> (map c/dist kpoints processed-points)
;;                                   (apply sum))]
;;            (recur new-temp-dist processed-points))
;;          (do
;;            (println " Final cluster points ")
;;            (println kpoints)))))))

