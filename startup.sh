#!/bin/bash	
DEST=target/scala-2.12/

sbt assemblyAll

tmux new-session java -jar ${DEST}mbFloodSeedNode.jar \; \
     split-window -v java -jar modules/module1/${DEST}module1.jar \; \
     split-window -h java -jar modules/module1/${DEST}module1.jar \; \
     select-pane -t 0 \; \
     split-window -h java -jar modules/module2/${DEST}module2.jar \;
#     select-pane -t 0 \;
     	