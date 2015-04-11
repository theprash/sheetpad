(ns sheetpad.dev
  (:require [sheetpad.core :as sheetpad]
            [figwheel.client :as fw]))

(fw/start {:on-jsload sheetpad/run
           :websocket-url "ws://localhost:3449/figwheel-ws"})
