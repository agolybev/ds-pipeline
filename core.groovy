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

def getReposList()
{
    def repos = []
    repos.add('build_tools')
    repos.add('core')
    repos.add('desktop-sdk')

    return repos
}

def checkoutRepos(String branch = 'master')
{    
    for (repo in getReposList()) {
        checkoutRepo(repo, branch)
    }

    return this
}

def linuxBuild(String branch = 'master')
{
    checkoutRepos(branch)
    
    String confParams = "\
        --module \"core\"\
        --platform linux_64\
        --update false\
        --branch ${branch}\
        --clean true\
        --qt-dir \$QT_PATH"

    sh "cd build_tools && \
        ./configure.py ${confParams} &&\
        ./make.py"
    sh "cd core && \
        make deploy"

    return this
}

def windowsBuild(String branch = 'master', String platform = 'x64', String sdk = '10.0.14393.0')
{
    checkoutRepos(branch)

    String confParams = "\
        --module \"core\"\
        --platform win_64\
        --update false\
        --branch ${branch}\
        --clean true\
        --qt-dir \"C:\\Qt\\Qt5.9.8\\5.9.8\"\
        --qt-dir-xp \"C:\\Qt\\Qt5.6.3\\5.6.3\""

    bat "cd build_tools &&\
            call python configure.py ${confParams} &&\
            call python make.py"

    bat "cd core && \
            call \"C:\\Program Files (x86)\\Microsoft Visual Studio 14.0\\VC\\vcvarsall.bat\" ${platform} ${sdk} && \
            mingw32-make deploy"

    return this
}
