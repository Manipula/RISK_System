node {   
	stage('SCM') {    
      		git 'https://github.com/Manipula/RISK_System.git'
    	}
	stage('QA') {    
		sh 'sonar-scanner'
    	}
   	stage('build') {		def mvnHome = tool 'M3' 			sh "${mvnHome}/bin/mvn -B clean package"          	}
    	stage('deploy') { 
		sh "docker stop my || true"         
		sh "docker rm my || true"         
		sh "docker run -p 12345:8000 -v /home/project:/usr/local/ -d maven"  
		sh "docker cp target/ApplicationIntegration.war my:/usr/local/tomcat/webapps"  
    	}
    	stage('results') {
		archiveArtifacts artifacts: '**/target/*.war', fingerprint: true
	} 
}