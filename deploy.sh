#!/bin/bash
# 进入项目目录
cd /data/server/xxx/Java-Boot-Template/

# 管理员
sudo su

source /etc/profile

# 从仓库拉去项目
git pull --rebase

#拉取项目可能需要时间,等2秒钟
sleep 2

# maven打包 --先clean在打包过程中跳过测试,节省时间
mvnd clean package -Dmaven.skip.test=true

# 打包可能需要一点时间,等十秒钟
sleep 10

#jar包文件路径及名称（目录按照各自配置）
APP_NAME=/data/server/xxx/Java-Boot-Template/boot-server/target/boot-server.jar

#日志文件路径及名称（目录按照各自配置）
LOG_FILE=/data/server/xxx/server.log


#查询进程，并杀掉当前jar/java程序

server_pid=`ps -ef|grep $APP_NAME | grep -v grep | awk '{print $2}'`
kill -9 $server_pid
echo "$server_pid进程终止成功"

sleep 2

#判断jar包文件是否存在，如果存在启动jar包，并时时查看启动日志

if test -e $APP_NAME
then
echo '文件存在,开始启动此程序...'

# 启动jar包，指向日志文件，2>&1 & 表示打开或指向同一个日志文件
#nohup java -jar $APP_NAME > $LOG_FILE 2>&1 &

nohup java -jar -Xms128m -XX:PermSize=128m -XX:MaxPermSize=128m -Dspring.profiles.active=prod $APP_NAME > $LOG_FILE 2>&1 &

#实时查看启动日志（此处正在想办法启动成功后退出）
#tail -f $LOG_FILE

#输出启动成功（上面的查看日志没有退出，所以执行不了，可以去掉）

echo "$APP_NAME 启动成功..."
else
echo "$APP_NAME 文件不存在,请检查。"
fi
