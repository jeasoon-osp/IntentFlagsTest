@echo off

git add .

git commit -a -m "dev" && git push dev master:master

ssh root@172.30.255.27 "cd /Projects/MyTestApp && git reset --hard HEAD && gradle clean assembleDebug installDebug && adb shell am start com.vixtel.mytestapp/com.vixtel.mytestapp.MainActivity"