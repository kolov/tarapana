(ns tarapana.flow-test
  (:use clojure.test
        tarapana.flow))

(deftest test-admit
  (testing "admittance"
    (is (= (admittance 30 10 [10 10]) [10 [10 10]]))
    (is (= (admittance 30 20 [10 10]) [15 [15/2 15/2]]))
    (is (= (admittance 30 20 [ 1 30]) [15 [1 14]]))
    (is (= (admittance 30 20 [ 1 3]) [20 [1 3]]))
    ))
