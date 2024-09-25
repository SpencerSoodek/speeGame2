package Entities;

import java.awt.Point;

public class PathNode {
  Point point;
  int gScore;
  int cost;

  PathNode(Point point, int gScore, int cost) {
    this.point = point;
    this.gScore = gScore;
    this.cost = cost;
  }

}
