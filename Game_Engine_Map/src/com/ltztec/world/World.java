package com.ltztec.world;

import java.awt.Graphics;
import java.util.ArrayList;


import com.ltztec.entities.Enemy;
import com.ltztec.entities.Entity;
import com.ltztec.entities.House;
import com.ltztec.entities.Player;
import com.ltztec.graficos.Spritesheet;
import com.ltztec.main.Game;

public class World {

	public static Tile[] tiles;
	public static House[] housetile;
	public static int WIDTH, HEIGHT;
	public static int TILE_SIZE = 32;
	public static int HOUSE_SIZE = 127;

	public World(String path) {

		Game.player.setX(0);
		Game.player.setY(0);
		WIDTH = 100;
		HEIGHT = 100;
		tiles = new Tile[WIDTH * HEIGHT];

		for(int xx = 0;xx < WIDTH;xx++) {
			for(int yy = 0;yy < HEIGHT;yy++) {
				tiles[xx + yy * WIDTH] = new WallTile(xx * 32, yy * 32, Tile.TILE_WALL);

			}
		}
		
		
		int dir = 0;
		int xx = 0, yy = 0;
		tiles[xx + yy * WIDTH] = new FloorTile(xx * 32, yy * 32, Tile.TILE_FLOOR);

		for (int i = 0; i < 200; i++) {
			if (dir == 0) {
				// direita
				if (xx < WIDTH) {
					xx++;
				}
			} else if (dir == 1) {
				// esquerda
				if (xx > 0) {
					xx--;
				}
			} else if (dir == 2) {
				// BAIXO
				if (yy < HEIGHT) {
					yy++;
				}
			} else if (dir == 3) {
				// cima
				if (yy > 0) {
					yy--;
				}
			}

			if (Game.rand.nextInt(100) < 30) {
				dir = Game.rand.nextInt(4);
			}

		}

	}

	public static boolean isFree(int xnext, int ynext) {
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;

		int x2 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;

		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext + TILE_SIZE - 1) / TILE_SIZE;

		int x4 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y4 = (ynext + TILE_SIZE - 1) / TILE_SIZE;

		return !((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile)
				|| (tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile)
				|| (tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile)
				|| (tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile));

	}

	public static void restarGame(String level) {
		Game.enemies.clear();
		Game.entities.clear();
		Game.bullets.clear();
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(10, 0, 32, 32, Game.spritesheet.getSprite(0, 0, 32, 32));
		Game.entities.add(Game.player);
		Game.world = new World("/" + level);
		return;
	}

	public void render(Graphics g) {

		int xstart = Camera.x >> 6;
		int ystart = Camera.y >> 6;

		int xfinal = xstart + (Game.WIDTH >> 2);
		int yfinal = ystart + (Game.HEIGHT >> 2);
		for (int xx = xstart; xx <= xfinal; xx++) {
			for (int yy = ystart; yy <= yfinal; yy++) {
				if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
					continue;
				}
				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render(g);
			}
		}
	}

	public static void renderMiniMap() {
		for (int i = 0; i < Game.miniMapPixels.length; i++) {
			Game.miniMapPixels[i] = 0;
		}
		for (int xx = 0; xx < WIDTH; xx++) {
			for (int yy = 0; yy < HEIGHT; yy++) {
				if (tiles[xx + (yy * WIDTH)] instanceof WallTile) {
					Game.miniMapPixels[xx + (yy * WIDTH)] = 0xff778284;
				}
			}
		}
		int xPlayer = Game.player.getX() / 32;
		int yPlayer = Game.player.getY() / 32;

		Game.miniMapPixels[xPlayer + (yPlayer * WIDTH)] = 0xff007F7F;
	}

}
