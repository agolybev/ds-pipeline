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
      defaultValue: true,
      description: '',
      name: 'win_32'
    )
  }
  triggers {
    cron('@midnight')
  }
  stages {
    stage('Build') {
      parallel {
        stage('Linux 64-bit build') {
          agent { label 'linux_64' }
          steps {
            script {
              def core = load "core.groovy"
              if ( params.linux_64 ) {
                core.linuxBuild(env.BRANCH_NAME)
              }
            }
          }
        }
        stage('Windows 64-bit build') {
          agent {
            node {
              label 'win_64'
              customWorkspace "C:\\core\\${env.BRANCH_NAME}\\win_64"
            }
          }
          steps {
            script {
              def core = load "core.groovy"
              if ( params.win_64 ) {
                core.windowsBuild(env.BRANCH_NAME, "x64")
              }
            }
          }
        }
        stage('Windows 32-bit build') {
          agent {
            node {
              label 'win_32'
              customWorkspace "C:\\core\\${env.BRANCH_NAME}\\win_32"
            }
          }
          steps {
            script {
              def core = load "core.groovy"
              if ( params.win_32 ) {
                core.windowsBuild(env.BRANCH_NAME, "x86")
              }
            }
          }
        }
      }
    }
  }
}
