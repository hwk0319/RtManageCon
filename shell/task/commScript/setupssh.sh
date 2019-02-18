#!/bin/sh
path=`pwd`
echo  "#!/usr/bin/expect" >/tmp/sshoracle.sh
echo  "spawn sh $path/sshUserSetup.sh -user $1 -hosts \"$3\"  -advanced -noPromptPassphrase ">>/tmp/sshoracle.sh
echo  "expect (yes/no)?">>/tmp/sshoracle.sh
echo  "send yes\r">>/tmp/sshoracle.sh
echo  "expect $2@*password">>/tmp/sshoracle.sh
echo  "sleep 2">>/tmp/sshoracle.sh
echo  "send $2\r">>/tmp/sshoracle.sh
echo  "expect $2@*password">>/tmp/sshoracle.sh
echo  "sleep 2">>/tmp/sshoracle.sh
echo  "send $2\r">>/tmp/sshoracle.sh
echo  "expect $2@*password">>/tmp/sshoracle.sh
echo  "sleep 2">>/tmp/sshoracle.sh
echo  "send $2\r">>/tmp/sshoracle.sh
echo  "expect $2@*password">>/tmp/sshoracle.sh
echo  "sleep 2">>/tmp/sshoracle.sh
echo  "send $2\r">>/tmp/sshoracle.sh
echo  "interact">>/tmp/sshoracle.sh
chmod a+x /tmp/sshoracle.sh
su - $1 -c "/usr/bin/expect /tmp/sshoracle.sh"
rm -rf /tmp/sshoracle.sh
