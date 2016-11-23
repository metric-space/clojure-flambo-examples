(ns clojure-flambo-examples.kmeans-test
  (:require [clojure.core.matrix :as c]
            [clojure-flambo-examples.kmeans :refer :all]
            [flambo.api :as f]
            [clojure.test :refer :all]))

;; from the clojure cookbook 
(defn float=
  ([x y] (float= x y 0.00001))
  ([x y epsilon]
   (let [scale (if (or (zero? x) (zero? y)) 1 (Math/abs x))]
     (<= (Math/abs (- x y)) (* scale epsilon)))) )

(deftest parse-test
  (let [test-line "0.3456, 98.45, 9.324"
        expected-output (c/dense [0.3456 98.45 9.324])]
    (is
     (= (parse-text-to-vec test-line)
        expected-output))))

(deftest closest-point-test
  (let [test-vectors [[1 2 3] [2 3 4] [8 9 10] [1 1 1] [6 5 4]]
        test-point   [0 0 0]
        expected-outcome 3]
    (is (= (closest-point test-point test-vectors))
        expected-outcome)))

(deftest test-calculate-new-centroids
  (f/with-context context c
    (let [test-data [[1,1] [1.3,0.5] [2,1] [2,2]
                     [2.3,3] [1.9,3.1] [2.2,4] [0.9,0.3]]
          test-rdd (f/parallelize context test-data)
          test-vectors [[1.3, 0.5] [2.3,3]]
          result (calculate-new-centroids test-rdd test-vectors)
          result1 ((comp flatten first) result)
          result2 ((comp flatten first rest) result)]
      (is
       (and
        (every? true? (map float= '(0 1.3 0.7) result1))
        (every? true? (map float= '(1 2.1 3.025) result2)))))))

