(ns rabbit-mq-test.rabbit-mq.core
  (:gen-class)
  (:require [langohr.core              :as rmq]
            [langohr.channel           :as lch]
            [langohr.queue             :as lq]
            [langohr.exchange          :as le]
            [langohr.consumers         :as lc]
            [langohr.basic             :as lb]
            [rabbit-mq-test.utils.core :as util]))

(defn get-conn-channel
      "returns connection and channel"
      []
      (let [conn  (rmq/connect {:username "nichuspn",
                                :password "swymdevtest",
                                :vhost "/",
                                :host "localhost",
                                :port 5672})
            ch    (lch/open conn)]
        [conn ch]))

(defn create-q
      "Creating a queue"
      [ch qname]
      (lq/declare ch
                  qname
                  {:exclusive false
                   :auto-delete true}))

(defn subscribe-q
      "Consumer subscribing for a queue"
      [ch qname message-handler]
      (lc/subscribe ch
                    qname
                    message-handler
                    {:auto-ack true}))

(defn publish-q-json
      "Publish a message with JSON payload"
      [ch ex qname payload]
      (lb/publish ch
                  ex
                  qname
                  (util/hash-map-to-string payload)
                  {:content-type "application/json"
                   :type "test.msg"}))

(defn bind-q-ex
      "Bind a queue to exchange"
      [ch qname ex]
      (lq/bind ch qname ex))

(defn fanout
      "Declare fanout exchange"
      [ch ex]
      (le/fanout ch ex))

(defn start-consumer
      "Start consumer for given exchange creating a queue"
      [ch ex username msg-handler]
      (let [qname (format "test.q.%s" username)]
        (create-q ch qname)
        (bind-q-ex ch qname ex)
        (subscribe-q ch qname msg-handler)))

(defn close
      [entity]
      (rmq/close entity))