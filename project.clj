(defproject sheet-pad "0.1.0-alpha"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.107" :scope "provided"]
                 [reagent "0.5.0"]
                 [re-frame "0.4.1"]
                 [lein-figwheel "0.3.7"]
                 [com.lucasbradstreet/instaparse-cljs "1.3.5"]]

  :aliases {"auto-test" ["do" "clean," "cljsbuild" "auto" "test"]}

  :plugins [[lein-cljsbuild "1.0.6"]
            [lein-figwheel "0.3.7"]]

  :hooks [leiningen.cljsbuild]

  :profiles {:dev {:plugins [[com.cemerick/clojurescript.test "0.3.2"]]
                   :cljsbuild {:builds {:client {:source-paths ["devsrc"]
                                                 :compiler
                                                 {:main sheetpad.dev
                                                  :optimizations :none
                                                  :source-map true
                                                  :source-map-timestamp true}}
                                        :test {:source-paths ["src/sheetpad" "test/sheetpad"]
                                               :notify-command ["phantomjs" :cljs.test/runner
                                                                "target/test.js"]
                                               :compiler {:output-to "target/test.js"
                                                          :optimizations :whitespace
                                                          :pretty-print true}}}}}

             :prod {:cljsbuild
                    {:builds {:client {:compiler
                                       {:optimizations :advanced
                                        :elide-asserts true
                                        :pretty-print false}}}}}}

  :figwheel {:repl false}

  :cljsbuild {:builds {:client {:source-paths ["src"]
                                :compiler
                                {:output-dir "target/client"
                                 :output-to "target/client.js"}}}})
