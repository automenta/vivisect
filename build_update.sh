#!/bin/bash

mount --bind /home/me/share/opennars/nars_gui/automenta /home/me/share/vivisect/src/main/java/automenta

#file helpful for local development until mvn setup is stabilized

#while true; do

#inotifywait -e close_write target/vivisect-1.0-SNAPSHOT.jar && \
#cp target/vivisect-1.0-SNAPSHOT.jar ../opennars/lib/gui/vivisect.jar

#done
