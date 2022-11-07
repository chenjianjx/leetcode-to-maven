#!/usr/bin/env bash

mvn clean install -DskipTests
artifact_version=`mvn help:evaluate -Dexpression=project.version -q -DforceStdout`
local_repo_dir=`mvn help:evaluate -Dexpression=settings.localRepository -q -DforceStdout`

echo "--------------------------------------------------------------------------------"

l2m_command="java -jar ${local_repo_dir}/com/github/chenjianjx/leetcode-to-maven/${artifact_version}/leetcode-to-maven-${artifact_version}-shaded.jar \$*"
l2m_command_file=/usr/local/bin/l2m

echo "#!/usr/bin/env bash" > /usr/local/bin/l2m
echo "echo l2m version is ${artifact_version}" >> /usr/local/bin/l2m
echo ${l2m_command} >> /usr/local/bin/l2m
chmod +x $l2m_command_file

echo "Successfully installed version ${artifact_version}. To uninstall, just delete file:  ${l2m_command_file}"