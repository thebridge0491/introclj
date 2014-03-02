(in-ns 'org.sandbox.introclj.practice.core-test)
(require '[org.sandbox.introclj.practice.classic :as classic])
(import '[org.sandbox.introclj.practice Classic_java])

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

(test/use-fixtures :once class-fixture1)
(test/use-fixtures :each fixture1)

(test/deftest test-fact
    "test-fact"
    (doseq [f [classic/fact-r classic/fact-i #(Classic_java/fact_lp %)
            #(Classic_java/fact_i %)]]
        (test/is (= 120 (f 5)))))

(test/deftest test-expt
    "test-expt"
    (doseq [row (util/cartesian-prod [2.0 11.0 20.0] [3.0 6.0 10.0])]
        (let [exp (Math/pow (nth row 0) (nth row 1))]
            (doseq [f [classic/expt-r classic/expt-i #(Classic_java/expt_lp %1 %2)
                    #(Classic_java/expt_i %1 %2)]]
                ;(test/is (= exp (f (nth row 0) (nth row 1))))
                (test/is (util/in-epsilon (* 0.001 exp) exp (f (nth row 0) (nth row 1))))
                ))
        ))
