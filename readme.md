### Generate proman-api request-response models

>Swagger plugins are used to generate request-response models from endpoint json file 

>Use below command to generate the models 
` mvn clean install -DskipTests`


### Setup proman-db database
>sql-maven-plugin is used to execute sql files during db setup.

>localhost.properties file is used to define db properties

> maven-resources plugin default goal can be run by below command:
> This command will copy resources from resources element in pom to output directory 
` mvn resources:resources`

>**maven-resources-plugin** has deactivated delimiter `@` as this is local environment

>This will change if **env changes to test/production** etc.
 
>maven-resources-plugin is used to copy resources to output directory

> Database setup is one time step. setup profile is picked from proman-db pom.

>sql-maven-plugin execution is disabled in project build by default.setup profile is defined in
> pom to enable it selectively.

> Setup profile is used to enable it.
>Use below command select setup profile 
 
` mvn clean install -Psetup` 