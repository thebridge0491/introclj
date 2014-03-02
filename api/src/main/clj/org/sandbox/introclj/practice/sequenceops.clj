(ns ^{:doc "Sequenceops library"} org.sandbox.introclj.practice.sequenceops
    (:require
       [clojure.tools.logging :as log]
       )
    (:import 
       [org.slf4j LoggerFactory]
       )
    )

(def ^{:private true} pracLogger (LoggerFactory/getLogger "prac"))

(defn index-r
    [pred xs & {:keys [ndx] :or {ndx 0}}]
    (cond (empty? xs) nil
        (pred (first xs)) ndx
        :else (index-r pred (rest xs) :ndx (+ ndx 1))))

(defn index-i
    [pred xs & {:keys [ndx] :or {ndx 0}}]
    (letfn [
        (iter
            [idx rst]
            (cond (empty? rst) nil
                (pred (first rst)) idx
                :else (iter (+ idx 1) (rest rst))))]
        (iter ndx xs)))

(defn reverse-r
    [xs]
    (if (empty? (rest xs))
        xs
        (into [] (concat (reverse-r (rest xs)) (list (first xs))))))

(defn reverse-i
    [xs]
    (letfn [
        (iter
            [rst acc]
            (if (empty? rst)
                acc
                (iter (rest rst) (cons (first rst) acc))))]
        (.debug pracLogger "reverse-i")
        (iter xs '())))


(defn -main
    [& argv]
    (let [xs (list 0 1 2 3)]
        (printf "(reverse %s): %s\n" xs (reverse-i xs))
        (flush)))

(when (and (.isFile (clojure.java.io/file *file*)) (re-find  #"sequenceops.clj" *file*))
    (apply -main *command-line-args*))
