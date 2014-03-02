(in-ns 'org.sandbox.introclj.practice.core-test)
(require '[org.sandbox.introclj.practice.sequenceops :as seqops])
(import '[org.sandbox.introclj.practice Sequenceops_java])

(defn genListInts
    [gen0]
    (gen/such-that not-empty (gen/list gen0)))

(def prop-index
    (prop/for-all [e-xs (gen/tuple (gen/choose -50 50) (genListInts (gen/choose -50 50)))]
        (let [e (get e-xs 0) xs (get e-xs 1) idx0 (.indexOf xs e)
                ans (if (= -1 idx0) nil idx0)]
            (and (= idx0 (Sequenceops_java/indexOf_lp e xs compare))
            (reduce (fn [a f] (and a (= ans (f (fn [el] (= e el)) xs)))) true
                [seqops/index-r seqops/index-i]))
            )))

(def prop-reverse
    (prop/for-all [xs (genListInts (gen/choose -50 50))]
        (= xs (reverse (reverse xs)))
        (let [tmp (java.util.ArrayList. xs)]
            (Sequenceops_java/reverse_lp tmp)
            (and (= xs (reverse tmp))
            (reduce (fn [a f]
                (and a (= xs (reverse (f xs))))) true
                [seqops/reverse-r seqops/reverse-i]))
        )))


;(qc/quick-check 100 prop-index)
;(testqc/defspec commutativeAdd 100 prop-index)
(testqc/defspec index-prop prop-index)

(testqc/defspec reverse-prop prop-reverse)
