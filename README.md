# accelQ Connect Bamboo

accelQ supports CI integration for Jenkins and Bamboo through a plug-in. Make sure the plugin is properly installed on the CI system before proceeding.

##Step 1 

On the accelQ system, kick off a test run as you would normally do. Under the "When to Run" option, select "CI Job". Configure the test run with required test suite, browser, operating system, test execution host etc. Click on "Create Job".


##Step 2 

In the confirmation modal, copy the job ID to be used in the next step.

##Step 3 

On the Jenkins or Bamboo front, add “accelQ Connect” as a step in the required project and provide information such as accelQ project name, user ID, password. In addition, copy/paste the Job ID that was created in the previous step.


accelQ job gets executed based on the triggers setup for the CI project and returns back the summary result. Test execution notification is emailed to the recipients specified during the test run creation. You are also present a link to the complete result.
