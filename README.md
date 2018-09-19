# accelQ Connect Bamboo

accelQ supports CI integration for Jenkins and Bamboo through a plug-in. Make sure the plugin is properly installed on the CI system before proceeding.

## Step 1 

On the accelQ system, kick off a test run as you would normally do. Under the "When to Run" option, select "CI Job". Configure the test run with required test suite, browser, operating system, test execution host etc. Click on "Create Job".

![mceclip0](https://user-images.githubusercontent.com/40689807/43002015-f2c78a0a-8c44-11e8-8c77-d3c4bd89d2ed.png)


## Step 2 

In the confirmation modal, copy the job ID to be used in the next step.

![mceclip0 1](https://user-images.githubusercontent.com/40689807/43002053-143d046c-8c45-11e8-97bc-e2b32107c8d3.png)



## Step 3 

On the Jenkins or Bamboo front, add “accelQ Connect” as a step in the required project and provide information such as accelQ project name, user ID, password. In addition, copy/paste the Job ID that was created in the previous step.

![mceclip0 2](https://user-images.githubusercontent.com/40689807/43002083-26dc4aec-8c45-11e8-94aa-b2eb7676ff3f.png)



accelQ job gets executed based on the triggers setup for the CI project and returns back the summary result. Test execution notification is emailed to the recipients specified during the test run creation. You are also present a link to the complete result.


## About accelQ
[accelQ](https://www.accelq.com/product.html) is a cloud based continuous testing platform for Functional & API Automation. 
accelQ brings automation to entire quality lifecycle with a highly intelligent platform powered with advanced autonomics.

- Codeless natural language Functional, API Automation
- No IDE, Fully browser based 
- Visualize and drive quality with accelQ Universe
- Design/Automate/Maintain your testing 3X Faster


