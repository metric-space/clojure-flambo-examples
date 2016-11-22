(ns clojure-flambo-examples.pi-test
  (:require [clojure-flambo-examples.pi :refer [pi]]
            [clojure.test :refer :all]))

(deftest pi-test
  (let [pi-value (pi)]
    (is (and
         (> pi-value 3.08)
         (< pi-value 3.2)))))

