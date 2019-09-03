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
    repos.add('core')
    repos.add('core-ext')
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
    sh "cd core/Common/3dParty && \
        ./make.sh"
    sh "cd core && \
        make clean && \
        make all ext desktop deploy"

    return this
}

def windowsBuild(String branch = 'master', String platform = 'x64', String sdk = '10.0.14393.0')
{
    checkoutRepos(branch)

    bat "cd core\\Common\\3dParty && \
            call make.bat"

    bat "cd core && \
            call \"C:\\Program Files (x86)\\Microsoft Visual Studio 14.0\\VC\\vcvarsall.bat\" ${platform} ${sdk} && \
            mingw32-make clean && \
            mingw32-make all ext desktop deploy"

    return this
}