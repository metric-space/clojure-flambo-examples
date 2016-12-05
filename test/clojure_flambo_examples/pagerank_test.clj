(ns clojure-flambo-examples.pagerank-test
  (:require [clojure-flambo-examples.pagerank :refer :all]
            [flambo.api :as f]
            [flambo.tuple :as ft]
            [clojure.test :refer :all]))

(deftest test-separate
  (let [test-data (ft/tuple [1 3 4 5] 1)
        expected-rank (/ 1 4)
        expected-outcome (map #(apply ft/tuple %)
                              (map vector [1 3 4 5] (repeat expected-rank)))]
    (is (= (separate test-data) expected-outcome))))
