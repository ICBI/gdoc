# G-DOC

The Georgetown Database of Cancer is a precision medicine platform containing molecular and clinical data from thousands of patients and cell lines, along with tools for analysis and data visualization. 
The platform enables the integrative analysis of multiple data types to understand disease mechanisms. G-DOC has three overlapping entry points for the user based on their interests: 1) Personalized Medicine, 2) Translational Research, and 3) Population Genetics. 

The Precision Medicine workflow integrates molecular and clinical research data with patient data and outcomes with patient as the central focus. The Translational Research workflow conducts multi-omic analysis of clinical samples, cell lines and animal models to discover new markers and explore connections between them and with clinical outcomes. Users can explore genetic variation across different populations, and understand how SNP frequencies impact drug metabolism through the Population Genetics workflow.

## System Architechture
G-DOC uses an in-house architectural framework that provides over 2100 biomedical research users (as of June 2017) with a comprehensive set of analysis routines and visualizations for a rich user experience through a web interface. The data and meta-data are stored in an Oracle database that allows for access control. The G-DOC  application is written in Groovy & Grails, an open source application framework that runs on the Java Virtual Machine (JVM) that is built on top of libraries such as Spring and Hibernate that meet high industry standards. This provides for a highly structured web application framework and allows for rapid development of modules. Adobe Flex and Javascript are used to provide interactive data visualizations. The system uses JBossMQ to communicate asynchronously with an analysis server on which analysis routines (such as classification, hierarchical clustering, etc.) are run using R/Bioconductor packages. This architecture has been expanded to allow storage and analysis of NGS data and medical images by using EC2 instances in the Amazon cloud computing environment. These compute intensive instances offer the ability to rapidly scale in the face of a deluge of data at a very low maintenance cost.

### The G-DOC ecosystem
The G-DOC ecosystem is a generalized concept that was realized for easy replication of the G-DOC tools for similar applications. It consists of many independent 'plugins' built on top of two base plugins to form a comprehensive ecosystem. A plugin is an independent lightweight 'mini' grails project that cannot be run on its own on a web server. 

G-core is a ‘core’ grails plugin created and installed into a skeleton grails application to provide a uniform database schema, common security framework that includes authentication, authorization, registration and basic features of a portal (exploring clinical data, saving patient lists, workflows). This is the biggest and most important plugin, and in principle the G-DOC application can be run with only the G-core plugin without any other plugin. The analysis-core plugin is another ‘core’ plugin developed to create communication between the analysis server and any type of analysis that requires computation. All plugins have a dependency on g-core plugin and those that call the analysis server are also dependent on the analysis-core plugin. The primary analysis features are built as plugins on top of these two base plugins, perform specific tasks for data management or analysis, and are independent of each other.

Such an ecosystem allows for easy creation of new plugins based on new analyses requirements, and independent installation into any application that already has the ‘core’ plugins installed. This module-based system hence gives us the flexibility to pick the necessary plugins to be able to build a G-DOC like web portal for another collaborator/application.

## Related links
* https://github.com/ICBI/grails-gcore  (our 'core' grails plugin)
* https://github.com/ICBI/analysis-server (another 'core' plugin)

## Citation
Source code and documentation developed by the Georgetown Innovation Center For Biomedical Informatics (GU-ICBI) are freely available under Lesser GNU Public License GPL v3 (http://opensource.org/licenses/gpl-3.0.html).
All uses of this software must cite this source and the publication:

* Krithika Bhuvaneshwar, Anas Belouali, Varun Singh, Robert M. Johnson, Lei Song, Adil Alaoui, Michael A. Harris, Robert Clarke, Louis M. Weiner, Yuriy Gusev and Subha Madhavan, **G-DOC Plus – an integrative bioinformatics platform for precision medicine** , *BMC Bioinformatics*, April 2016.
* Subha Madhavan,Yuriy Gusev, Michael Harris, David M Tanenbaum,Robinder Gauba,Krithika Bhuvaneshwar,Andrew Shinohara,Kevin Rosso Lavinia A Carabet,Lei Song,Rebecca B Riggins,Sivanesan Dakshanamurthy,Yue Wang,Stephen W Byers,Robert Clarke, and Louis M Weiner, **G-DOC: A Systems Medicine Platform for Personalized Oncology**, *Neoplasia*, Sep 2011.
