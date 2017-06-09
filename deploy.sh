# TODO:
# 1. github clone - checkout branch
# 2. mvn clean package
# 3. get target/[].war
# 4. copy to ~/apache-tomcat-8.5.15/webapps/ROOT.war

REPO_NAME="jwp-spring-final"
GITHUB_REPO="https://github.com/YeeChanKo/$REPO_NAME.git"
TARGET_BRANCH="master"

HOME_DIR="/home/viz"
RELEASE_DIR="$HOME_DIR/releases/dist"
PM_PAGE_DIR="$HOME_DIR/releases/pm-page"

TOMCAT_DIR="$HOME_DIR/apache-tomcat-8.5.15"
TOMCAT_START="$TOMCAT_DIR/bin/startup.sh"
TOMCAT_STOP="$TOMCAT_DIR/bin/shutdown.sh"

NGINX_CONF_DIR="/etc/nginx"
NGINX_SITES_AVAILABLE_DIR="$NGINX_CONF_DIR/sites-available"
NGINX_SITES_ENABLED_DIR="$NGINX_CONF_DIR/sites-enabled"

NGINX_RELEASE_NAME="jwp"
NGINX_PM_PAGE_NAME="jwp-pm"

# 나중에 설정으로 비밀번호 없이 sudo 쓸 수 있게 하기
PASSWORD="hasta333"

# configure nginx sites-enabled to show pm page
cd $NGINX_SITES_ENABLED_DIR
ln -s $NGINX_SITES_AVAILABLE_DIR/$NGINX_PM_PAGE_NAME $NGINX_PM_PAGE_NAME
rm -f $NGINX_RELEASE_NAME
echo $PASSWORD | sudo -S nginx -s reload

# initial dir
cd ~/

# git clone if not exist
if [ ! -d "$HOME_DIR/$REPO_NAME" ]; then
  git clone $GITHUB_REPO
fi
# git dir
cd $REPO_NAME
# git checkout to set branch
git checkout -t origin/$TARGET_BRANCH
# sync with github server
git pull
# maven build
mvn -Dmaven.test.skip=true clean package

# mkdir release dir if not exist
if [ ! -d "$RELEASE_DIR/" ]; then
  mkdir $RELEASE_DIR
fi
cd $RELEASE_DIR
# mkdir based on datetime
DATETIME="$(date +"%Y-%m-%d_%H-%M-%S")"
# copy war to target dir
mkdir $DATETIME && cd $DATETIME
jar xvf $HOME_DIR/$REPO_NAME/target/$REPO_NAME.war
ln -sfn "$(pwd)" $TOMCAT_DIR/webapps/ROOT

# [re]start tomcat
$TOMCAT_STOP && $TOMCAT_START

# reconfigure nginx sites-enabled to show site page
cd $NGINX_SITES_ENABLED_DIR
ln -s $NGINX_SITES_AVAILABLE_DIR/$NGINX_RELEASE_NAME $NGINX_RELEASE_NAME
rm -f $NGINX_PM_PAGE_NAME
echo $PASSWORD | sudo -S nginx -s reload
