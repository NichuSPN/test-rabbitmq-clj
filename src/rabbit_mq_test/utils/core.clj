(ns rabbit-mq-test.utils.core
  (:gen-class)
  (:require [clojure.data.json :as json]))

(defn hash-map-to-string
      "Convert Hash Map to String"
      [obj]
      (json/write-str obj))

(defn bytes-to-hash-map
      "Convert Bytes to Hash Map"
      [bytes]
      (-> bytes
          (String. "UTF-8")
          (json/read-str :key-fn keyword)))