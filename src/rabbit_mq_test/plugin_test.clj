(ns rabbit-mq-test.plugin-test
  (:gen-class)
  (:require [rabbit-mq-test.rabbit-mq.core :as rmq]
            [rabbit-mq-test.utils.core     :as util]))

(defn msg-1
  [ch {:keys [content-type delivery-tag type] :as meta} ^bytes payload]
  ;(println content-type type)
  (println "Handler-1 >>> " (util/bytes-to-hash-map payload)))

(defn msg-2
  [ch {:keys [content-type delivery-tag type] :as meta} ^bytes payload]
  ;(println content-type type)
  (println "Handler-2 >>> " (util/bytes-to-hash-map payload)))

(defn initialize-plugin
  []
  (let [[conn ch] (rmq/get-conn-channel)]
    (rmq/create-q ch "test-q")
    (rmq/subscribe-q ch "test-q" msg-1)
    (rmq/subscribe-q ch "test-q" msg-2)
    [conn ch]))

(defn publish-msg [ch payload]
  (do
    ;(println "Publishing >>> " payload)
    (rmq/publish-q-json ch "" "test-q" (util/hash-map-to-string payload))))
