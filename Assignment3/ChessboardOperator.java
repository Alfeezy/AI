/*
  Name: Bastien Gliech
  CIS 421 Artificial Intelligence
  Assignment: 3 - Genetic Algorithm
  Due: February 5, 2018
*/

// ChessboardOperator is a static class that hold all methods that deals
// with crossing, mutating, pairing, and everything else involved with
// bla bla bla i'll figure it out
// TODO:
//  - fitness: 1 / (conflicts + E) where E is a small ass number
//  - parents selection:
//    - Set current_member = 1
//    - WHILE (current_member <= N * 10%) 
//      - Pick 3 individuals randomly, with or without replacement1
//      - Select the best of these 3 by comparing their fitness values
//      - Denote that chosen individual as i
//      - Set mating_pool[current_member] = i
//      - Set current_member = current_member + 1
//  - crossover: snip snip
//  - mutation: 10% of all spots