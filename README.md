# Paint Shop Interview Question
You run a paint shop, and there are a few different colors of paint you can prepare.  Each color can be either "gloss" or "matte".

You have a number of customers, and each have some colors they like, either gloss or matte.  No customer will like more than one color in matte.

You want to mix the colors, so that:
  * There is just one batch for each color, and it's either gloss or matte.
  * For each customer, there is at least one color they like.
  * You make as few mattes as possible (because they are more expensive).

Your program should accept an input file as a command line argument, and print a result to standard out.  An example input file is:

```plain
5
1 M 3 G 5 G
2 G 3 M 4 G
5 M
```

The first line specifies how many colors there are.

Each subsequent line describes a customer.  For example, the first customer likes color 1 in matte, color 3 in gloss and color 5 in gloss.

Your program should read an input file like this, and print out either that it is impossible to satisfy all the customer, or describe, for each of the colors, whether it should be made gloss or matte.

The output for the above file should be:

```plain
G G G G M
``` 

## Quick Start
### Prerequisites
Java 11 must be installed on your local system.
Maven must be installed on your local system.

### Building
* `mvn clean package` will build the source code

### Running
* Start by running ```./scripts/start.sh <DATA_FILE_PATH>```.
* Sample DATA files are located in the ```data/``` directory so you should be able to run 
```./scripts/start.sh data/sample-1.txt```

