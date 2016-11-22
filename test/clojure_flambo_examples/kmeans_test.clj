(ns clojure-flambo-examples.kmeans-test
  (:require [clojure.core.matrix :as c]
            [clojure-flambo-examples.kmeans :refer :all]
            [clojure.test :refer :all]))

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

