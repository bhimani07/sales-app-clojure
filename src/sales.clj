(ns sales
  (:require [clojure.string :as str]))

(use 'clojure.java.io)

(defn String->Number [str]
  (let [n (read-string str)]
    (if (number? n) n nil)))

(defn nested-map [fname re keys]
  (let [lines (-> (slurp fname)
                  (str/split-lines))
        line-cols (map #(clojure.string/split % re) lines)
        ids (map #(Integer/parseInt (first %)) line-cols)
        rest-cols (map rest line-cols)
        rest-maps (map #(zipmap keys %) rest-cols)]
    (zipmap ids rest-maps)))

(def customer-table (into (sorted-map) (nested-map "cust.txt" #"\|" [:name :address :phone])))
(def product-table (into (sorted-map) (nested-map "prod.txt" #"\|" [:itemDescription :unitCost])))
(def sales-table (into (sorted-map) (nested-map "sales.txt" #"\|" [:custID :prodID :itemCount])))

(defn total-sales-for-customer
  [name, &customerTable, &salesTable, &productTable]

  (def customer-purchase-detail (keep (fn [[k v]] (if (= (get v :custID)
                                                         (apply str (keep (fn [[k v]] (if (= (get v :name) name)
                                                                                        k)) &customerTable)))
                                                    (get-in v []))) &salesTable))
  (reduce + (map #(:totalCost %) (map (fn [v]
                                        (let [item-price (:unitCost (get &productTable (Integer/parseInt (:prodID v))))
                                              item-count (:itemCount v)]
                                          {:totalCost (* (String->Number item-count) (String->Number item-price))}))
                                      customer-purchase-detail)))
  )

(defn total-count-for-product
  [name, &salesTable, &productTable]

  (def intermediate-result (keep (fn [[k v]] (if (= (get v :prodID)
                                                    (apply str (keep (fn [[k v]] (if (= (get v :itemDescription) name) k)) &productTable)))
                                               {:itemCount (String->Number (:itemCount v))})) &salesTable))
  (reduce + (map #(:itemCount %) intermediate-result)))

(defn user-input
  []
  (println "\n*** Sales Menu ***\n------------------\n1. Display Customer Table\n2. Display Product Table\n3. Display Sales Table\n4. Total Sales for Customer\n5. Total Count for Product\n6. Exit\nEnter an option?")
  (let [x (Integer/parseInt (str/trim (read-line)))]
    (case x 1 (do (doall (map (fn [[k, v]] (println k " --> [" v "]")) customer-table)) (recur))
            2 (do (doall (map (fn [[k, v]] (println k " --> [" v "]")) product-table)) (recur))
            3 (do (doall (map (fn [[k, v]] (println k " --> [" v "]")) sales-table)) (recur))
            4 (do (println "Enter Customer Name: ")
                  (let [name (str/trim (read-line))]
                    (println name ": $" (total-sales-for-customer name customer-table sales-table product-table))) (recur))
            5 (do (println "Enter Product Name: ")
                  (let [name (str/trim (read-line))]
                    (println name ": " (total-count-for-product name sales-table product-table))) (recur))
            6 ((println "Good-Bye") (System/exit 0))
            (do (println "Incorrect Choice") (recur))
            )))

(user-input)
