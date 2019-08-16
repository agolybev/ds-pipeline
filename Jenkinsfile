pipeline {
  agent none
  parameters {
    booleanParam (
      defaultValue: true,
      description: '',
      name: 'linux_64'
    )
    booleanParam (
      defaultValue: true,
      description: '',
      name: 'win_64'
    )
    booleanParam (
      defaultValue: false,
      description: '',
      name: 'documentserver'
    )
    booleanParam (
      defaultValue: false,
      description: '',
      name: 'documentserver-de'
    )
    booleanParam (
      defaultValue: true,
      description: '',
      name: 'documentserver-ie'
    )
  }
  triggers {
    cron('@daily')
  }
  stages {
    stage('Prepare') {
      steps {
        script {
          def branchName = env.BRANCH_NAME
          def productVersion = "5.4.99"
          def pV = branchName =~ /^(release|hotfix)\\/v(.*)$/
          if(pV.find()) {
            productVersion = pV.group(2)
          }
          env.PRODUCT_VERSION = productVersion
        }
      }
    }
    stage('Build') {
      parallel {
        stage('DocumentServer(linux_64) build') {
          agent { label 'linux_64' }
          environment { PRODUCT_NAME = "documentserver" }
          steps {
            script {
              def utils = load "utils.groovy"
              if ( params.linux_64 && params.documentserver) {
                utils.linuxBuild(env.BRANCH_NAME )
              }
            }
          }
        }
        stage('DocumentServer-DE(linux_64) build') {
          agent { label 'linux_64' }
          environment { PRODUCT_NAME = "documentserver-de" }
          steps {
            script {
              def utils = load "utils.groovy"
              if ( params.linux_64 && params.documentserver-de) {
                utils.linuxBuild(env.BRANCH_NAME)
              }
            }
          }
        }
        stage('DocumentServer-IE(linux_64) build') {
          agent { label 'linux_64' }
          environment { PRODUCT_NAME = "documentserver-ie" }
          steps {
            script {
              def utils = load "utils.groovy"
              if ( params.linux_64 && params.documentserver-ie) {
                utils.linuxBuild(env.BRANCH_NAME)
              }
            }
          }
        }
        stage('DocumentServer(win_64) build') {
          agent {
            node {
              label 'win_64'
              customWorkspace 'C:\\ds\\os'
            }
          }
          environment { PRODUCT_NAME = "DocumentServer" }
          steps {
            script {
              def utils = load "utils.groovy"
              if ( params.win_64 && params.documentserver ) {
                utils.windowsBuild(env.BRANCH_NAME)
              }
            }
          }
        }
        stage('DocumentServer-DE(win_64) build') {
          agent {
            node {
              label 'win_64'
              customWorkspace 'C:\\ds\\de'
            }
          }
          environment { PRODUCT_NAME = "DocumentServer-DE" }
          steps {
            script {
              def utils = load "utils.groovy"
              if ( params.win_64 && params.documentserver-de ) {
                utils.windowsBuild(env.BRANCH_NAME)
              }
            }
          }
        }
        stage('DocumentServer-IE(win_64) build') {
          agent {
            node {
              label 'win_64'
              customWorkspace 'C:\\ds\\ie'
            }
          }
          environment { PRODUCT_NAME = "DocumentServer-IE" }
          steps {
            script {
              def utils = load "utils.groovy"
              if ( params.win_64 && params.documentserver-ie ) {
                utils.windowsBuild(env.BRANCH_NAME)
              }
            }
          }
        }
      }
    }
  }
}
