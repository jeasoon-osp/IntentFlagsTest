#!/bin/bash

adb shell "echo; while true; do date \"+%Y-%m-%d %T\"; echo; dumpsys activity com.jeasoon.intent | grep -E '^TASK|^ +ACTIVITY|mResumed'; echo -e '\n--------------------------------------------------------------------------\n'; sleep 5;done"

