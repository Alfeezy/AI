Author: Bastien Gliech

ACO PATHFINDING ALGORITHM:

DESCRIPTION:

    This ACO algorithm utlilizes Ant Colony Optimization to solve for the 
shortest distance between two locations on a weighted bidirectional graph.

RUNNING:

    1. Compile all files with >javac AntMain.java AntMap.java Ant.java Edge.java

    2. run main with >java AntMain [name] [a] [g]
        where: map.txt = name of map input file
               a = number of ants in the colony
               g = iterations (generations) to run it for

        in this specific assignment, the command will be:
          >java AntMain map.txt 10 25

    ** To change any runtime parameters, open AntMain and change static variables