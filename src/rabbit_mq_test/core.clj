(ns rabbit-mq-test.core
  (:gen-class)
  (:require [rabbit-mq-test.mq :as mq]
            [rabbit-mq-test.test :as test]))

(defn -main
  "I don't do a whole lot...yet."
  [& args]
  (println "Hello, World!")
  (test/test-run))