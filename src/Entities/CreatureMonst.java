package Entities;

import main.Direction;
import main.GamePanel;

public class CreatureMonst extends Entity {


  public CreatureMonst(GamePanel gp, int x, int y) {
    super(gp);
    this.col = x;
    this.row = y;
    this.worldX = col * gp.tileSize;
    this.worldY = row * gp.tileSize;
    // gp.tileM.occupiedTiles[col][row] = true;
    this.speed = 1;

    this.direction = Direction.DOWN;
    this.stepRate = 60;
    this.state = EntityState.IDLE;
    this.attackCooldown = 60;
    this.attackCooldownCounter = 60;
    this.maxHealth = 5;
    this.health = maxHealth;
    getImage();
  }

  public void getImage() {
    up1 = setup("/monsters/creature/creature_up_1");
    up2 = setup("/monsters/creature/creature_up_2");
    left1 = setup("/monsters/creature/creature_left_1");
    left2 = setup("/monsters/creature/creature_left_2");
    right1 = setup("/monsters/creature/creature_right_1");
    right2 = setup("/monsters/creature/creature_right_2");
    down1 = setup("/monsters/creature/creature_down_1");
    down2 = setup("/monsters/creature/creature_down_2");
    attackup = setup("/monsters/creature/creature_attack_up");
    attackleft = setup("/monsters/creature/creature_attack_left");
    attackright = setup("/monsters/creature/creature_attack_right");
    attackdown = setup("/monsters/creature/creature_attack_down");
  }

  public void onReachedTile() { }

  public void meleeAttack() {
    gp.player.health -= 1;
    if (gp.player.col > col) {
      direction = direction.RIGHT;
    }
    if (gp.player.col < col) {
      direction = direction.LEFT;
    }
    if (gp.player.row < row) {
      direction = direction.UP;
    }
    if (gp.player.row > row) {
      direction = direction.DOWN;
    }
  }


  public void update() {
    if (displayHealth) {
      if (displayHealthCounter < displayHealthFrames) {
        displayHealthCounter++;
      } else {
        displayHealth = false;
        displayHealthCounter = 0;
      }
    }
    if (state == EntityState.MOVE) {
      keepMovingInDirection();
    }

    if (attackCooldownCounter > attackCooldown) {
      if (state == EntityState.ATTACK) {
        this.state = EntityState.IDLE;
      }
      if (nextToPlayer() && state == EntityState.IDLE) {
        state = EntityState.ATTACK;
        meleeAttack();
        attackCooldownCounter = 0;
      }
    }
    attackCooldownCounter++;

    if (state == EntityState.IDLE) {

      // If there is no path to the player, wait for the cooldown to check for a path.
      if (noPath && noPathCooldownCounter >= noPathCooldown) {
        noPath = false;
        noPathCooldownCounter = 0;
      } else if (noPath || nextToPlayer()) {
        noPathCooldownCounter++;
      } else {
        if (pathCounter >= this.stepsToResetPath || path[pathCounter] == null) {
          aStarPath();
          pathCounter = 0;
        }
        //if (path[pathCounter] != null){
          direction = path[pathCounter];
          pathCounter++;

        // Check for collision after setting direction.
        collisionOn =
            gp.cChecker.checkCollision(this) || gp.cChecker.objCollision(this) || gp.cChecker
                .checkOccupied(this);

        // Start moving in the direction if no collision.
        if (!collisionOn) {
          startMoving();
        } else {
          aStarPath();
        }
      }

    }
  }
}
