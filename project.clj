(defproject clojure-flambo-examples "0.1.0-SNAPSHOT"
  :description "Spark examples in clojure using flambo DSL"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.apache.spark/spark-core_2.11 "2.0.1"]
                 [yieldbot/flambo "0.8.0"]
                 [net.mikera/core.matrix "0.57.0"]]
  :aot [flambo.function])
