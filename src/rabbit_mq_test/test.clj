(ns rabbit-mq-test.test
  (:gen-class)
  (:require [rabbit-mq-test.rabbit-mq.core :as rmq]
            [rabbit-mq-test.utils.core     :as util]
            [rabbit-mq-test.plugin-test    :as plugin]))

(defn message-handler-common
  [ch {:keys [content-type delivery-tag type] :as meta} ^bytes payload]
  ;(println content-type type)
  (println "Common >>> " (util/bytes-to-hash-map payload)))

(defn message-handler-test
  [p-ch ch {:keys [content-type delivery-tag type] :as meta} ^bytes payload]
  (plugin/publish-msg p-ch (util/bytes-to-hash-map payload)))

(defn test-run [& args]
  (let [[conn ch] (rmq/get-conn-channel)
        [p-conn p-ch] (plugin/initialize-plugin)
        ex "test.ex"]
    (rmq/fanout ch ex)
    ;(rmq/start-consumer ch ex "test-1" message-handler-common)
    (rmq/start-consumer ch ex "test-2" message-handler-common)
    (rmq/start-consumer ch ex "plugin-test" (partial message-handler-test p-ch))
    (rmq/publish-q-json ch ex "" {:test-msg "Message 1 Test"})
    (rmq/publish-q-json ch ex "" {:test-msg "Message 2 Test"})
    (rmq/publish-q-json ch ex "" {:test-msg "Message 3 Test"})
    (rmq/publish-q-json ch ex "" {:test-msg "Message 4 Test"})
    (rmq/publish-q-json ch ex "" {:test-msg "Message 5 Test"})
    (rmq/publish-q-json ch ex "" {:test-msg "Message 6 Test"})
    ;(rmq/publish-q-json ch ex "" {:test-msg "Second Message Test"})
    (Thread/sleep 20000)
    (rmq/close ch)
    (rmq/close conn)))