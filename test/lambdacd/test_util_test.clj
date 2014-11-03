(ns lambdacd.test-util-test
  (:require [clojure.test :refer :all]
            [clojure.core.async :as async]
            [lambdacd.test-util :refer :all]
            [clojure.core.async :as async]))

(defn some-function-changing-an-atom [a]
  (reset! a "hello")
  (reset! a "world"))

(deftest atom-history-test
  (testing "that we can record the history of an atom"
    (let [some-atom (atom "")]
      (is (= ["hello" "world"]
             (atom-history-for some-atom (some-function-changing-an-atom some-atom)))))))


;; probably unused, TODO: remove
(deftest last-on-test
  (testing "that it returns the last element on a channel"
    (is (= "world" (last-on (async/to-chan ["hello" "world"]))))
    (is (= nil (last-on (async/to-chan []))))))

(deftest result-channel->map-test
  (testing "that it converts the channel-data to a map"
    (is (= {} (result-channel->map (async/to-chan []))))
    (is (= {:status :success} (result-channel->map (async/to-chan [[:status :success]]))))
    (is (= {:status :success :out "hello world"}
           (result-channel->map (async/to-chan [[:out "hello"] [:out "hello world"] [:status :success] ]))))
    (is (= {:status :success :out "hello world"}
           (result-channel->map (async/to-chan [[:out "hello"] [:out "hello world"] [:status :success] [:foo :bar] ]))))))