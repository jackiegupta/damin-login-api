#!/bin/sh
echo "********************************************************"
echo "**************** PROFILE $PROFILE **********************"
echo "********************************************************"

echo "********************************************************"
echo "STARING $PROFILE"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom   \
     -Dspring.profiles.active=$PROFILE  \
     -DJAVA_OPTS=-Xmx1024m -Xms512m        \
     -jar /usr/local/target/@project.build.finalName@.jar