(in-ns 'org.sandbox.introclj.intro.core-test)

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

(test/deftest test-method
    "test-method"
    (test/is (= (* 2 2) 4)))

(test/deftest test-dblMethod
    "test-dblMethod"
    ;(test/is (= 100.0 100.0))
    (test/is (util/in-epsilon (* 0.001 100.0) 100.0 100.0))
    )

(test/deftest test-strMethod
    "test-strMethod"
    (test/is (= "Hello" "Hello")))

(test/deftest test-badMethod
    "test-badMethod"
    (test/is (> 4 5)))

(test/deftest test-failMethod
    "test-failMethod"
    (test/is false))

(test/deftest test-exceptionMethod
    "test-exceptionMethod"
    (test/is (thrown? ArithmeticException (/ 1 0))))

;(defn test-ns-hook []
;    (test-method))
