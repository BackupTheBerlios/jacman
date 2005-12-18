#!/bin/bash

# jacman_test.sh - this script is for running jacman outside of Eclipse, but within its
# workspace directory structure. This script needs to be run from the "bin" directory of
# the Jacman workspace.
#
# Andrew Roberts - 18 December 2005

LIB_DIR="../lib"

LIBS="glazedlists-1.0.0.jar 
jdic.jar
l2fprod-common-all.jar
looks-1.2.2.jar"

CLASSPATH="."

for lib in $LIBS
do
	CLASSPATH="$CLASSPATH:$LIB_DIR/$lib"
done

exec java -cp $CLASSPATH andyr.jacman.Jacman
