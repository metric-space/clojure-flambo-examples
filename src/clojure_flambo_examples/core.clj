(ns clojure-flambo-examples.core
  (:require [clojure-flambo-examples.pi :refer [pi]]))

;; yay the monte carlo pi calc!!!
(println (pi))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))


