# rollback
HOME_DIR="/home/viz"
RELEASE_DIR="$HOME_DIR/releases/dist"

TOMCAT_DIR="$HOME_DIR/apache-tomcat-8.5.15"
TOMCAT_START="$TOMCAT_DIR/bin/startup.sh"
TOMCAT_STOP="$TOMCAT_DIR/bin/shutdown.sh"

cd $RELEASE_DIR
# one per each line, reverse time order
# latest at the bottom
LAST="$(ls -1tr | tail -1)"
SECONDTOLAST="$(ls -1tr | tail -2 | head -1)"

rm -rf $LAST

$TOMCAT_STOP
ln -sfn $RELEASE_DIR/$SECONDTOLAST $TOMCAT_DIR/webapps/ROOT
$TOMCAT_START
