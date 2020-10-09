# DB Best Test Task

## Description
Let us consider a water pipeline system. We use three parameters to describe it: starting point id, endpoint id, and pipe length. 
Your goal is to create a Java application which answers the following questions:
1) Does the route between two points (id-A and id-B) exist? 
2) If the answer is yes, then calculate the minimal route length between id-A and id-B.

## Requirements
1) Your application should upload the file that describes the water pipeline system. Load it into an H2 database. 
2) Also, the application should upload the file with a set of points. 
3) You may utilize JavaFX to create the application's UI or leverage the CLI (command-line interface). 
4) You need to create a readme.txt file. This file should explicitly describe how your app works. 
5) Software have to support JDK 11.

## Application description

#### Used technologies in this application:
* Spring (Boot, Data)
* H2 Database
* OpenCSV
* Thymeleaf
* HTML, Bootstrap
* Maven
* Lombok

___

When the application starts, 3 tables are created in the H2 (in memory) database:
1)	WATERPIPELINE - the table in which the values are written from input_pipeline.csv.
2)	POINT – the table in which the values are written from input_route.csv.
3)	RESULT – the table with the result of program execution.

After starting the application, go to http://localhost:8080/

Next, you need to download 2 files in order to write them to the H2 database:
1)	A CSV file that describes the water pipeline system. [input_pipeline.csv](src/main/resources/csv/input_pipeline.csv)
2)	A CSV file with a set of points, between which you need to find the route. [input_route.csv](src/main/resources/csv/input_route.csv)

When you press the "Get Result" button, 2 files are parsed using the methods of the OpenCSV library and written to the WATERPIPELINE and POINT tables of the h2:mem:testdb. 

After, based on the received data, the RESULT table is written. The logic for writing to the RESULT table is implemented using Dijkstra's algorithm.

Then the user is redirected to the page http://localhost:8080/result , where the results are displayed, as well as the results are output to the console.
You can get the results in the form of a csv file by clicking on the "Download file" button, when you click, records from the RESULT table are written to a file named "result.csv" using the methods of the OpenCSV library.




