<a name="readme-top"></a>

<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->

<h3 align="center">CHATOP</h3>

<p align="center">
School work : A basic platform enabling individuals to post rental listings.
</p>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#frontend-installation">Frontend Installation</a></li>
        <li><a href="#backend-installation">Backend Installation</a></li>
      </ul>
    </li>
    <li><a href="#usages">Usages</a></li>
    <li><a href="#swagger">Swagger</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

[![Product Name Screen Shot][product-screenshot]](https://example.com)

Here's a blank template to get started: To avoid retyping too much info. Do a search and replace with your text editor for the following: `github_username`, `repo_name`, `twitter_handle`, `linkedin_username`, `email_client`, `email`, `project_title`, `project_description`

<p align="right">(<a href="#readme-top">back to top</a>)</p>



### Built With

* Spring Boot 3.1.5
* Spring Security 6.1.5
* Lombok Annotations
* MySQL 8.0
* Spring JPA
* Jakarta Validation

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

To run the app, you will need to clone two repositories. One for the front-end and the current one for the back-end.

In case you wouldn't want to install the front-end, you could simply test the back-end using the following swagger : http://127.0.0.1:3001/swagger-ui/index.html after installing and running the back-end.

### Prerequisites

First you need to install these softwares, packages and librairies :

* nodejs
   ```
   https://nodejs.org/en
   ```
  
* npm (after installing nodejs)
    ```
    npm install -g npm
    ```
  
* java development kit 17 (jdk17) and if needed, add a JAVA_HOME environment variable pointing at your java installation folder. 
   ```
   https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
   ```
  
* mysql & mysqlwork bench (full install)
   ```
   https://dev.mysql.com/downloads/windows/
   ```

* the angular cli (after installing nodejs)
    ```
    npm install -g @angular/cli
    ```
  
<p align="right">(<a href="#readme-top">back to top</a>)</p>



### FrontEnd Installation

1. Clone the front end repo
   ```
   git clone https://github.com/OpenClassrooms-Student-Center/Developpez-le-back-end-en-utilisant-Java-et-Spring.git
   ```

2. Install the packages needed for the front end (node & npm must be installed)
   ```
   npm install
   ```
   
3. Start the Front End of the App (npm & the angular cli must be installed)
    ```
    npm run start
    ```
   
<p align="right">(<a href="#readme-top">back to top</a>)</p>



### Backend Installation

1. Clone the repo
   ```
   git clone https://github.com/ask0ldd/P3-SpringV2.git
   ```
2. Install MySQL & Workbench and define a root password.

3. Create an env.properties file into the ressources folder of the project and add the following lines, with your created password replacing 'yourownrootpassword' (don't do this on a production server) :
    ```
    spring.datasource.username=root
    spring.datasource.password=yourownrootpassword
    ```
4. Open MySQL Workbench
    ```
    The following connection should already be set up : 
       Local Instance MySQL80 / user : root / url : localhost:3306.
    ```
5. Create an "immo" empty schema with Workbench. You don't need to do more since all the mandatory tables will be created by Spring JPA when starting up the project.

6. Build the project.
    ```
    mvnw package
    ```

7. Run the project with Maven.
    ```
    mvnw spring-boot:run
    ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- USAGE EXAMPLES -->
## Usages

* Register a new user account.
* Log in to a user account.
* View personal user information.
* Post a new rental listing, including a rental property image.
* Update an existing rental listing.
* Browse all posted rental listings.
* View full details of a specific rental listing.
* Leave a message for a rental property landlord.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- SWAGGER -->
## Swagger

After launching the server, go to the following url :

    http://127.0.0.1:3001/swagger-ui/index.html

Don't forget to register and use the returned JWT to authenticate yourself.

<p align="right">(<a href="#readme-top">back to top</a>)</p>