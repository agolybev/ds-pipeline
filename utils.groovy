def checkoutRepo(String repo, String branch = 'master') {
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
                    url: 'git@github.com:ONLYOFFICE/' + repo + '.git'
                ]
            ]
        ]
    )
}

return this

def checkoutRepos(String branch = 'master')
{
    def repos = []
    repos.add('core')
    repos.add('core-ext')
    repos.add('core-fonts')
    repos.add('desktop-sdk')
    repos.add('dictionaries')
    repos.add('document-server-integration')
    repos.add('document-server-package')
    repos.add('sdkjs')
    repos.add('sdkjs-plugins')
    repos.add('server')
    repos.add('web-apps-pro')
    repos.add('Docker-DocumentServer')
    
    for (repo in repos) {
        checkoutRepo(repo, branch)
    }

    return this
}

def linuxBuild(String branch = 'master')
{
    checkoutRepos(branch)
    sh "cd core/Common/3dParty && \
        ./make.sh"
    sh "cd core && \
        make clean && \
        make all ext"
    sh "cd sdkjs && \
        make clean && \
        make all"
    sh "cd server && \
        make clean && \
        make all ext"
    sh "cd document-server-integration && \
        make all"
    sh "cd document-server-package && \
        make clean &&\
        make deploy"
    sh "cd Docker-DocumentServer && \
        make clean &&\
        make all"
    return this
}

def windowsBuild(String branch = 'master')
{
    checkoutRepos(branch)

    bat "cd core\Common\3dParty && \
            call make.bat"

    bat "cd core && \
            call \"C:\Program Files (x86)\Microsoft Visual Studio 14.0\VC\vcvarsall.bat\" x64 10.0.14393.0 && \
            mingw32-make clean &&
            mingw32-make all ext"

    bat "cd sdkjs && \
            mingw32-make clean &&
            mingw32-make all"

    bat "cd server && \
            mingw32-make clean &&
            mingw32-make all ext"

    bat "cd document-server-integration && \
            mingw32-make all"

    bat "cd document-server-package && \
            mingw32-make clean
            mingw32-make deploy"
}