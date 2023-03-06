 #!/bin/bash

set -e

#PACKAGER=`/usr/libexec/java_home`/bin/jpackage
PACKAGER=/Users/mjh/Documents/GitHub/jdk/build/macosx-x86_64-server-release/images/jdk/bin/jpackage
LINKER=/Users/mjh/Documents/GitHub/jdk/build/macosx-x86_64-server-release/images/jdk/bin/jlink

#MODS=`jdeps --module-path ~/Documents/javafx-sdk-19/lib --print-module-deps input/jfxdt.jar`
#echo $MODS

#${LINKER} \
#	--strip-debug \
#	--no-man-pages \
#	--no-header-files \
#	--module-path ~/Documents/javafx-jmods-19:/Users/mjh/Documents/GitHub/jdk/build/macosx-x86_64-server-release/images/jdk/modules \
#	--add-modules java.desktop,java.se,javafx.controls,javafx.fxml \
#	--output runtime
	
${PACKAGER} \
	--verbose \
	--input input \
	--name JavaFXDesktop \
	--main-jar jfxdt.jar \
	--module-path /Users/mjh/Documents/javafx-jmods-19 \
	--add-modules java.desktop,java.se,javafx.controls,javafx.fxml \
	--java-options '-Dapple.awt.application.name=JavaFXDesktop' \
	--main-class us.hall.jfxdesktop.Main \
	--mac-package-identifier "us.hall.JavaFXDesktop" 
	
	#--runtime-image runtime 
	
	#--jlink-options '--strip-debug --no-man-pages --no-header-files' 
	#--module-path ~/Documents/javafx-jmods-19 \
	#--add-modules java.desktop,java.se,${MODS},javafx.controls \	

	#	--java-options '-Dapple.laf.useScreenMenuBar=true -Dapple.awt.application.name=HelloWorld' \
	
