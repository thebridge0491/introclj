(in-ns 'org.sandbox.introclj.practice.core-test)
(require '[org.sandbox.introclj.practice.sequenceops :as seqops])
(import '[org.sandbox.introclj.practice Sequenceops_java])

(defn class-fixture1
    "Class setUp/tearDown"
    [func]
    (println "setUpClass ...")
    (func)
    (println "... tearDownClass"))

(defn fixture1
    "Per test setUp/tearDown"
    [func]
    (println "setUp ...")
    (func)
    (println "... tearDown"))

(def lst (range 0 5))
(def revlst (range 4 -1 -1))

(test/use-fixtures :once class-fixture1)
(test/use-fixtures :each fixture1)

(test/deftest test-index
    "test-index"
    (let [el 3 pred (fn [e] (= e el))]
        (test/is (= 3 (Sequenceops_java/indexOf_lp el (java.util.ArrayList. lst) compare)))
        (test/is (= 1 (Sequenceops_java/indexOf_lp el (java.util.ArrayList. revlst) compare)))
        (doseq [f [seqops/index-r seqops/index-i]]
            (test/is (= 3 (f pred lst)))
            (test/is (= 1 (f pred revlst)))
            )))

(test/deftest test-reverse
    "test-reverse"
    (let [tmp (java.util.ArrayList. lst) revtmp (java.util.ArrayList. revlst)]
        (Sequenceops_java/reverse_lp tmp)
        (test/is (= (reverse lst) tmp))
        (Sequenceops_java/reverse_lp revtmp)
        (test/is (= (reverse revlst) revtmp))
        (doseq [f [seqops/reverse-r seqops/reverse-i]]
            (test/is (= (reverse lst) (f lst)))
            (test/is (= (reverse revlst) (f revlst)))
            )))
