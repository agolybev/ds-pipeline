def checkoutRepo(String repo, String branch = 'master', String company = 'ONLYOFFICE') {
    checkout([
            $class: 'GitSCM',
            branches: [[
                    name: branch
                ]
            ],
            doGenerateSubmoduleConfigurations: false,
            extensions: [[
                    $class: 'RelativeTargetDirectory',
                    relativeTargetDir: repo
                ]
            ],
            submoduleCfg: [],
            userRemoteConfigs: [[
                    url: "git@github.com:${company}/${repo}.git"
                ]
            ]
        ]
    )
}

return this

def getReposList(String branch = 'master')
{
    def repos = []
    repos.add('build_tools')
    repos.add('core')
    repos.add('core-fonts')
    repos.add('desktop-sdk')
    repos.add('dictionaries')
    repos.add('document-server-integration')
    repos.add('document-server-package')
    repos.add('sdkjs')
    repos.add('sdkjs-comparison')
    repos.add('sdkjs-plugins')
    repos.add('server')
    repos.add('web-apps')
    repos.add('Docker-DocumentServer')
    return repos
}

def checkoutRepos(String branch = 'master')
{    
    for (repo in getReposList()) {
        checkoutRepo(repo, branch)
    }

    return this
}

def tagRepos(String tag)
{
    for (repo in getReposList()) {
        sh "cd ${repo} && \
            git tag -l | xargs git tag -d && \
            git fetch --tags && \
            git tag ${tag} && \
	        git push origin --tags"
    }

    return this
}

def linuxBuild(String branch = 'master', String platform = 'native', Boolean clean = true, Boolean noneFree = false)
{
    checkoutRepos(branch)

    String confParams = "
      --module \"server\"
      --platform ${platform}\
      --update false\
      --branch ${branch}\
      --clean ${clean.toString()}\
      --qt-dir \$QT_PATH"

    if (noneFree) {
      confParams = confParams.concant("--sdkjs-addon sdkjs-comparison")
    }

    sh "cd build_tools && \
        ./configure.py ${confParams} &&\
        ./make.py"
    sh "cd server && \
        make all ext"
    sh "cd document-server-integration && \
        make all"
    sh "cd document-server-package && \
        make clean && \
        make deploy"
    sh "cd Docker-DocumentServer && \
        make clean && \
        make deploy"
    return this
}

def windowsBuild(String branch = 'master', String platform = 'native', Boolean clean = true, Boolean noneFree = false)
{
    checkoutRepos(branch)

    String confParams = "\
            --module \"server\"\
            --platform ${platform}\
            --update false\
            --branch ${branch}\
            --clean ${clean.toString()}\
            --sdkjs-addon sdkjs-comparison\
            --qt-dir \"C:\\Qt\\Qt5.9.8\\5.9.8\"\
            --qt-dir-xp \"C:\\Qt\\Qt5.6.3\\5.6.3\""
            
    if (noneFree) {
      confParams = confParams.concant("--sdkjs-addon sdkjs-comparison")
    }

    bat "cd build_tools &&\
            call python configure.py ${confParams} &&\
            call python make.py"

    bat "cd server && \
            mingw32-make all ext"

    bat "cd document-server-integration && \
            mingw32-make all"

    bat "cd document-server-package && \
            mingw32-make clean && \
            mingw32-make deploy"
}
